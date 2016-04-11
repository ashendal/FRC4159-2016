package org.usfirst.frc.team4159.robot.subsystems;

import org.usfirst.frc.team4159.robot.RobotMap;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Lifter subsystem
 * 
 * @author Cole Scott
 */
public class Lifter extends Subsystem {

    private Victor lifterDart;

    private AnalogInput lifterEncoder;

    /**
     * Main Lifter constructor<br>
     * <br>
     * Setup all motors, encoders, and PID<br>
     */
    public Lifter() {
        lifterDart = new Victor(RobotMap.dartActuator);
        lifterEncoder = new AnalogInput(RobotMap.dartEncoder);
    }

    public void setRaw(double raw) {
        lifterDart.set(raw);
    }

    /**
     * Get current angle of lifter
     * 
     * @return Angle
     */
    public double getAngle() {
        SmartDashboard.putNumber("lifter value", lifterEncoder.getValue());
        return getAngleFromValue(lifterEncoder.getValue());
    }

    public void initDefaultCommand() {
    }

    double encoderYIntercept = 3165;
    double encoderSlope = 9.052;

    /**
     * Get encoder value from given angle<br>
     * <br>
     * Used for setting PID
     * 
     * @param degrees
     *            Degrees input
     * @return Encoder value output
     */
    public double getValueFromAngle(double degrees) {
        return encoderYIntercept + (encoderSlope * degrees);
    }

    /**
     * Get angle given encoder value<br>
     * <br>
     * Used for returning angle of lifter
     * 
     * @param value
     *            Encoder value
     * @return Angle of lifter
     */
    public double getAngleFromValue(double value) {
        // double angle = ((1/encoderSlope)*encoderYIntercept +
        // ((1/encoderSlope) * value));
        double angle = (value - encoderYIntercept) / encoderSlope;
        return angle;
    }

    public void zero() {
        encoderYIntercept = lifterEncoder.getValue();
    }
}
