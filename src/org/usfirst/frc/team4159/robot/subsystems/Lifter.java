package org.usfirst.frc.team4159.robot.subsystems;

import org.usfirst.frc.team4159.robot.RobotMap;

import edu.wpi.first.wpilibj.AnalogInput;
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
    
    private AnalogInput lifterEncoder;
        
    /**
     * Main Lifter constructor<br>
     * <br>
     * Setup all motors, encoders, and PID<br>
     */
    public Lifter()
    {
        lifterDart = new Victor(RobotMap.dartActuator);
        lifterEncoder = new AnalogInput(RobotMap.dartEncoder);
    }
    
    public void setRaw(double raw)
    {
        lifterDart.set(raw);
    }
    
    /**
     * Get current angle of lifter
     * 
     * @return Angle
     */
    public double getAngle()
    {
        SmartDashboard.putNumber("lifter value", lifterEncoder.getValue());
        return getAngleFromValue(lifterEncoder.getValue());
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
        return 2896 + (9.052 *degrees);
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
        return (-316.3 + (0.1094 * value));

    }
}

