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
    private Defense defense;
    private boolean isDone = false;
    
    private double shooterSetpoint;
    private double startStage;
    private double driveStage;
    
    private Timer driveTime;
    
    private SetShooterAngle setShooterAngle;
    
    public AutoCommand(Defense d)
    {
        defense = d;
    }

    public AutoCommand() {
        requires(Robot.drivetrain);
        requires(Robot.shooter);
        setShooterAngle = new SetShooterAngle(Robot.lifter.getAngle());
    }

    protected void initialize() {
        driveTime.reset();
        
        autoTime.reset();
        autoTime.start();
        
        switch(defense) {
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
        }

        Robot.drivetrain.enable();
        Robot.drivetrain.setGear(Drivetrain.SpeedGear.HIGH);
        
        setShooterAngle.setAngle(Robot.lifter.getAngle());
        setShooterAngle.start();
    }

    protected void execute() {
        if(autoTime.get() < startStage) // In starting stage
        {
            setShooterAngle.setAngle(shooterSetpoint);
        } else if(setShooterAngle.getError() < 1.5){ // If ready
            if(driveTime.get() > 0)
                driveTime.start();
            if (driveTime.get() < driveStage) {
                Robot.drivetrain.set(8, 8);
            } else {
                Robot.drivetrain.set(0, 0);
                isDone = true;
            }
        } else {
            // Wait
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isDone;
    }

    // Called once after isFinished returns true
    protected void end() {
        setShooterAngle.cancel();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        Robot.drivetrain.set(0, 0);
    }
    
    public enum Defense {
        LOW_BAR, SECOND, THIRD, FOURTH, FIFTH
    }
}
