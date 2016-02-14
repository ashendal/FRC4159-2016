package org.usfirst.frc.team4159.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
    
    // Drivetrain PWM ports:
    public static int leftDriveMotor = 0;
    public static int rightDriveMotor = 0;
    
    //Drivetrain encoder ports:
    public static int leftDriveEncoderA = 0;
    public static int leftDriveEncoderB = 0;
    public static int rightDriveEncoderA = 0;
    public static int rightDriveEncoderB = 0;
    
    // Drivetrain solenoid ports:
    public static int leftShiftPistonForwards = 0;
    public static int leftShiftPistonReverse = 0;
    public static int rightShiftPistonForwards = 0;
    public static int rightShiftPistonReverse = 0;
    
    // Shooter PWM ports:
    public static int shooter1 = 0;
    public static int shooter2 = 0;
    public static int dartActuator = 0;
}
