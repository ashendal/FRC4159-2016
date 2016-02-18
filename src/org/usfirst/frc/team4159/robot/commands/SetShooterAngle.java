package org.usfirst.frc.team4159.robot.commands;

import org.usfirst.frc.team4159.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class SetShooterAngle extends Command {

    double kP;
    
    double setpoint;
    
    public SetShooterAngle(double s) {
        setpoint = s;
        
        kP = SmartDashboard.getNumber("Lifter.lifterPID.kP");
        requires(Robot.lifter);
    }
    
    public void setAngle(double s)
    {
        setpoint = s;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        double error = setpoint - Robot.lifter.getAngle();
        
        Robot.lifter.setRaw(error * kP);
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
