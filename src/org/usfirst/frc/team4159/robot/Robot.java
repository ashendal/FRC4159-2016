
package org.usfirst.frc.team4159.robot;

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

    Command autonomousCommand;
    SendableChooser chooser;

    /**
     * Main robot initialization method
     */
    public void robotInit() {
		oi = new OI();
        chooser = new SendableChooser();
        chooser.addDefault("Default Auto", null);
//        chooser.addObject("My Auto", new MyAutoCommand());
        SmartDashboard.putData("Auto mode", chooser);
    }
	
	/**
     * Robot disable method
     * 
     * Used to reset subsystems and values
     */
    public void disabledInit(){

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
        if (autonomousCommand != null) autonomousCommand.start();
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
        if (autonomousCommand != null) autonomousCommand.cancel();
    }

    /**
     * Teleop periodic method
     * 
     * Get and act on operator input and run commands
     */
    public void teleopPeriodic() {
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
