package org.usfirst.frc.team4159.robot.subsystems;

import org.usfirst.frc.team4159.robot.RobotMap;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Shooter extends Subsystem {

    private static int TARGET_SPEED = 5000;
    
    private Victor topWheelMotor;
    private Victor bottomWheelMotor;
    
    private Encoder topWheelEncoder;
    private Encoder bottomWheelEncoder;
    
    private DoubleSolenoid triggerActuator;
    
    public Shooter()
    {
        // Setup motors
        topWheelMotor = new Victor(RobotMap.topShooter);
        bottomWheelMotor = new Victor(RobotMap.bottomShooter);
        
        // Setup encoders
        topWheelEncoder = new Encoder(RobotMap.topShooterEncoderA, RobotMap.topShooterEncoderB);
        bottomWheelEncoder = new Encoder(RobotMap.bottomShooterEncoderA, RobotMap.bottomShooterEncoderB);
        
        // Setup solenoid
        triggerActuator = new DoubleSolenoid(RobotMap.shooterTriggerForwards, RobotMap.shooterTriggerReverse);
    }
    
    public boolean atTargetSpeed()
    {
        return getTopWheelSpeed() >= TARGET_SPEED && getBottomWheelSpeed() >= TARGET_SPEED;
    }
    
    public double getTopWheelSpeed()
    {
        return topWheelEncoder.get();
    }
    
    public double getBottomWheelSpeed()
    {
        return bottomWheelEncoder.get();
    }
    
    public void setTopWheel(double value)
    {
        topWheelMotor.set(value);
    }
    
    public void setBottomWheel(double value)
    {
        bottomWheelMotor.set(value);
    }
    
    public void setWheels(double leftValue, double rightValue)
    {
        topWheelMotor.set(leftValue);
        bottomWheelMotor.set(rightValue);
    }
    
    public void setTrigger(TriggerPosition pos)
    {
        switch(pos)
        {
        case OPEN:
            triggerActuator.set(Value.kReverse);
            break;
        case CLOSED:
            triggerActuator.set(Value.kReverse);
            break;
        }
    }
    
    protected void initDefaultCommand() {
        
    }
    
    public enum TriggerPosition {
        OPEN, CLOSED
    }
}
