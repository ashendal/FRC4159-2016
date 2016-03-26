
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

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.SPI;
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

    private Command autonomousCommand;
    private SendableChooser chooser;

    public static TowerTracker towerTracker;

    private Command shoot;
    private SetShooterAngle setLifterAngle;
    private AutoAim autoAim;

    private AHRS ahrs;

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
        chooser.addDefault("Low Bar", new AutoCommand(AutoCommand.Defense.LOW_BAR));
        chooser.addDefault("Second", new AutoCommand(AutoCommand.Defense.SECOND));
        chooser.addDefault("Third", new AutoCommand(AutoCommand.Defense.THIRD));
        chooser.addDefault("Fourth", new AutoCommand(AutoCommand.Defense.FOURTH));
        chooser.addDefault("Fifth", new AutoCommand(AutoCommand.Defense.FIFTH));
        SmartDashboard.putData("Auto mode", chooser);

        try {
            ahrs = new AHRS(SPI.Port.kMXP);
        } catch (RuntimeException ex) {
            DriverStation.reportError("Error instantiating navX-MXP:  " + ex.getMessage(), true);
        }
    }

    private void setupSmartDashboard() {
        SmartDashboard.putNumber("lifterSetpoint", 30);

        SmartDashboard.putNumber("Lifter.lifterPID.kP", 0.1);

        SmartDashboard.putNumber("Drivetrain.leftPID.kP", 0.05);
        SmartDashboard.putNumber("Drivetrain.leftPID.kI", 0);
        SmartDashboard.putNumber("Drivetrain.leftPID.kD", 0);
        SmartDashboard.putNumber("Drivetrain.leftPID.kF", 0);

        SmartDashboard.putNumber("Drivetrain.rightPID.kP", 0.05);
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

        // schedule the autonomous command
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

        shoot = null;
        shoot = new Shoot();

        setLifterAngle.setP(SmartDashboard.getNumber("Lifter.lifterPID.kP"));
        setLifterAngle.start();
    }

    /**
     * Teleop periodic method
     * 
     * Get and act on operator input and run commands
     */
    public void teleopPeriodic() {
        if (oi.secondaryStick.getTrigger() && !shoot.isRunning()) {
            shoot = new Shoot();
            shoot.start();
        } /*
           * else if(oi.secondaryStick.getRawButton(4) && !autoAim.isRunning())
           * { autoAim = new AutoAim(); autoAim.start(); }
           */ else if (oi.secondaryStick.getRawButton(oi.SECONDARY_INTAKE))
            shooter.setWheels(0.3, -0.3); // Intake
        else if (oi.secondaryStick.getRawButton(oi.SECONDARY_SPIT_OUT))
            shooter.setWheels(-0.5, -0.5); // Spit out
        else if (!shoot.isRunning())
            shooter.setWheels(0.0, 0.0);

        if (oi.secondaryStick.getRawButton(oi.SECONDARY_TRIGGER_OPEN))
            shooter.setTrigger(TriggerPosition.OPEN);
        if (oi.secondaryStick.getRawButton(oi.SECONDARY_TRIGGER_CLOSE))
            shooter.setTrigger(TriggerPosition.CLOSED);

        drivetrain.set(oi.leftStick.getAxis(oi.LEFT_DRIVE_AXIS) * oi.LEFT_DRIVE_MULTIPLIER,
                oi.rightStick.getAxis(oi.RIGHT_DRIVE_AXIS) * oi.RIGHT_DRIVE_MULTIPLIER);
        if (oi.rightStick.getRawButton(oi.RIGHT_SHIFT_HIGH))
            drivetrain.setGear(Drivetrain.SpeedGear.HIGH);
        if (oi.rightStick.getRawButton(oi.RIGHT_SHIFT_LOW))
            drivetrain.setGear(Drivetrain.SpeedGear.LOW);

        setLifterAngle.setAngle(lifter.getAngle()
                + (oi.secondaryStick.getAxis(oi.SECONDARY_LIFTER_AXIS) * oi.SECONDARY_LIFTER_MULTIPLIER));

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
