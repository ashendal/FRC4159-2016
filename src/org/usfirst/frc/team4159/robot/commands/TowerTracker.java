package org.usfirst.frc.team4159.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class TowerTracker extends Command {

    private double distance;
    private double sideAngle;

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
            distance = SmartDashboard.getNumber("distance");
            sideAngle = SmartDashboard.getNumber("sideAngle");
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
}
