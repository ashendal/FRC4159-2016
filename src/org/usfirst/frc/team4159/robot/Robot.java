
package org.usfirst.frc.team4159.robot;

import org.usfirst.frc.team4159.robot.commands.AutoAim;
import org.usfirst.frc.team4159.robot.commands.AutoCommand;
import org.usfirst.frc.team4159.robot.commands.SetShooterAngle;
import org.usfirst.frc.team4159.robot.commands.Shoot;
import org.usfirst.frc.team4159.robot.commands.TowerTracker;
import org.usfirst.frc.team4159.robot.subsystems.Drivetrain;
import org.usfirst.frc.team4159.robot.subsystems.Lifter;
import org.usfirst.frc.team4159.robot.subsystems.Shooter;
import org.usfirst.frc.team4159.robot.subsystems.Shooter.TriggerPosition;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
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
    
    public static TowerTracker towerTracker;
    
    Command currentCommand;
    SetShooterAngle setLifterAngle;
    AutoAim autoAim;

    /**
     * Main robot initialization method
     */
    public void robotInit() {
        setupSmartDashboard();
        
        oi = new OI();
        
        shooter = new Shooter();
        lifter = new Lifter();
        drivetrain = new Drivetrain();
        
        towerTracker = new TowerTracker();
        towerTracker.start();
        
        setLifterAngle = new SetShooterAngle(45);
        autoAim = new AutoAim();
        
        chooser = new SendableChooser();
        chooser.addDefault("Default Auto", new AutoCommand());
        // chooser.addObject("My Auto", new MyAutoCommand());
        SmartDashboard.putData("Auto mode", chooser);
    }
    
    private void setupSmartDashboard()
    {
        SmartDashboard.putNumber("lifterSetpoint", 30);
        
        SmartDashboard.putNumber("Lifter.lifterPID.kP", 0.1);
        
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
        
        currentCommand = new Shoot();
        
        setLifterAngle.setP(SmartDashboard.getNumber("Lifter.lifterPID.kP"));
        setLifterAngle.start();
    }

    DoubleSolenoid ds = new DoubleSolenoid(3,7); //TODO: fix this to lifter subsystem
    
    /**
     * Teleop periodic method
     * 
     * Get and act on operator input and run commands
     */
    public void teleopPeriodic() {
        if(oi.secondaryStick.getTrigger() && !currentCommand.isRunning())
        {
            currentCommand = new Shoot();
            currentCommand.start();
        } /*else if(oi.secondaryStick.getRawButton(4) && !autoAim.isRunning())
        {
            autoAim = new AutoAim();
            autoAim.start();
        }*/ else if(oi.secondaryStick.getRawButton(4)) // 5
            shooter.setWheels(0.3, -0.3);
        else if(oi.secondaryStick.getRawButton(5)) // Change back to 6 after Anya changes mind
            shooter.setWheels(-0.5, -0.5);
        else if(!currentCommand.isRunning())
            shooter.setWheels(0.0, 0.0);
        
        if(oi.secondaryStick.getRawButton(2))
            shooter.setTrigger(TriggerPosition.OPEN);
        if(oi.secondaryStick.getRawButton(3))
            shooter.setTrigger(TriggerPosition.CLOSED);
            
        
        
        drivetrain.set(oi.leftStick.getY() * 15, oi.rightStick.getY() * 15);
        if(oi.rightStick.getRawButton(3))
            drivetrain.setGear(Drivetrain.SpeedGear.HIGH);
        if(oi.rightStick.getRawButton(2))
            drivetrain.setGear(Drivetrain.SpeedGear.LOW);
        
        //lifter.setRaw(oi.secondaryStick.getY()); //Used for raw input/being lazy
        setLifterAngle.setAngle(lifter.getAngle() + (Math.pow(oi.secondaryStick.getY(), 2) * Math.signum(oi.secondaryStick.getY()) * 10));
        
        SmartDashboard.putNumber("lifterAngle", lifter.getAngle());
        
        ds.set(Value.kForward);
        
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
