
package org.usfirst.frc.team4159.robot;

import org.usfirst.frc.team4159.robot.commands.SetShooterAngle;
import org.usfirst.frc.team4159.robot.commands.Shoot;
import org.usfirst.frc.team4159.robot.subsystems.Drivetrain;
import org.usfirst.frc.team4159.robot.subsystems.Lifter;
import org.usfirst.frc.team4159.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Main robot class
 */
public class Robot extends IterativeRobot {

    public static OI oi;
    
    public static Shooter shooter;
    public static Lifter lifter;
    public static Drivetrain drivetrain;

    Command autonomousCommand;
    SendableChooser chooser;
    ShooterLookup aiming;
    
    Command currentCommand;
    SetShooterAngle setLifterAngle;

    /**
     * Main robot initialization method
     */
    public void robotInit() {
        setupSmartDashboard();
        
        oi = new OI();
        
        shooter = new Shooter();
        lifter = new Lifter();
        drivetrain = new Drivetrain();
        
        setLifterAngle = new SetShooterAngle(45);
        
        chooser = new SendableChooser();
        aiming = new ShooterLookup();
        chooser.addDefault("Default Auto", null);
        // chooser.addObject("My Auto", new MyAutoCommand());
        SmartDashboard.putData("Auto mode", chooser);
    }
    
    private void setupSmartDashboard()
    {
        SmartDashboard.putNumber("Lifter.lifterPID.kP", 0.001);
        
        SmartDashboard.putNumber("Drivetrain.leftPID.kP", 0.1);
        SmartDashboard.putNumber("Drivetrain.leftPID.kI", 0);
        SmartDashboard.putNumber("Drivetrain.leftPID.kD", 0);
        SmartDashboard.putNumber("Drivetrain.leftPID.kF", 0);
        
        SmartDashboard.putNumber("Drivetrain.rightPID.kP", 0.1);
        SmartDashboard.putNumber("Drivetrain.rightPID.kI", 0);
        SmartDashboard.putNumber("Drivetrain.rightPID.kD", 0);
        SmartDashboard.putNumber("Drivetrain.rightPID.kF", 0);
    }

    /**
     * Robot disable method
     * 
     * Used to reset subsystems and values
     */
    public void disabledInit() {
        drivetrain.disable();
        
        setLifterAngle.cancel();
        lifter.setRaw(0.0);
    }

    /**
     * Disabled periodic function
     * 
     * Nothing should run here...
     */
    public void disabledPeriodic() {
        Scheduler.getInstance().run();
    }

    /**
     * Autonomous initialization function
     * 
     * Read autonomous mode from SmartDashboard and run that command
     */
    public void autonomousInit() {
        autonomousCommand = (Command) chooser.getSelected();

        // schedule the autonomous command (example)
        if (autonomousCommand != null)
            autonomousCommand.start();
    }

    /**
     * Autonomous periodic method
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    /**
     * Teleop initialization function
     * 
     * Kill autonomous command and setup any subsystems for teleop mode
     */
    public void teleopInit() {
        // kill autonomous command at start of teleop
        if (autonomousCommand != null)
            autonomousCommand.cancel();
        drivetrain.enable();
        
        currentCommand = null;
        
        setLifterAngle.start();
    }

    /**
     * Teleop periodic method
     * 
     * Get and act on operator input and run commands
     */
    public void teleopPeriodic() {
        if(oi.secondaryStick.getTrigger())
        {
            currentCommand = new Shoot();
            currentCommand.start();
        }
        
        
        drivetrain.set(oi.leftStick.getY() * 15, oi.rightStick.getY() * 15);
        if(oi.rightStick.getRawButton(3))
            drivetrain.setGear(Drivetrain.SpeedGear.HIGH);
        if(oi.rightStick.getRawButton(2))
            drivetrain.setGear(Drivetrain.SpeedGear.LOW);
        
        //lifter.setRaw(oi.secondaryStick.getY()); //Used for raw input/being lazy
        setLifterAngle.setAngle(lifter.getAngle() + (oi.secondaryStick.getY() * 5));
        
        SmartDashboard.putNumber("lifterAngle", lifter.getAngle());
        
        Scheduler.getInstance().run();
    }

    /**
     * Test initialization method
     * 
     * Setup subsystems and SmartDashboard for testing
     */
    public void testInit() {

    }

    /**
     * Test periodic method
     * 
     * Teleop periodic plus writing to SmartDashboard
     */
    public void testPeriodic() {
        LiveWindow.run();
    }
}
