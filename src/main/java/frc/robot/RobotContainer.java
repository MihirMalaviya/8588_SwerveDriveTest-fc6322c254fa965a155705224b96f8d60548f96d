// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.DefaultDrive;
import frc.robot.subsystems.Indexing;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.SwerveSubsystem;
import frc.robot.subsystems.Wrist;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  public static final SwerveSubsystem m_Swerb = new SwerveSubsystem();
  public static final Intake intake = new Intake();
  public static final Indexing indexing = new Indexing();
  public static final Shooter shooter = new Shooter();
  public static final Wrist wrist = new Wrist();

  // Replace with CommandPS4Controller or CommandJoystick if needed
  public static CommandXboxController driverController = new CommandXboxController(0);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the trigger bindings
    configureBindings();
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {
    // swerve drive
    // m_Swerb.setDefaultCommand(new DefaultDrive());

    // reset swerve gyro
    driverController.y()
    .onTrue(new InstantCommand(
      () -> m_Swerb.zeroYaw()));

    // negative moves note out of bot positive moves note into bot

    // intake
    driverController.a() 
    .whileTrue(new StartEndCommand(
      () -> {
        intake.set(1);
        indexing.set(1);
      }, 
      () -> {
        intake.set(0);
        indexing.set(0);
      },
      intake, indexing));

    // shoot
    driverController.b() 
    .whileTrue(new StartEndCommand(
      () -> {
        shooter.set(-1);
        indexing.set(1);
      },
      () -> {
        shooter.set(0);
        indexing.set(0);
      }, 
      shooter, indexing
      ));

    // wind up shoot
    driverController.x() 
    .whileTrue(new StartEndCommand(
      () -> {
        shooter.set(-1);
      },
      () -> {
        shooter.set(0);
      }, 
      shooter
      ));
    
    // // shooter intake
    // driverController.rightBumper() 
    // .whileTrue(new StartEndCommand(
    //   () -> {
    //     shooter.set(.4);
    //     indexing.set(-1);
    //   },
    //   () -> {
    //     shooter.set(0);
    //     indexing.set(0);
    //   }, 
    //   shooter, indexing
    //   ));
    
    // // stop all
    // driverController.leftBumper() 
    // .onTrue(new InstantCommand(
    //   () -> {
    //     intake.set(0); 
    //     indexing.set(0); 
    //     shooter.set(0);
    //   }, 
    //   intake, indexing, shooter
    //   ));

    driverController.leftBumper().onTrue(
      wrist.incrementUp()
    );

    driverController.rightBumper().onTrue(
      wrist.incrementDown()
    );
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // TODO IMPLEMENT AN AUTON COMMAND!!!
    return new InstantCommand(); // this is the equivalent of "do nothing"
  }
}
