// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.Constants.armConstants;
import frc.robot.subsystems.intake;


public class RobotContainer {
  // The robot's subsystems and commands are defined here...

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public static Joystick joy = new Joystick(0);
  private final intake intake = new intake();
  public RobotContainer() {
    // Configure the trigger bindings
    configureBindings();
  }

  JoystickButton aButton = new JoystickButton(joy, 1);
  private void configureBindings() {
    
    new JoystickButton(joy, 3).onTrue(intake.goToPosition(armConstants.INTAKE_POSITION));
    new JoystickButton(joy, 2).onTrue(intake.goToPosition(armConstants.AMP_POSITION));
    new JoystickButton(joy, 4).onTrue(intake.goToPosition(armConstants.SHOOTER_POSITION));

    if(intake.AmpPosition() == true || intake.ShooterPosition() == true){
      aButton.onTrue(intake.take(armConstants.sVelocity));
    }
    if(intake.IntakePosition() == true){
      aButton.onTrue(intake.take(armConstants.iVelocity));
    }
  }

  public Command getAutonomousCommand() {
    return null;
  }
}
