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

    private double phase1Time = 0.2;
    private double phase2Time = 2.0;
    private double phase3Time = 0.5;

    private boolean phase1 = false;
    private boolean phase2 = false;
    private boolean phase3 = false;

    private Timer time;

    private boolean doneShooting = false;

    public Shoot() {
        requires(Robot.shooter);

        time = new Timer();
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        SmartDashboard.putString("Status", "Shooting... ");
        time.reset();
        time.start();
        phase1 = true;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if (phase1) {
            SmartDashboard.putString("Status", "Shooting... phase1");
            Robot.drivetrain.set(0, 0);
            Robot.lifter.setRaw(0);
            Robot.shooter.setTrigger(TriggerPosition.OPEN);
            if (time.get() > phase1Time) {
                phase1 = false;
                phase2 = true;
                time.reset();
                time.start();
            }
        } else if (phase2) {
            SmartDashboard.putString("Status", "Shooting... phase2");
            Robot.shooter.setWheels(0.75, 0.75);
            if (time.get() > phase2Time) {
                phase2 = false;
                phase3 = true;
                time.reset();
                time.start();
            }
        } else if (phase3) {
            SmartDashboard.putString("Status", "Shooting... phase3");
            Robot.shooter.setTrigger(TriggerPosition.CLOSED);
            if (time.get() > phase3Time) {
                phase3 = false;
                Robot.shooter.setWheels(0.0, 0.0);
                doneShooting = true;
            }
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return doneShooting;
    }

    // Called once after isFinished returns true
    protected void end() {
        SmartDashboard.putString("Status", "Shooting... Done.");
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
