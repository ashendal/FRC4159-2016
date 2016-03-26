package org.usfirst.frc.team4159.robot.commands;

import org.usfirst.frc.team4159.robot.Robot;
import org.usfirst.frc.team4159.robot.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutoCommand extends Command {
    
    private Timer autoTime = new Timer();
    
    private boolean isDone = false;

    public AutoCommand() {
        requires(Robot.drivetrain);
        requires(Robot.shooter);
    }

    protected void initialize() {
        autoTime.reset();
        autoTime.start();
        
        Robot.drivetrain.enable();
        Robot.drivetrain.setGear(Drivetrain.SpeedGear.HIGH);
    }

    protected void execute() {
        if(autoTime.get() < 5)
        {
            Robot.drivetrain.set(8, 8);
        } else {
            Robot.drivetrain.set(0, 0);
            isDone = true;
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isDone;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        Robot.drivetrain.set(0, 0);
    }
}
