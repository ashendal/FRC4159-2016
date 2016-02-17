package org.usfirst.frc.team4159.robot.subsystems;

import org.usfirst.frc.team4159.robot.RobotMap;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Lifter subsystem
 * 
 * @author Cole Scott
 */
public class Lifter extends Subsystem {
    
    private static double JOYSTICK_MULT = 5.0;
    
    private Victor lifterDart;
    
    private Encoder lifterEncoder;
    
    private PIDController lifterPID;
    
    /**
     * Main Lifter constructor<br>
     * <br>
     * Setup all motors, encoders, and PID<br>
     */
    public Lifter()
    {
        lifterDart = new Victor(RobotMap.dartActuator);
        lifterEncoder = new Encoder(RobotMap.dartEncoderA, RobotMap.dartEncoderB);
        lifterPID = new PIDController(SmartDashboard.getNumber("Lifter.lifterPID.kP"), SmartDashboard.getNumber("Lifter.lifterPID.kI"), SmartDashboard.getNumber("Lifter.lifterPID.kD"), lifterEncoder, lifterDart);
    }
    
    /**
     * Enable subsystem
     */
    public void enable()
    {
        lifterPID.enable();
    }
    
    /**
     * Disable subsystem
     */
    public void disable()
    {
        lifterPID.disable();
    }
    
    /**
     * Feed joystick value to use to move lifter
     * 
     * @param joystick Joystick for input
     */
    public void feedJoystick(Joystick joystick)
    {
        setAngle(getAngle() + (joystick.getY() * JOYSTICK_MULT));
    }
    
    /**
     * Set angle of lifter
     * 
     * @param angle Angle setpoint
     */
    public void setAngle(double angle)
    {
        lifterPID.setSetpoint(getValueFromAngle(angle));
    }
    
    /**
     * Get current angle of lifter
     * 
     * @return Angle
     */
    public double getAngle()
    {
        return getAngleFromValue(lifterEncoder.get());
    }
    
    public void initDefaultCommand() {
    }
    
    /**
     * Get encoder value from given angle<br>
     * <br>
     * Used for setting PID 
     * 
     * @param degrees Degrees input
     * @return Encoder value output
     */
    private double getValueFromAngle(double degrees)
    {
        return 2965 + (-11.33 * -1 *degrees);
    }
    
    /**
     * Get angle given encoder value<br>
     * <br>
     * Used for returning angle of lifter 
     * 
     * @param value Encoder value
     * @return Angle of lifter
     */
    private double getAngleFromValue(double value)
    {
        return -1*(261.6 + (-0.08824 * value));

    }
}

