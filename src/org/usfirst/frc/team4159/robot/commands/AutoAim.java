package org.usfirst.frc.team4159.robot.commands;

import org.usfirst.frc.team4159.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class AutoAim extends Command {

    private static final int MAX_TIME = 3; // Max time in seconds
    private static final int ROTATE_MULTIPLIER = 15;
    private static final int LIFTER_MULTIPLIER = 10;

    private boolean readyToShoot = false;
    private boolean isShooting = false;
    private boolean hasShot = false;
    private double targetLifterAngle = 0;

    private SetShooterAngle setShooterAngle;
    private Shoot shoot;

    private Timer time;

    public AutoAim() {
        requires(Robot.drivetrain);
        setShooterAngle = new SetShooterAngle(Robot.lifter.getAngle());
        shoot = new Shoot();
        time = new Timer();
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        time.reset();
        time.start();
        setShooterAngle.setAngle(Robot.lifter.getAngle());
        setShooterAngle.start();
        SmartDashboard.putBoolean("runningAutoAim", true);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if (time.get() > MAX_TIME) {
            hasShot = true;
            return;
        }

        if (isShooting) {
            if (shoot.isRunning())
                return;
            hasShot = true;
            return;
        }

        double rotation = (SmartDashboard.getNumber("center.x") / SmartDashboard.getNumber("cameraWidth")) - 0.5;
        SmartDashboard.putNumber("rotation", rotation);

        targetLifterAngle = Robot.lifter.getAngle()
                + ((SmartDashboard.getNumber("center.x") / SmartDashboard.getNumber("cameraWidth")) - 0.5)
                        * LIFTER_MULTIPLIER;

        Robot.drivetrain.set(rotation * -ROTATE_MULTIPLIER, rotation * ROTATE_MULTIPLIER);

        setShooterAngle.setAngle(targetLifterAngle);

        if (Math.abs(rotation) <= 0.04 && setShooterAngle.getError() < 1.5) {
            readyToShoot = true;
        }

        if (readyToShoot) {
            shoot.start();
            isShooting = true;
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return hasShot;
    }

    // Called once after isFinished returns true
    protected void end() {
        Robot.drivetrain.set(0, 0);
        setShooterAngle.cancel();

        time.stop();
        SmartDashboard.putBoolean("runningAutoAim", false);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
