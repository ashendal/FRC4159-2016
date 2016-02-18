package org.usfirst.frc.team4159.robot.commands;

import org.usfirst.frc.team4159.robot.Robot;
import org.usfirst.frc.team4159.robot.subsystems.Shooter.TriggerPosition;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Shoot extends Command {

    private boolean doneShooting = false;
    
    public Shoot() {
        requires(Robot.shooter);
        SmartDashboard.putBoolean("Running Shoot", true);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        Robot.shooter.setTrigger(TriggerPosition.OPEN);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        Robot.shooter.setWheels(1.0, 1.0);
        
        Timer.delay(0.5);
        //if(Robot.shooter.atTargetSpeed())
        //{
            Timer.delay(0.5);
            Robot.shooter.setTrigger(TriggerPosition.CLOSED);
            Timer.delay(0.5);
            Robot.shooter.setWheels(0.0, 0.0);
            doneShooting = true;
        //} else {
        //    Robot.shooter.setWheels(1.0, 1.0);
        //}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return doneShooting;
    }

    // Called once after isFinished returns true
    protected void end() {
        SmartDashboard.putBoolean("Running Shoot", true);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
