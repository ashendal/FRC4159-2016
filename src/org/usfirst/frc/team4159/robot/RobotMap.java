package org.usfirst.frc.team4159.robot;

import edu.wpi.first.wpilibj.DigitalInput;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {

    // Drivetrain PWM ports
    public static int leftDriveMotor = 0;
    public static int rightDriveMotor = 4;

    // Drivetrain encoder ports
    public static int leftDriveEncoderA = 0;
    public static int leftDriveEncoderB = 1;
    public static int rightDriveEncoderA = 2;
    public static int rightDriveEncoderB = 3;

    // Drivetrain solenoid ports
    public static int leftShiftPistonForwards = 5;
    public static int leftShiftPistonReverse = 1;
    public static int rightShiftPistonForwards = 6;
    public static int rightShiftPistonReverse = 2;

    // Shooter PWM ports
    public static int topShooter = 2;
    public static int bottomShooter = 6;

    // Shooter encoder ports
    public static int topShooterEncoderA = 4;
    public static int topShooterEncoderB = 5;
    public static int bottomShooterEncoderA = 6;
    public static int bottomShooterEncoderB = 7;

    // Shooter solenoid ports
    public static int shooterTriggerForwards = 0;
    public static int shooterTriggerReverse = 4;

    // lifter PWM ports
    public static int dartActuator = 3;
    public static int dartEncoder = 0;

    public static DigitalInput zeroSwitch = new DigitalInput(7);
    public static DigitalInput topSwitch = new DigitalInput(8);
    public static DigitalInput ballSwitch = new DigitalInput(9);
}
