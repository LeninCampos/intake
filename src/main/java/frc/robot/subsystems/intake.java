// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.trajectory.TrapezoidProfile.State;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ProfiledPIDSubsystem;
import frc.robot.Constants.armConstants;

public class intake extends ProfiledPIDSubsystem {
  /** Creates a new intake. */
  CANSparkMax intakeLeft = new CANSparkMax(2, MotorType.kBrushless);
  CANSparkMax intakeRight = new CANSparkMax(3, MotorType.kBrushless);

  CANSparkMax intakeMotor = new CANSparkMax(2, MotorType.kBrushless);

  DutyCycleEncoder dutyEncoder = new DutyCycleEncoder(0);

   private final ArmFeedforward m_feedforward = new ArmFeedforward(armConstants.kS, 
   armConstants.kG, 
   armConstants.kV, 
   armConstants.kA);


  public intake() {
    super(new ProfiledPIDController(
              armConstants.kP, 
              armConstants.ki, 
              armConstants.kD, 
            new TrapezoidProfile.Constraints(
              armConstants.kMaxVelocity, 
              armConstants.kMaxAcceleration)), 
            (0));

    dutyEncoder.setDistancePerRotation(360);

    dutyEncoder.setDutyCycleRange(getMeasurement(), getMeasurement());
   
    intakeLeft.setSmartCurrentLimit(20);
    intakeRight.setSmartCurrentLimit(20);

    intakeMotor.setSmartCurrentLimit(40);

    intakeRight.setInverted(false);
    intakeLeft.follow(intakeRight, false);

    intakeMotor.setInverted(false);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    super.periodic();
  }

  public Command take(double speed){
    return this.runOnce(() -> intakeMotor.set(speed));
  }

  @Override
  public void useOutput(double output, State setpoint) { 
    // Calculate the feedforward from the sepoint
    double feedforward = m_feedforward.calculate(setpoint.position, setpoint.velocity);
    // Add the feedforward to the PID output to get the motor output
    intakeLeft.setVoltage(output + feedforward);

  }

  @Override
  public double getMeasurement() {
    // TODO Auto-generated method stub
    return dutyEncoder.getDistance();
  }

  public Command goToPosition(double position){
    Command ejecutable = Commands.runOnce(
              () -> {
                this.setGoal(position);
                this.enable();
              }, this);
              return ejecutable;
  }

  public boolean IntakePosition(){
    if(this.getController().getGoal().position == armConstants.INTAKE_POSITION){
      return true;
    }else{
      return false;
    }
  }

  public boolean AmpPosition(){
    if(this.getController().getGoal().position == armConstants.AMP_POSITION){
    return true;
  }else{
    return false;
  }
  }

  public boolean ShooterPosition(){
    if(this.getController().getGoal().position == armConstants.SHOOTER_POSITION){
      return true;
    }else{
      return false;
    }
  }
}
