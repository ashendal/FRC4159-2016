package org.usfirst.frc.team4159.robot.commands;

import org.usfirst.frc.team4159.robot.Robot;
import org.usfirst.frc.team4159.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Intake extends Command { // TODO: Finish class

    private SetShooterAngle setShooterAngle;
    private boolean done = false;

    public Intake() { // shooter.setWheels(0.4, -0.4);
        requires(Robot.lifter);
        requires(Robot.shooter);

        setShooterAngle = new SetShooterAngle(Robot.lifter.getAngle());
    }

    protected void initialize() {
        if (!RobotMap.ballSwitch.get())
            this.cancel();

        setShooterAngle.start();
        setShooterAngle.setAngle(0);

        SmartDashboard.putString("Status", "Intaking... ");
    }

    protected void execute() {
        if (Robot.oi.secondaryStick.getRawButton(Robot.oi.SECONDARY_INTAKE))
            Robot.shooter.setWheels(0.4, -0.4);
        else
            done = true;
    }

    protected boolean isFinished() {
        SmartDashboard.putBoolean("switch", RobotMap.ballSwitch.get());
        return /* RobotMap.ballSwitch.get() || */ done;
    }

    protected void end() {
        Robot.shooter.setWheels(0, 0);
        setShooterAngle.setAngle(10);
        SmartDashboard.putString("Status", "Intaking... Done.");
    }

    protected void interrupted() {
    }
}
