
package org.usfirst.frc.team4159.robot;

import org.usfirst.frc.team4159.robot.commands.AutoAim;
import org.usfirst.frc.team4159.robot.commands.AutoCommand;
import org.usfirst.frc.team4159.robot.commands.Intake;
import org.usfirst.frc.team4159.robot.commands.SetShooterAngle;
import org.usfirst.frc.team4159.robot.commands.Shoot;
import org.usfirst.frc.team4159.robot.commands.TowerTracker;
import org.usfirst.frc.team4159.robot.subsystems.Drivetrain;
import org.usfirst.frc.team4159.robot.subsystems.Lifter;
import org.usfirst.frc.team4159.robot.subsystems.Shooter;
import org.usfirst.frc.team4159.robot.subsystems.Shooter.TriggerPosition;

import com.kauailabs.navx.frc.AHRS;
import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ImageType;
import com.ni.vision.NIVision.Overlay;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.USBCamera;

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
    private Intake intake;

    public static AHRS ahrs;

    private Relay flashlight;

    /**
     * Main robot initialization method
     */
    public void robotInit() {
        flashlight = new Relay(0);

        setupSmartDashboard();

        sendFeed();

        oi = new OI();

        shooter = new Shooter();
        lifter = new Lifter();
        drivetrain = new Drivetrain();

        towerTracker = new TowerTracker();
        towerTracker.start();

        setLifterAngle = new SetShooterAngle(45);
        autoAim = new AutoAim();
        intake = new Intake();

        chooser = new SendableChooser();
        chooser.addDefault("Passive", new AutoCommand(AutoCommand.Defense.PASSIVE));
        chooser.addObject("Low Bar", new AutoCommand(AutoCommand.Defense.LOW_BAR));
        chooser.addObject("Spy", new AutoCommand(AutoCommand.Defense.SPY));
        SmartDashboard.putData("Auto mode", chooser);

        try {
            ahrs = new AHRS(SPI.Port.kMXP);
        } catch (RuntimeException ex) {
            DriverStation.reportError("Error instantiating navX-MXP:  " + ex.getMessage(), true);
        }
    }

    private void setupSmartDashboard() {
        SmartDashboard.putNumber("lifterSetpoint", 0);

        SmartDashboard.putNumber("Lifter.lifterPID.kP", 0.1);

        SmartDashboard.putNumber("Drivetrain.leftPID.kP", 0.2);
        SmartDashboard.putNumber("Drivetrain.leftPID.kI", 0);
        SmartDashboard.putNumber("Drivetrain.leftPID.kD", 0);
        SmartDashboard.putNumber("Drivetrain.leftPID.kF", 0); // TODO: Change to
                                                              // real values
                                                              // when pits open
                                                              // thursday

        SmartDashboard.putNumber("Drivetrain.rightPID.kP", 0.2);
        SmartDashboard.putNumber("Drivetrain.rightPID.kI", 0);
        SmartDashboard.putNumber("Drivetrain.rightPID.kD", 0);
        SmartDashboard.putNumber("Drivetrain.rightPID.kF", 0);

        SmartDashboard.putNumber("Set angle", 48.5);
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
        sendFeed();
        Scheduler.getInstance().run();
    }

    /**
     * Autonomous initialization function
     * 
     * Read autonomous mode from SmartDashboard and run that command
     */
    public void autonomousInit() {
        autonomousCommand = (Command) chooser.getSelected();

        flashlight.set(Value.kOff);

        // schedule the autonomous command
        if (autonomousCommand != null)
            autonomousCommand.start();
    }

    /**
     * Autonomous periodic method
     */
    public void autonomousPeriodic() {
        sendFeed();
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

    public static Victor stabbyStabby = new Victor(7);
    public static DigitalInput stabbyUpperLimit = new DigitalInput(6);

    /**
     * Teleop periodic method
     * 
     * Get and act on operator input and run commands
     */
    public void teleopPeriodic() {
        sendFeed();
        if (shoot.isRunning() || autoAim.isRunning() || intake.isRunning()) { // If
                                                                              // stuff
                                                                              // is
                                                                              // running
                                                                              // then
                                                                              // don't
                                                                              // interrupt
        } else if (oi.secondaryStick.getRawButton(oi.SECONDARY_SHOOT)
                /* && oi.rightStick.getRawButton(oi.SECONDARY_SHOOT) */ && !shoot.isRunning()) {
            shoot = new Shoot();
            shoot.start();
        } else if (oi.secondaryStick.getRawButton(oi.SECONDARY_AUTOAIM) && !autoAim.isRunning()) {
            // autoAim = new AutoAim();
            // autoAim.start();
        } else if (oi.secondaryStick.getRawButton(oi.SECONDARY_SPIT_OUT))
            shooter.setWheels(-0.5, -0.5);
        else if (oi.secondaryStick.getRawButton(oi.SECONDARY_INTAKE) && !intake.isRunning()) {
            // intake = new Intake();
            // intake.start();
            shooter.setWheels(0.3, -0.3);
        } else if (!shoot.isRunning())
            shooter.setWheels(0.0, 0.0);

        if (stabbyUpperLimit.get() && oi.leftStick.getRawButton(3))
            stabbyStabby.set(0.75);
        else if (oi.leftStick.getRawButton(2))
            stabbyStabby.set(-0.75);
        else
            stabbyStabby.set(0);

        // if(!oi.secondaryStick.getRawButton(oi.SECONDARY_INTAKE))
        // intake.cancel();

        SmartDashboard.putBoolean("switch", RobotMap.ballSwitch.get());

        if (oi.secondaryStick.getRawButton(oi.SECONDARY_LIGHT)) {
            flashlight.set(Value.kOn);
        } else {
            flashlight.set(Value.kOff);
        }

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

        if (oi.secondaryStick.getRawButton(11)) {
            setLifterAngle.cancel();
            Robot.lifter.setRaw(oi.secondaryStick.getY());
        } else {
            setLifterAngle.start();
            if (oi.leftStick.getRawButton(1)) {
                setLifterAngle.setAngle(7.78);
            } else if (oi.secondaryStick.getRawButton(oi.SECONDARY_SET)) {
                setLifterAngle.setAngle(SmartDashboard.getNumber("Set angle"));
            } else
                setLifterAngle.setAngle(lifter.getAngle()
                        + (oi.secondaryStick.getAxis(oi.SECONDARY_LIFTER_AXIS) * oi.SECONDARY_LIFTER_MULTIPLIER));
        }

        SmartDashboard.putNumber("lifterAngle", lifter.getAngle());

        Scheduler.getInstance().run();
    }

    /**
     * Test initialization method
     * 
     * Setup subsystems and SmartDashboard for testing
     */
    public void testInit() {
        setLifterAngle.setP(SmartDashboard.getNumber("Lifter.lifterPID.kP"));
        setLifterAngle.start();
    }

    /**
     * Test periodic method
     * 
     * Teleop periodic plus writing to SmartDashboard
     */
    public void testPeriodic() {
        LiveWindow.run();

        drivetrain.setGear(Drivetrain.SpeedGear.LOW);

        setLifterAngle.setAngle(62.417);
        if (stabbyUpperLimit.get())
            stabbyStabby.set(0.5);
        else
            stabbyStabby.set(0);
    }

    USBCamera usbCam = new USBCamera("cam0");
    CameraServer cs = CameraServer.getInstance();

    private void sendFeed() {
        /*
         * usbCam.openCamera(); Image img =
         * NIVision.imaqCreateImage(ImageType.IMAGE_RGB, 100);
         * NIVision.imaqSetImageSize(img, 640, 480); usbCam.getImage(img);
         * //NIVision.imaqOverlayLine(image, start, end, color, group);
         * cs.setImage(img); usbCam.closeCamera();
         */

        cs.startAutomaticCapture(usbCam);
    }
}
