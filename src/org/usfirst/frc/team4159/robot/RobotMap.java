package org.usfirst.frc.team4159.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
    
    //TODO: Set port values based on real robot wiring
    
    // Drivetrain PWM ports
    public static int leftDriveMotor = 0;
    public static int rightDriveMotor = 0;
    
    // Drivetrain encoder ports
    public static int leftDriveEncoderA = 0;
    public static int leftDriveEncoderB = 0;
    public static int rightDriveEncoderA = 0;
    public static int rightDriveEncoderB = 0;
    
    // Drivetrain solenoid ports
    public static int leftShiftPistonForwards = 0;
    public static int leftShiftPistonReverse = 0;
    public static int rightShiftPistonForwards = 0;
    public static int rightShiftPistonReverse = 0;
    
    // Shooter PWM ports
    public static int topShooter = 0;
    public static int bottomShooter = 0;
    
    // Shooter encoder ports
    public static int topShooterEncoderA = 0;
    public static int topShooterEncoderB = 0;
    public static int bottomShooterEncoderA = 0;
    public static int bottomShooterEncoderB = 0;
    
    // Shooter solenoid ports
    public static int shooterTriggerForwards = 0;
    public static int shooterTriggerReverse = 0;
    
    //lifter PWM ports
    public static int dartActuator = 0;
    public static int dartEncoderA = 0;
    public static int dartEncoderB = 0;
}
