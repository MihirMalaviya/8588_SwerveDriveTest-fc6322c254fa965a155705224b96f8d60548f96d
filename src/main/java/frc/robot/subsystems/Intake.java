// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import static edu.wpi.first.units.MutableMeasure.mutable;
import static edu.wpi.first.units.Units.Meters;
import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.Volts;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.units.Distance;
import edu.wpi.first.units.Measure;
import edu.wpi.first.units.MutableMeasure;
import edu.wpi.first.units.Velocity;
import edu.wpi.first.units.Voltage;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
// import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkLowLevel;

public class Intake extends SubsystemBase {
  // CAN IDs
  public static final int kIntakeCanId = 20;

  // MEASUREMENTS
  // public static final double kWheelDiameter = Units.inchesToMeters(4.0); // meters
  // public static final double kGearRatio = 1.0 / 12.0; // 12:1 gear ratio

  // ---
  private CANSparkMax m_intake;
  private RelativeEncoder m_intakeEncoder;

  // private final MutableMeasure<Voltage> m_appliedVoltage = mutable(Volts.of(0));
  // private final MutableMeasure<Distance> m_distance = mutable(Meters.of(0));
  // private final MutableMeasure<Velocity<Distance>> m_velocity = mutable(MetersPerSecond.of(0));

  // SysIdRoutine routine;

  public Intake() {
    this.m_intake = new CANSparkMax(kIntakeCanId, MotorType.kBrushless);

    // Factory reset, so we get the SPARK MAX to a known state before configuring them. Useful in case a SPARK MAX is swapped out.
    m_intake.restoreFactoryDefaults();

    m_intake.setInverted(true);

    m_intakeEncoder = m_intake.getEncoder();
    
    m_intake.setInverted(true);

    setCoast();
    m_intake.setSmartCurrentLimit(50);

    // Save the SPARK MAX configurations. If a SPARK MAX browns out during operation, it will maintain the above configurations.
    m_intake.burnFlash();

    setCoast();

    // // Creates a SysIdRoutine
    // routine = new SysIdRoutine(
    //   new SysIdRoutine.Config(),
    //   new SysIdRoutine.Mechanism(this::voltageIntake, 
    //       log -> {
    //       log.motor("intake")
    //           .voltage(
    //               m_appliedVoltage.mut_replace(
    //                 m_intake.get() * RobotController.getBatteryVoltage(), Volts))
    //           .linearPosition(m_distance.mut_replace(m_intake.getEncoder().getPosition(), Meters))
    //           .linearVelocity(
    //               m_velocity.mut_replace(m_intake.getEncoder().getVelocity(), MetersPerSecond));
    //       },
    //   this
    // ));
  }

  // private void voltageIntake(Measure<Voltage> volts){
  //   m_intake.setVoltage(volts.in(Volts));
  // }

  /** sets intake idlemode to brake */
  public void setBrake() {
    m_intake.setIdleMode(CANSparkMax.IdleMode.kBrake);
  }

  public void setCoast() {
    m_intake.setIdleMode(CANSparkMax.IdleMode.kCoast);
  }

  public double getCurrent() {
    return m_intake.getOutputCurrent();
  }

  public double getVelocity() {
    return m_intakeEncoder.getVelocity();
  }

  public void set(double speed) {
    SmartDashboard.putNumber("Intake Speed", speed);

    m_intake.set(speed);
  }
  
  @Override
  public void periodic() {
    SmartDashboard.putNumber("Intake Encoder Position", m_intakeEncoder.getPosition());
    SmartDashboard.putNumber("Intake Encoder Velocity", m_intakeEncoder.getVelocity());
    SmartDashboard.putNumber("Intake Temp", m_intake.getMotorTemperature());

    SmartDashboard.putNumber("Intake Current", getCurrent());
  } 
  
  // public Command sysIdQuasistatic(SysIdRoutine.Direction direction) {
  //   return routine.quasistatic(direction);
  // }

  // public Command sysIdDynamic(SysIdRoutine.Direction direction) {
  //   return routine.dynamic(direction);
  // }
}
