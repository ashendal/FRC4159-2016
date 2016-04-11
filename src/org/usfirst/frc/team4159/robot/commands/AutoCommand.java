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

    private static final double DRIVE_SPEED = 8.0;

    private Timer autoTime = new Timer();
    private Defense defense;
    private boolean isDone = false;

    private double shooterSetpoint;
    private double startStage;
    private double driveStage;

    private boolean inSetup = false;
    private boolean inDrive = false;

    private Timer driveTime = new Timer();

    private SetShooterAngle setShooterAngle;

    private double startAhrsAngle;

    public AutoCommand(Defense d) {
        this();
        defense = d;
    }

    public AutoCommand() {
        requires(Robot.drivetrain);
        requires(Robot.shooter);
    }

    protected void initialize() {
        SmartDashboard.putString("Status", "Auto init");

        setShooterAngle = new SetShooterAngle(Robot.lifter.getAngle());

        driveTime.reset();

        autoTime.reset();
        autoTime.start();

        switch (defense) {
        case LOW_BAR:
            shooterSetpoint = 0;
            startStage = 3;
            driveStage = 5;
            break;
        case SECOND:
            shooterSetpoint = 45;
            startStage = 3;
            driveStage = 5;
            break;
        case THIRD:
            shooterSetpoint = 45;
            startStage = 3;
            driveStage = 5;
            break;
        case FOURTH:
            shooterSetpoint = 45;
            startStage = 3;
            driveStage = 5;
            break;
        case FIFTH:
            shooterSetpoint = 45;
            startStage = 3;
            driveStage = 5;
            break;
        case SPY:
            this.cancel();
            break;
        }

        Robot.drivetrain.enable();
        Robot.drivetrain.setGear(Drivetrain.SpeedGear.HIGH);

        setShooterAngle.start();
        setShooterAngle.setAngle(Robot.lifter.getAngle());

        Robot.shooter.setTrigger(TriggerPosition.CLOSED);

        Robot.ahrs.reset();

        startAhrsAngle = Robot.ahrs.getYaw();

        inSetup = true;
    }

    protected void execute() {
        setShooterAngle.setAngle(shooterSetpoint);

        if (inSetup) {
            SmartDashboard.putString("Status", "Auto setup");
            if (setShooterAngle.getError() < 1.5 && autoTime.get() > startStage) {
                inSetup = false;
                inDrive = true;
                driveTime.start();
            }
        } else if (inDrive) { // If ready
            SmartDashboard.putString("Status", "Auto drive");
            if (driveTime.get() < driveStage) {
                double errorAngle = Robot.ahrs.getYaw() - startAhrsAngle;

                SmartDashboard.putNumber("auto_error", errorAngle);

                // Robot.drivetrain.set(Math.pow(driveSpeed, errorAngle),
                // Math.pow(driveSpeed, -1 * errorAngle));
                // Fuck that and drive 100%
                Robot.drivetrain.set(DRIVE_SPEED, DRIVE_SPEED);
            } else {
                Robot.drivetrain.set(0, 0);
                isDone = true;
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
        LOW_BAR, SECOND, THIRD, FOURTH, FIFTH, SPY
    }
}
