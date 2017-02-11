
import lejos.hardware.Button;
import lejos.hardware.motor.EV3LargeRegulatedMotor;


public class Kobe {
	private static final int ROTATE_SPEED = 150;
	
	
	public EV3LargeRegulatedMotor leftMotor;
	public EV3LargeRegulatedMotor rightMotor;
	public EV3LargeRegulatedMotor catapultR;
	public EV3LargeRegulatedMotor catapultL;
	int launchSpeed;
	int launchAngle=90;
	public static double WHEEL_RADIUS = 2.1;
	public static final double TRACK = 14.2;
	int buttonChoice;
	
	
	//angle orientation
	double Orientation;
	
	public Kobe(EV3LargeRegulatedMotor leftmotor, EV3LargeRegulatedMotor rightmotor,EV3LargeRegulatedMotor catapultL,EV3LargeRegulatedMotor catapultR) {
		this.leftMotor=leftMotor;
		this.rightMotor=rightMotor;
		this.catapultL=catapultL;
		this.catapultR=catapultR;
		
	}
	
	
	
	//methods for shooting
	public void middleShot(){
		catapultL.setSpeed(1000);
		catapultR.setSpeed(1000);
		leftMotor.setSpeed(ROTATE_SPEED);
		rightMotor.setSpeed(ROTATE_SPEED);
		do{
			//throw
			catapultL.rotate(launchAngle,false);
			catapultR.rotate(launchAngle,true);
			
			//return to launch position
			catapultL.rotate(-launchAngle,false);
			catapultR.rotate(-launchAngle,true);
		
			buttonChoice = Button.waitForAnyPress();
			
			
			
		}while(buttonChoice != Button.ID_ESCAPE);
		
	}
	
	public void leftShot(){
		Orientation=Math.atan(-1/3);
		leftMotor.rotate(convertAngle(WHEEL_RADIUS, TRACK, Math.toDegrees(Orientation)), true);
		rightMotor.rotate(-convertAngle(WHEEL_RADIUS, TRACK, Math.toDegrees(Orientation)), false);
		leftMotor.setSpeed(ROTATE_SPEED);
		rightMotor.setSpeed(ROTATE_SPEED);
		
		catapultL.setSpeed(1000);
		catapultR.setSpeed(1000);
		do{
			//throw
			catapultL.rotate(launchAngle,false);
			catapultR.rotate(launchAngle,true);
			
			//return to launch position
			catapultL.rotate(-launchAngle,false);
			catapultR.rotate(-launchAngle,true);
		
			buttonChoice = Button.waitForAnyPress();
			
			
			
		}while(buttonChoice != Button.ID_ESCAPE);
		
	}
	
	
	public void rightShot(){
		
		Orientation=Math.atan(1/3);
		leftMotor.rotate(convertAngle(WHEEL_RADIUS, TRACK, Math.toDegrees(Orientation)), true);
		rightMotor.rotate(-convertAngle(WHEEL_RADIUS, TRACK, Math.toDegrees(Orientation)), false);
		catapultL.setSpeed(1000);
		catapultR.setSpeed(1000);
		leftMotor.setSpeed(ROTATE_SPEED);
		rightMotor.setSpeed(ROTATE_SPEED);
		
		do{
			//throw
			catapultL.rotate(launchAngle,false);
			catapultR.rotate(launchAngle,true);
			
			//return to launch position
			catapultL.rotate(-launchAngle,false);
			catapultR.rotate(-launchAngle,true);
		
			buttonChoice = Button.waitForAnyPress();
			
			
			
		}while(buttonChoice != Button.ID_ESCAPE);
		
		
		
	}
	
	
	//Angle and distance conversion methods
	
	private static int convertDistance(double radius, double distance) {
		return (int) ((180.0 * distance) / (Math.PI * radius));
	}

	private static int convertAngle(double radius, double width, double angle) {
		return convertDistance(radius, Math.PI * width * angle / 360.0);
	}
	


}
