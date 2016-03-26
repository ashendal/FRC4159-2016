package org.usfirst.frc.team4159.robot;

import edu.wpi.first.wpilibj.Joystick;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
    Joystick leftStick = new Joystick(0);
    Joystick rightStick = new Joystick(1);
    Joystick secondaryStick = new Joystick(2);

    public final Joystick.AxisType LEFT_DRIVE_AXIS = Joystick.AxisType.kY;
    public final double LEFT_DRIVE_MULTIPLIER = 15.0;

    public final Joystick.AxisType RIGHT_DRIVE_AXIS = Joystick.AxisType.kY;
    public final double RIGHT_DRIVE_MULTIPLIER = 15.0;
    public final int RIGHT_SHIFT_HIGH = 3;
    public final int RIGHT_SHIFT_LOW = 2;

    public final Joystick.AxisType SECONDARY_LIFTER_AXIS = Joystick.AxisType.kY;
    public final double SECONDARY_LIFTER_MULTIPLIER = 10.0;
    public final int SECONDARY_INTAKE = 6;
    public final int SECONDARY_SPIT_OUT = 6;
    public final int SECONDARY_TRIGGER_OPEN = 2;
    public final int SECONDARY_TRIGGER_CLOSE = 3;
}
