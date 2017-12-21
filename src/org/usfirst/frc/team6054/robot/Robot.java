package org.usfirst.frc.team6054.robot;
import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

@SuppressWarnings("unused")
public class Robot extends IterativeRobot {
    CANTalon driveLeftOne = new CANTalon(0);
    CANTalon driveLeftTwo = new CANTalon(3);
    CANTalon driveRightOne = new CANTalon(1);
    CANTalon driveRightTwo = new CANTalon(2);
    Joystick xbox = new Joystick(0);    //xbox controller plugs into right usb port (left when viewed from back)
    Joystick x3d = new Joystick(1);     //x3d joystick plugs into left usb port (right when viewed from back)
     NetworkTable table;
 
 public void robotInit() {
    table = NetworkTable.getTable("datatable");
 }
  
 public void teleopPeriodic() {
  //this code declares the variables
  double rightStickY = xbox.getRawAxis(5);
  double leftStickY = xbox.getRawAxis(1);
  double rightStickX = xbox.getRawAxis(4);
  double leftStickX = xbox.getRawAxis(0);
  double rightDriveSpeed = rightStickY;
  double leftDriveSpeed = leftStickY;

  //this code drives the right side of the robot
  if (rightStickY > 0.075 && rightStickY < 0.75 || rightStickY < -0.075 && rightStickY >-0.75){
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
  if (leftStickY > 0.075 && leftStickY < .75 || leftStickY < -0.075 && leftStickY >-0.75){
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
