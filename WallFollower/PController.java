package wallFollower;
import java.util.concurrent.TimeUnit;

import lejos.hardware.motor.EV3LargeRegulatedMotor;

public class PController implements UltrasonicController {
	
	private final int bandCenter, bandwidth;
	private final int motorStraight = 300, FILTER_OUT = 20;
	private EV3LargeRegulatedMotor leftMotor, rightMotor;
	private int distance;
	private int filterControl;
	int FilteredDistance;
	
	public PController(EV3LargeRegulatedMotor leftMotor, EV3LargeRegulatedMotor rightMotor,
					   int bandCenter, int bandwidth) {
		//Default Constructor
		this.bandCenter = bandCenter;
		this.bandwidth = bandwidth;
		this.leftMotor = leftMotor;
		this.rightMotor = rightMotor;
		leftMotor.setSpeed(motorStraight);					// Initalize motor rolling forward
		rightMotor.setSpeed(motorStraight);
		leftMotor.forward();
		rightMotor.forward();
		filterControl = 0;
	}
	
	@Override
	public void processUSData(int distance) {

		if(distance == 21474)
			FilteredDistance=bandCenter;
		else if (distance >= bandCenter+40 && filterControl < FILTER_OUT) {
			// bad value, do not set the distance var, however do increment the
			// filter value
			filterControl++;
		} else if (distance >= bandCenter+40 && filterControl > FILTER_OUT) {
			// We have repeated large values, so there must actually be nothing
			// there: leave the distance alone
			FilteredDistance = distance;
		}	
		 else {
			// distance went below 255: reset filter and leave
			// distance alone.
			filterControl = 0;
			FilteredDistance = distance;
		}

		
		int distError=FilteredDistance-bandCenter;
		int deltaspeed=Math.abs(distError*15);
		
		if (deltaspeed>150)
			deltaspeed=150;
		
		/*if(FilteredDistance>=120){
			

			leftMotor.setSpeed(motorStraight);
			rightMotor.setSpeed(motorStraight);
			leftMotor.forward();
			rightMotor.forward();
			
				try {
					TimeUnit.MILLISECONDS.sleep(650);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			
				if(FilteredDistance>=120) {
					
						leftMotor.setSpeed(motorStraight-deltaspeed);
						rightMotor.setSpeed(motorStraight);
						leftMotor.forward();
						rightMotor.forward();
						
				
				try {
					TimeUnit.MILLISECONDS.sleep(750);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				}	
		}*/
		
		if(Math.abs(distError)<=bandwidth){
			leftMotor.setSpeed(motorStraight);
			rightMotor.setSpeed(motorStraight);
			leftMotor.forward();
			rightMotor.forward();
		}
		else if(distError>0){
			leftMotor.setSpeed(motorStraight-deltaspeed);
			rightMotor.setSpeed(motorStraight+deltaspeed);
			leftMotor.forward();
			rightMotor.forward();
		}
		else if(distError<0){
			leftMotor.setSpeed(motorStraight+deltaspeed);
			rightMotor.setSpeed(motorStraight-deltaspeed);
			leftMotor.forward();
			rightMotor.forward();
		}
	}

	
	@Override
	public int readUSDistance() {
		return FilteredDistance;
	}

}
