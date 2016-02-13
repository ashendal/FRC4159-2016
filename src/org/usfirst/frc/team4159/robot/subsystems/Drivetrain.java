package org.usfirst.frc.team4159.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
    private static final double maxHighGearSpeed = 0;
    
    public Drivetrain(int leftMotorPort, int rightMotorPort, int leftSolenoidPortA, int leftSolenoidPortB, int rightSolenoidPortA, int rightSolenoidPortB, int leftEncoderPortA, int leftEncoderPortB, int rightEncoderPortA, int rightEncoderPortB, EncoderType leftEncoderType, EncoderType rightEncoderType)
    {
        // Setup motors with given port numbers
        leftMotor = new Victor(leftMotorPort);
        rightMotor =  new Victor(rightMotorPort);
        
        // Setup Solenoids
        leftSolenoid = new DoubleSolenoid(leftSolenoidPortA, leftSolenoidPortB);
        rightSolenoid = new DoubleSolenoid(rightSolenoidPortA, rightSolenoidPortB);
        
        // Setup encoders given ports
        leftEncoder = new Encoder(leftEncoderPortA, leftEncoderPortB);
        rightEncoder = new Encoder(rightEncoderPortA, rightEncoderPortB);
        
        // Setup revolutions per pulse on encoders
        leftEncoder.setDistancePerPulse(getRevolutionsPerPulse(leftEncoderType));
        rightEncoder.setDistancePerPulse(getRevolutionsPerPulse(rightEncoderType));
        
        // Setup PID
        leftPID = new PIDController(SmartDashboard.getNumber("Drivetrain.leftPID.kP"), SmartDashboard.getNumber("Drivetrain.leftPID.kI"), SmartDashboard.getNumber("Drivetrain.leftPID.kD"), SmartDashboard.getNumber("Drivetrain.leftPID.kF"), leftEncoder, leftMotor);
        rightPID = new PIDController(SmartDashboard.getNumber("Drivetrain.rightPID.kP"), SmartDashboard.getNumber("Drivetrain.rightPID.kI"), SmartDashboard.getNumber("Drivetrain.rightPID.kD"), SmartDashboard.getNumber("Drivetrain.rightPID.kF"), rightEncoder, rightMotor);
    
        // Set current gear
        setGear(SpeedGear.LOW);
    }
    
    public void enable()
    {
        leftPID.enable();
        rightPID.enable();
    }
    
    public void disable()
    {
        leftPID.disable();
        rightPID.disable();
    }
    
    public void set(double leftValue, double rightValue)
    {
        leftPID.setSetpoint(leftValue);
        rightPID.setSetpoint(rightValue);
    }
    
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
    
    public SpeedGear getGear()
    {
        return currentGear;
    }
    
    
    protected void initDefaultCommand() {
        
    }
    
    enum SpeedGear { LOW, HIGH }

    enum EncoderType {
        S4
    }
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
