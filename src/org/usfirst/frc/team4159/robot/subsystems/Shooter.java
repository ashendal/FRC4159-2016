package org.usfirst.frc.team4159.robot.subsystems;

import org.usfirst.frc.team4159.robot.RobotMap;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Shooter extends Subsystem {

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
    
    protected void initDefaultCommand() {
        
    }
    
    private enum TriggerPositions {
        
    }
}
