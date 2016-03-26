package org.usfirst.frc.team4159.robot.commands;

import org.usfirst.frc.team4159.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class SetShooterAngle extends Command {

    private static double TOP_ANGLE = 75;
    private static double BOTTOM_ANGLE = -5;

    private double kP;

    private double setpoint;

    private double error = 10000;

    public SetShooterAngle(double s) {
        setpoint = s;

        kP = SmartDashboard.getNumber("Lifter.lifterPID.kP");
        requires(Robot.lifter);
    }

    public void setP(double p) {
        kP = p;
    }

    public void setAngle(double s) {
        setpoint = s > TOP_ANGLE ? TOP_ANGLE : s < BOTTOM_ANGLE ? BOTTOM_ANGLE : s;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        error = setpoint - Robot.lifter.getAngle();
        SmartDashboard.putNumber("lifterPID error", error);
        Robot.lifter.setRaw(error * kP);
    }

    public double getError() {
        return error;
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
}
