package org.usfirst.frc.team4159.robot.subsystems;

import org.usfirst.frc.team4159.robot.RobotMap;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Drivetrain subsystem
 * 
 * @author Cole Scott
 */
public class Drivetrain extends Subsystem {

    private Victor leftMotor;
    private Victor rightMotor;
    
    private Encoder leftEncoder;
    private Encoder rightEncoder;
    private PIDController leftPID;
    private PIDController rightPID;
    
    private DoubleSolenoid leftSolenoid;
    private DoubleSolenoid rightSolenoid;
    
    private SpeedGear currentGear;
    
    private static final double maxLowGearSpeed = 0; //TODO: Set real speeds 
    private static final double maxHighGearSpeed = 0; //TODO: Figure out what the hell this is for
    
    /**
     * Main drivetrain constructor<br>
     * <br>
     * Initializes all motors, solenoids, and PID controllers<br>
     * PID values read from SmartDashboard<br>
     */
    public Drivetrain()
    {
        // Setup motors with given port numbers
        leftMotor = new Victor(RobotMap.leftDriveMotor);
        rightMotor =  new Victor(RobotMap.rightDriveMotor);
        
        // Setup Solenoids
        leftSolenoid = new DoubleSolenoid(RobotMap.leftShiftPistonForwards, RobotMap.leftShiftPistonReverse);
        rightSolenoid = new DoubleSolenoid(RobotMap.rightShiftPistonForwards, RobotMap.rightShiftPistonReverse);
        
        // Setup encoders given ports
        leftEncoder = new Encoder(RobotMap.leftDriveEncoderA, RobotMap.leftDriveEncoderB);
        rightEncoder = new Encoder(RobotMap.rightDriveEncoderA, RobotMap.rightDriveEncoderB);
        
        // Setup revolutions per pulse on encoders
        leftEncoder.setDistancePerPulse(getRevolutionsPerPulse(EncoderType.S4));
        rightEncoder.setDistancePerPulse(getRevolutionsPerPulse(EncoderType.S4));
        
        // Setup PID
        leftPID = new PIDController(SmartDashboard.getNumber("Drivetrain.leftPID.kP"), SmartDashboard.getNumber("Drivetrain.leftPID.kI"), SmartDashboard.getNumber("Drivetrain.leftPID.kD"), SmartDashboard.getNumber("Drivetrain.leftPID.kF"), leftEncoder, leftMotor);
        rightPID = new PIDController(SmartDashboard.getNumber("Drivetrain.rightPID.kP"), SmartDashboard.getNumber("Drivetrain.rightPID.kI"), SmartDashboard.getNumber("Drivetrain.rightPID.kD"), SmartDashboard.getNumber("Drivetrain.rightPID.kF"), rightEncoder, rightMotor);
    
        // Set current gear
        setGear(SpeedGear.LOW);
    }
    
    /**
     * Enable subsystem<br>
     * To be called in teleopInit and autonomousInit in Robot class<br>
     */
    public void enable()
    {
        leftPID.enable();
        rightPID.enable();
    }
    
    /**
     * Enable subsystem and reset PID components<br>
     * To be called in disabledInit in Robot class<br>
     */
    public void disable()
    {
        leftPID.reset();
        rightPID.reset();
    }
    
    /**
     * Set motor values<br>
     * 
     * @param leftValue  Left motor setpoint. In RPS (revolutions / sec)
     * @param rightValue Right motor setpoint. In RPS (revolutions / sec)
     */
    public void set(double leftValue, double rightValue)
    {
        leftPID.setSetpoint(leftValue);
        rightPID.setSetpoint(rightValue);
    }
    
    /**
     * Set motor values from joysticks
     * 
     * @param leftStick  Left joystick
     * @param rightStick Right joystick
     */
    public void set(Joystick leftStick, Joystick rightStick)
    {
        leftPID.setSetpoint(leftStick.getY());
        rightPID.setSetpoint(rightStick.getY());
    }
    
    /**
     * Set current gear to low or high gear<br>
     * 
     * @param gear New gear value, LOW or HIGH
     */
    public void setGear(SpeedGear gear)
    {
        currentGear = gear;
        switch(gear)
        {
        case HIGH:
            leftSolenoid.set(Value.kForward);
            rightSolenoid.set(Value.kForward);
            break;
        case LOW:
            leftSolenoid.set(Value.kReverse);
            rightSolenoid.set(Value.kReverse);
        }
    }
    
    /**
     * Get current gear setting<br>
     * Not guaranteed to be finished shifting to gear yet.<br>
     * 
     * @return Current gear
     */
    public SpeedGear getGear()
    {
        return currentGear;
    }
    
    /**
     * Sets up default command<br>
     * <br>
     * Currently unused<br>
     */
    protected void initDefaultCommand() {
        
    }
    
    /**
     * Gear settings<br>
     * HIGH and LOW values<br>
     * <br>
     * Used to get and set current gear<br>
     */
    public enum SpeedGear { LOW, HIGH }

    /**
     * Encoder types<br>
     * <br>
     * Used for constants for encoder settings<br>
     */
    enum EncoderType {
        S4
    }
    
    /**
     * Get revolutions per pulse given encoder type used
     * 
     * @param encoderType Encoder being used to calculate rate
     * @return revolutions per pulse of encoder
     */
    private double getRevolutionsPerPulse(EncoderType encoderType)
    {
        switch(encoderType)
        {
        case S4:
            return 1 / 400; //TODO: Change to actual value
        }
        return -1;
    }
}
