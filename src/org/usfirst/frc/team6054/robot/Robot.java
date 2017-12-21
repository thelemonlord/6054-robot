package org.usfirst.frc.team6054.robot;
import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.AnalogAccelerometer;
@SuppressWarnings("unused")
public class Robot extends IterativeRobot {
 CANTalon driveLeftOne = new CANTalon(0);
 CANTalon driveLeftTwo = new CANTalon(3);
 CANTalon driveRightOne = new CANTalon(1);
 CANTalon driveRightTwo = new CANTalon(2);
 Timer timer = new Timer();
 //RobotDrive myRobot = new RobotDrive(0, 1);
 Joystick xbox = new Joystick(0);    //xbox controller plugs into right usb port (left when viewed from back)
 Joystick x3d = new Joystick(1);     //x3d joystick plugs into left usb port (right when viewed from back)
 private ADXRS450_Gyro gyro = new ADXRS450_Gyro();
 //accel = new BuiltInAccelerometer();
 NetworkTable table;
 
 public void robotInit() {
  
 /*
 UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
 camera.setBrightness(0);
 camera.setResolution(320, 240);
 camera.setFPS(30);
 CameraServer.getInstance().addServer("cam", 1181);
 */
 //gyro.setSensitivity(.1); 
 //myRobot = new RobotDrive(0,1,2,3);
 gyro.reset();
 gyro.calibrate();
 table = NetworkTable.getTable("datatable");
 }
  
 public void teleopPeriodic() {
  //this code declares the variables
  //myRobot.arcadeDrive(xbox);
  double rightStickY = xbox.getRawAxis(5);
  double leftStickY = xbox.getRawAxis(1);
  double rightStickX = xbox.getRawAxis(4);
  double leftStickX = xbox.getRawAxis(0);
  boolean xButton = xbox.getRawButton(3);
  boolean yButton = xbox.getRawButton(4);
  double x3dX = x3d.getRawAxis(0);    //x3d refers to the Extreme 3D Pro Joystick
  double x3dY = x3d.getRawAxis(1);
  double rightDriveSpeed = rightStickY;
  double leftDriveSpeed = leftStickY;
  double angleBot = gyro.getAngle();
  double rad = 0;
  double deg = 0;
  int angleGyro = 0;
  int angleJS = 0;       //JS refers to joystick
  int diffJS = 0;
  int diffBot = 0;
  float power = 0;
  int distToAngle = 0;
  
  if(leftStickY > .075 || leftStickX > .075 || leftStickY < -.075 || leftStickX < -.075 ){
   rad = Math.atan2(leftStickX, leftStickY); //this code gets the x and y coords from the xbox controller
   deg = rad * (180 / Math.PI);     // and converts it to an angle
   deg = deg + 180;         //180 is added to compensate for the gyro angle starting at 0
   deg = 360 - deg;
   angleJS = (int) deg;
   SmartDashboard.putNumber("Angle", deg);
  }
  
  else{
   deg = 500;          //if the left stick is not out of the deadzone, send 500 as the angle
   SmartDashboard.putNumber("Angle", deg);
  }
 
  angleGyro = (int) (angleBot % 360); //this code makes sure angleGyro can't be above 360
  if(angleGyro < 0){
   angleGyro = angleGyro + 360; //this code makes sure angleGyro can't be negative
  }
  
  if(deg != 500){ //checks if the joystick is out of the deadzone
    if(leftStickY > .075 || leftStickX > .075 || leftStickY < -.075 || leftStickX < -.075 ) { //this sets a deadzone of .075 (may or may not be useful, lol)
     diffJS = 360 - angleJS;   //diffJS is the joystick's angle offset from 0
     diffBot = (int) (diffJS + angleBot); //diffbot is the difference between the bot's angle and the joystick angle
     
     if(diffBot > 360){   //this makes sure diffBot cannot be above 360
      diffBot = diffBot % 360;
     }
 
      distToAngle = (int) (angleBot - angleJS);   //distToAngle is the distance between the bot's angle and the desired angle
      distToAngle = Math.abs(distToAngle);  //this makes distToAngle an absolute value
      
     if(distToAngle < 40){
      power = distToAngle * 2;
      power = power / 100;
     }
     
     if(power > .6){
      power = (float) .6;
     }
     
     if(diffBot > 180){
      driveLeftOne.set(power);
      driveLeftTwo.set(power);
      driveRightOne.set(power);
      driveRightTwo.set(power);
     }
     if(diffBot < 180){
      driveLeftOne.set(-power);
      driveLeftTwo.set(-power);
      driveRightOne.set(-power);
      driveRightTwo.set(-power);
     }
     /*if(diffBot > angleBot + 5 && diffBot < angleBot - 5){   //broken anti-wiggle code
      driveLeftOne.set(0);
      driveLeftTwo.set(0);
      driveRightOne.set(0);
      driveRightTwo.set(0);
     }*/
    }
  }
  else{
   driveLeftOne.set(0);
   driveLeftTwo.set(0);
   driveRightOne.set(0);
   driveRightTwo.set(0);
  }
 
  if (xButton == true){
   gyro.reset();    //this code resets and calibrates the gyro
   gyro.calibrate();
  }
  if (yButton == true){
   gyro.reset();    //this code resets the gyro angle to 0
   diffBot = 0;
   diffJS = 0;
  }
  
  SmartDashboard.putNumber("deg", deg);    //diagnostics/for testing, not necessary
  SmartDashboard.putNumber("diffJS", diffJS);
  SmartDashboard.putNumber("diffBot", diffBot);
  SmartDashboard.putNumber("angleBot", angleBot);
  SmartDashboard.putNumber("angleJS", angleJS); 
  SmartDashboard.putNumber("rightStickY", rightStickY);
  SmartDashboard.putNumber("leftStickY", leftStickY);
  SmartDashboard.putNumber("rightStickX", rightStickX);
  SmartDashboard.putNumber("leftStickX", leftStickX);
  SmartDashboard.putNumber("Power", power);
  SmartDashboard.putNumber("distToAngle", distToAngle);
  table.putNumber("True gyro angle", angleBot);
  
  table.putNumber("Useful gyro angle", angleGyro); //necessary, keep this
 }
  
