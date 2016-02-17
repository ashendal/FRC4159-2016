package org.usfirst.frc.team4159.robot.subsystems;

import org.usfirst.frc.team4159.robot.RobotMap;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Lifter extends Subsystem {
    
    private static double JOYSTICK_MULT = 5.0;
    
    Victor lifterDart;
    
    Encoder lifterEncoder;
    
    PIDController lifterPID;
    
    public Lifter()
    {
        lifterDart = new Victor(RobotMap.dartActuator);
        lifterEncoder = new Encoder(RobotMap.dartEncoderA, RobotMap.dartEncoderB);
        lifterPID = new PIDController(SmartDashboard.getNumber("Lifter.lifterPID.kP"), SmartDashboard.getNumber("Lifter.lifterPID.kI"), SmartDashboard.getNumber("Lifter.lifterPID.kD"), lifterEncoder, lifterDart);
    }
    
    public void enable()
    {
        lifterPID.enable();
    }
    
    public void disable()
    {
        lifterPID.disable();
    }
    
    public void feedJoystick(Joystick joystick)
    {
        setAngle(getAngle() + (joystick.getY() * JOYSTICK_MULT));
    }
    
    public void setAngle(double angle)
    {
        lifterPID.setSetpoint(getValueFromAngle(angle));
    }
    
    public double getAngle()
    {
        return getAngleFromValue(lifterEncoder.get());
    }
    
    public void initDefaultCommand() {
    }
    
    private double getValueFromAngle(double degrees)
    {
        return 2965 + (-11.33 * -1 *degrees);
    }
    
    private double getAngleFromValue(double value)
    {
        return -1*(261.6 + (-0.08824 * value));

    }
}

