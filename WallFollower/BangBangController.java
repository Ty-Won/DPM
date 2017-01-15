package wallFollower;

import java.util.concurrent.TimeUnit;

import lejos.hardware.motor.*;

public class BangBangController implements UltrasonicController{
	private final int bandCenter, bandwidth;
	private final int motorLow, motorHigh;
	private EV3LargeRegulatedMotor leftMotor, rightMotor;
	private int filterControl;
	private final int FILTER_OUT = 20;
	private int FilteredDistance;
	
	public BangBangController(EV3LargeRegulatedMotor leftMotor, EV3LargeRegulatedMotor rightMotor,
							  int bandCenter, int bandwidth, int motorLow, int motorHigh) {
		//Default Constructor
		this.bandCenter = bandCenter;
		this.bandwidth = bandwidth;
		this.motorLow = motorLow;
		this.motorHigh = motorHigh;
		this.leftMotor = leftMotor;
		this.rightMotor = rightMotor;
		leftMotor.setSpeed(motorHigh);				// Start robot moving forward
		rightMotor.setSpeed(motorHigh);
		leftMotor.forward();
		rightMotor.forward();
	}
	
	@Override
	public void processUSData(int distance) {
		
		//Filter
		
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
		
		if(FilteredDistance>=120){
			

			leftMotor.setSpeed(motorHigh);
			rightMotor.setSpeed(motorHigh);
			leftMotor.forward();
			rightMotor.forward();
			
				try {
					TimeUnit.MILLISECONDS.sleep(650);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			
				if(FilteredDistance>=120) {
					
						leftMotor.setSpeed(motorLow);
						rightMotor.setSpeed(motorHigh);
						leftMotor.forward();
						rightMotor.forward();
						
				
				try {
					TimeUnit.MILLISECONDS.sleep(750);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				}	
		}
		
		if(Math.abs(distError)<=bandwidth){
			leftMotor.setSpeed(motorHigh);
			rightMotor.setSpeed(motorHigh);
			leftMotor.forward();
			rightMotor.forward();
		}
		else if(distError>0){
			leftMotor.setSpeed(motorLow);
			rightMotor.setSpeed(motorHigh);
			leftMotor.forward();
			rightMotor.forward();
		}
		else if(distError<0){
			leftMotor.setSpeed(motorHigh);
			rightMotor.setSpeed(motorLow);
			leftMotor.forward();
			rightMotor.forward();
		}
	}

	@Override
	public int readUSDistance() {
		return FilteredDistance;
	}
}