  /*
  //this code drives the right side of the robot
  if (rightStickY > 0.075 && rightStickY < .7 || rightStickY < -0.075 && rightStickY >-0.7)
  {
   driveRightOne.set(rightDriveSpeed);
   driveRightTwo.set(rightDriveSpeed);
  }
  else if (rightStickY > .7) {	//this sets the speed limit at .7
   driveRightOne.set(.7);
   driveRightTwo.set(.7);
  } 
  else if (rightStickY < -.7) {	//this sets the speed limit at .7
   driveRightOne.set(-.7);
   driveRightTwo.set(-.7);
  } 
  else
  {
   driveRightOne.set(0);
   driveRightTwo.set(0);
  }
   
  //this code drives the left side of the robot
  if (leftStickY > 0.075 && leftStickY < .7 || leftStickY < -0.075 && leftStickY >-0.7)
  {
   driveLeftOne.set(-leftDriveSpeed);
   driveLeftTwo.set(-leftDriveSpeed);
  }
   
  else if (leftStickY > .7) { //this sets the speed limit at .7
   driveLeftOne.set(-.7);
   driveLeftTwo.set(-.7);
  }
  else if (leftStickY < -.7) { //this sets the speed limit at .7
   driveLeftOne.set(.7);
   driveLeftTwo.set(.7);
  }
  else
  {
   driveLeftOne.set(0);
   driveLeftTwo.set(0);
  }
  
 }
*/
 public void autonomous() {
  gyro.reset();
  gyro.calibrate();
  }
  
 }