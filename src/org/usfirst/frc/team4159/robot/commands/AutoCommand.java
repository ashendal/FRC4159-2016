package org.usfirst.frc.team4159.robot.commands;

import org.usfirst.frc.team4159.robot.Robot;
import org.usfirst.frc.team4159.robot.subsystems.Drivetrain;
import org.usfirst.frc.team4159.robot.subsystems.Shooter.TriggerPosition;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class AutoCommand extends Command {

    private static final double DRIVE_SPEED = 6.0;
    private static final double STRAIGHT_DRIVE_COEFF = 2.0 / 90.0;

    private Timer autoTime = new Timer();
    private Defense defense;
    private boolean isDone = false;

    private double shooterSetpoint;
    private double startStage;
    private double driveStage;
    private double backStage;
    private double stabbyStage;

    private boolean inSetup = false;
    private boolean inDrive = false;
    private boolean inSpit = false;
    private boolean inBack = false;

    private Timer stabbyTime = new Timer();
    private Timer driveTime = new Timer();
    private Timer backTime = new Timer();

    private SetShooterAngle setShooterAngle;

    private double startAhrsAngle;

    Spit spitCommand;

    public AutoCommand(Defense d) {
        this();
        defense = d;
    }

    public AutoCommand() {
        requires(Robot.drivetrain);
    }

    protected void initialize() {
        SmartDashboard.putString("Status", "Auto init");

        setShooterAngle = new SetShooterAngle(Robot.lifter.getAngle());

        switch (defense) {
        case LOW_BAR:
            shooterSetpoint = 8.3;
            startStage = 5;
            driveStage = 3.5;
            stabbyStage = 1;
            break;
        case PASSIVE:
            shooterSetpoint = 45;
            startStage = 3;
            driveStage = 3.5;
            break;
        case SPY:
            this.cancel();
            break;
        }

        backStage = driveStage;

        Robot.drivetrain.enable();
        Robot.drivetrain.setGear(Drivetrain.SpeedGear.LOW);

        setShooterAngle.start();
        setShooterAngle.setAngle(Robot.lifter.getAngle());

        Robot.shooter.setTrigger(TriggerPosition.CLOSED);

        Robot.ahrs.reset();

        startAhrsAngle = Robot.ahrs.getYaw();

        isDone = false;
        inDrive = false;
        inSpit = false;
        inBack = false;
        inSetup = true;

        autoTime.reset();
        driveTime.reset();
        backTime.reset();
        stabbyTime.reset();

        autoTime.start();
    }

    private boolean stabbyGoneUp = false;

    protected void execute() {
        setShooterAngle.setAngle(shooterSetpoint);

        if (inSetup) {
            SmartDashboard.putString("Status", "Auto setup");
            if (defense == Defense.LOW_BAR) {
                if (!stabbyGoneUp) {
                    Robot.stabbyStabby.set(0.5);
                    stabbyGoneUp = !Robot.stabbyUpperLimit.get();
                    if (stabbyGoneUp) {
                        stabbyTime.start();
                    }
                }

                if (stabbyTime.get() <= stabbyStage)
                    Robot.stabbyStabby.set(-0.75);
                else
                    Robot.stabbyStabby.set(0);

                if (setShooterAngle.getError() < 5 && autoTime.get() > startStage && stabbyTime.get() > stabbyStage) {
                    inSetup = false;
                    inDrive = true;
                    driveTime.start();
                } else {
                    if (setShooterAngle.getError() < 5 && autoTime.get() > startStage) {
                        autoTime.stop();
                        inSetup = false;
                        inDrive = true;
                        driveTime.start();
                    }
                }
            }
        } else if (inDrive) { // If ready
            SmartDashboard.putString("Status", "Auto drive");
            if (driveTime.get() < driveStage) {
                double errorAngle = Robot.ahrs.getYaw() - startAhrsAngle;

                SmartDashboard.putNumber("auto_error", errorAngle);

                Robot.drivetrain.set(DRIVE_SPEED + errorAngle * STRAIGHT_DRIVE_COEFF,
                        DRIVE_SPEED - errorAngle * STRAIGHT_DRIVE_COEFF);
                SmartDashboard.putNumber("leftSpeed", DRIVE_SPEED + errorAngle * STRAIGHT_DRIVE_COEFF);
                SmartDashboard.putNumber("rightSpeed", DRIVE_SPEED - errorAngle * STRAIGHT_DRIVE_COEFF);
                // Robot.drivetrain.set(DRIVE_SPEED, DRIVE_SPEED);
            } else {
                Robot.drivetrain.set(0, 0);
                inDrive = false;
                driveTime.stop();

                // isDone = true;

                inSpit = true;
                spitCommand = new Spit();
                spitCommand.start();
            }
        } else if (inSpit) {
            SmartDashboard.putString("Status", "Auto spit");
            SmartDashboard.putBoolean("spit running", spitCommand.isRunning());
            if (!spitCommand.isRunning()) {
                inSpit = false;
                inBack = true;
                backTime.start();
            }
        } else if (inBack) {
            SmartDashboard.putString("Status", "Auto drive back");
            if (backTime.get() < backStage) {
                double errorAngle = Robot.ahrs.getYaw() - startAhrsAngle;

                SmartDashboard.putNumber("auto_error", errorAngle);

                Robot.drivetrain.set(-1 * (DRIVE_SPEED - errorAngle * STRAIGHT_DRIVE_COEFF),
                        -1 * (DRIVE_SPEED + errorAngle * STRAIGHT_DRIVE_COEFF));
                SmartDashboard.putNumber("leftSpeed", -1 * (DRIVE_SPEED - errorAngle * STRAIGHT_DRIVE_COEFF));
                SmartDashboard.putNumber("rightSpeed", -1 * (DRIVE_SPEED + errorAngle * STRAIGHT_DRIVE_COEFF));
                // Robot.drivetrain.set(-DRIVE_SPEED, -DRIVE_SPEED);
            } else {
                Robot.drivetrain.set(0, 0);
                inBack = false;
                isDone = true;
                backTime.stop();
            }
        }

    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isDone;
    }

    // Called once after isFinished returns true
    protected void end() {
        SmartDashboard.putString("Status", "Auto finished");
        setShooterAngle.cancel();
        Robot.drivetrain.set(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }

    public enum Defense {
        LOW_BAR, PASSIVE, SPY
    }
}
