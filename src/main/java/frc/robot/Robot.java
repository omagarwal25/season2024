package frc.robot;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;
import java.util.function.LongConsumer;
import java.util.function.LongSupplier;
import java.util.function.Supplier;

import com.revrobotics.CANSparkBase;

import edu.wpi.first.util.function.BooleanConsumer;
import edu.wpi.first.util.function.FloatConsumer;
import edu.wpi.first.util.function.FloatSupplier;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.shuffleboard.EventImportance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.auto.selector.AutoModeSelector;
import frc.robot.core.util.CTREConfigs;
import frc.robot.subsystems.AddressableLEDLights;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Prototypes;
import frc.robot.subsystems.Shamper;
import frc.robot.subsystems.Vision.PoseEstimator;
import frc.robot.subsystems.Vision.Vision;
// import frc.robot.subsystems.PrototypeSubsystem;
import frc.robot.util.SendableMotor;
import frc.robot.auto.AutoCommands;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  public static CTREConfigs ctreConfigs;
  private final Field2d m_field = new Field2d();
  private Prototypes prototypes;

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    // TODO put auto chooser here. make sure to use the one from
    // robot/auto/selector/AutoModeSelector.java
    ctreConfigs = new CTREConfigs();

    if(RobotMap.PrototypeMap.LIVE_WINDOW_ENABLED)
      enableLiveWindowInTest(true);
    var autoModeSelector = AutoModeSelector.getInstance();
    SmartDashboard.putData("Blue Autos", autoModeSelector.getChooser());
    OI.getInstance();
    SmartDashboard.putData("field", m_field);

    if(ExampleConfig.Subsystems.PROTOTYPE_ENABLED && RobotMap.PrototypeMap.LIVE_WINDOW_ENABLED)
      Prototypes.getInstance();
    Drivetrain.getInstance().zeroGyroYaw();
    AutoCommands.registerAutoCommands();
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();
    if(ExampleConfig.Subsystems.DRIVETRAIN_ENABLED) {
      // UNTESTED GLASS TELEMETRY CODE - MAY RESULT IN NULL POINTERS
      m_field.getObject("Odometry Pose").setPose(Drivetrain.getInstance().getPose());
      m_field.getObject("Vision Pose").setPose(Vision.getInstance().visionBotPose());
      m_field.getObject("PoseEstimate Pose").setPose(PoseEstimator.getInstance().getPosition());
    }
    if (Vision.getInstance().getNotePose2d() != null){
      m_field.getObject("Note Pose").setPose(Vision.getInstance().getNotePose2d());
    }
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    CommandScheduler.getInstance().cancelAll();

    var autonomousCommand = AutoModeSelector.getInstance().getChooser().getSelected();

      if(autonomousCommand != null){
        autonomousCommand.schedule();
      }
    //Drivetrain.getInstance().setGyroYaw(180);
  }
    

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {}

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {
    CommandScheduler.getInstance().cancelAll();
    OI.getInstance().registerCommands();
    //Drivetrain.getInstance().zeroGyroYaw();
    //Drivetrain.getInstance().setGyroYaw(0);
    //PoseEstimator.getInstance().resetPoseEstimate(Vision.getInstance().visionBotPose());
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    //AddressableLEDLights.getInstance().periodic();
  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}
