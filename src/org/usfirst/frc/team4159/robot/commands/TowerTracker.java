package org.usfirst.frc.team4159.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class TowerTracker extends Command {

    private double distance;
    private double centerX;
    private double centerY;
    private double cameraWidth;
    private double cameraHeight;

    private boolean isRunning;

    public TowerTracker() {
        this.setRunWhenDisabled(true);

        // Set to false to allow for checking if RPi is up
        SmartDashboard.putBoolean("RPiRunning", false);
        isRunning = false;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if (isRunning) {
            distance = SmartDashboard.getNumber("distance", -1);
            centerX = SmartDashboard.getNumber("center.x", -1);
            centerY = SmartDashboard.getNumber("center.x", -1);
            cameraWidth = SmartDashboard.getNumber("cameraWidth", -1);
            cameraHeight = SmartDashboard.getNumber("cameraHeight", -1);

        } else {
            if (SmartDashboard.getBoolean("RPiRunning"))
                isRunning = true;
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }

    public double getDistance() {
        return distance;
    }

    public double getCenterX() {
        return centerX;
    }

    public double getCenterY() {
        return centerY;
    }

    public double getCameraWidth() {
        return cameraWidth;
    }

    public double getCameraHeight() {
        return cameraHeight;
    }
}
