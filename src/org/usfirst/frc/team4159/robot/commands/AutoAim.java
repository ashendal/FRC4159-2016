package org.usfirst.frc.team4159.robot.commands;

import org.usfirst.frc.team4159.robot.Robot;
import org.usfirst.frc.team4159.robot.ShooterLookup;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class AutoAim extends Command {

    private boolean readyToShoot = false;
    private boolean isShooting = false;
    private boolean hasShot = false;
    private double targetLifterAngle = 0;
    
    private SetShooterAngle setShooterAngle;
    private Shoot shoot;
    
    public AutoAim() {
        requires(Robot.drivetrain);
        setShooterAngle = new SetShooterAngle(Robot.lifter.getAngle());
        shoot = new Shoot();
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        setShooterAngle.start();
        SmartDashboard.putBoolean("runningAutoAim", true);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if(isShooting)
        {
            if(shoot.isRunning())
                return;
            hasShot = true;
            return;
        }
        
        double rotation = (SmartDashboard.getNumber("center.x") / SmartDashboard.getNumber("cameraWidth")) - 0.5;
        SmartDashboard.putNumber("rotation", rotation);
        
        //if(Math.abs(rotation) < 0.1)
        //    rotation = Math.signum(rotation) * 0.1;
        
        //if(setShooterAngle.getError() < 1) //TODO: Idk fix it or not i dont really care
        
        
        //targetLifterAngle = ShooterLookup.getAngle(Robot.towerTracker.getDistance());
        
        Robot.drivetrain.set(rotation * -15, rotation * 15);
        
        //setShooterAngle.setAngle(targetLifterAngle);
        
        if(!(rotation > 0.04 || rotation < -0.04))
        {
            readyToShoot = true;
        }
        
        if(readyToShoot)
        {
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
        SmartDashboard.putBoolean("runningAutoAim", false);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
