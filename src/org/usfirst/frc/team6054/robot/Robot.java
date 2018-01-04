package org.usfirst.frc.team6054.robot;
import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.AnalogGyro;
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
		double rightTrigger = xbox.getRawAxis(3);
		boolean xButton = xbox.getRawButton(3);
		boolean yButton = xbox.getRawButton(4);
		double x3dX = x3d.getRawAxis(0);    //x3d refers to the Extreme 3D Pro Joystick
		double x3dY = x3d.getRawAxis(1);
		double rightDriveSpeed = rightStickY;
		double leftDriveSpeed = leftStickY;
		double gyroRaw = gyro.getAngle();
		double rad = 0;
		double deg = 0;
		double JSraw = 0;
		double gyroClean = 0;
		int a = 0;					//math term; raw joystick value minus the raw gyro value
		int b = 0;					//math term; if a is below 0 then 360 is added to it to make b
		int mul = 0;					//math term; multiplier derived if b is above or below 180 (-1 if above, 1 if below)
		double k = 0.015; 			//math term; k is a constant to help control and limit the robot speed
		int c = 0;					//math term
		float d = 0;			//initial left rotary power for left motors
		float e = 0;				//initial right rotary power for right motors
		float f = 0;					//math term
		float g = 0;					//math term
		double throttle = rightTrigger;			//needs to be from the right trigger
		float h = 0;					//math term
		float i = 0;					//math term
		float norm;				//acts as a normalizer for lRotPow and rRotPow
		double lRotPow = 0;			//left rotary power for left motors
		double rRotPow = 0;			//right rotary power for right motors
		int distToAngle;		//distance between the joystick angle and the gyro angle


		gyroClean = gyroRaw % 360;
		if(gyroClean < 0){
			gyroClean = gyroClean + 360;
		}
		
		
		if(leftStickY > .1 || leftStickX > .1 || leftStickY < -.1 || leftStickX < -.1 ){
			rad = Math.atan2(leftStickX, leftStickY); //this code gets the x and y coords from the xbox controller
			deg = rad * (180 / Math.PI);     // and converts it to an angle
			deg = deg + 180;         //180 is added to compensate for the gyro angle starting at 0
			deg = 360 - deg;
			JSraw = deg;
		}
		if(leftStickY > .1 || leftStickX > .1 || leftStickY < -.1 || leftStickX < -.1 ){
			
			
			a = (int) (JSraw - gyroClean);
			if(a < 0) {
				b = a + 360;
			}
			else {
				b = a;
			}
			
			if(b > 180) {
				mul = -1;		//-1 is ccw
			}
			else {
				mul = 1;		//1 is cw
			}

			if(b > 180) {
				distToAngle = 360 - b;
			}
			else {
				distToAngle = b;
			}
						
			/*if(mul == 1) {
				c = b;
			}
			else {
				c = -1 * a;
			}*/

			d = (float) (distToAngle * mul * k);
			e = d * -1;

			if(Math.abs(d) > 1) {
				f = Math.abs(d) / d;
			}
			else {
				f = d;
			}

			g = f * -1;


			h = (float) (f + throttle);
			i = (float) (g + throttle);

			if(Math.abs(h) > Math.abs(i)) {
				norm = Math.abs(h);
			}
			else {
				norm = Math.abs(i);
			}

			if(norm > 1) {
				lRotPow = h / norm;
			}
			else {
				lRotPow = h;
			}

			if(norm > 1) {
				rRotPow = i / norm;
			}
			else {
				rRotPow = i;
			}
			
		}

		
		//  if(leftStickY > .1 || leftStickX > .1 || leftStickY < -.1 || leftStickX < -.1 ){
		driveLeftOne.set(lRotPow);						//drives the robot
		driveLeftTwo.set(lRotPow);
		driveRightOne.set(-rRotPow);
		driveRightTwo.set(-rRotPow);  

		if (xButton == true){
			gyro.reset();    //this code resets and calibrates the gyro
			gyro.calibrate();
		}
		if (yButton == true){
			gyro.reset();    //this code resets the gyro angle to 0
		}

			SmartDashboard.putNumber("JSraw", JSraw);
			SmartDashboard.putNumber("gyroClean", gyroClean);
			SmartDashboard.putNumber("a", a);
			SmartDashboard.putNumber("b", b);
			SmartDashboard.putNumber("c", c);
			SmartDashboard.putNumber("d", d);
			SmartDashboard.putNumber("e", e);
			SmartDashboard.putNumber("f", f);
			SmartDashboard.putNumber("g", g);
			SmartDashboard.putNumber("h", h);
			SmartDashboard.putNumber("i", i);
			SmartDashboard.putNumber("leftStickY", leftStickY);
			SmartDashboard.putNumber("leftStickX", leftStickX);
			SmartDashboard.putNumber("mul", mul);
			SmartDashboard.putNumber("lRotPow", lRotPow);
			SmartDashboard.putNumber("rRotPow", rRotPow);
		
		//table.putNumber("Useful gyro angle", angleGyro); //necessary, keep this
	}

	public void autonomous() {
		gyro.reset();
		gyro.calibrate();
	}

}