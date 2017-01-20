/* 
 * OdometryCorrection.java
 */
package ev3Odometer;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.Sound;




public class OdometryCorrection extends Thread {
	private static final long CORRECTION_PERIOD = 10;
	private Odometer odometer;
	public EV3ColorSensor colorSensor;
	public int linecount;
	public double Xval;
	public double Yval;
	public OdometryCorrection(Odometer odometer) {
		this.odometer = odometer;
		colorSensor= new EV3ColorSensor(SensorPort.S1);
		Sound.setVolume(20);
	}

	// run method (required for Thread)
	public void run() {
		long correctionStart, correctionEnd;

		while (true) {
			correctionStart = System.currentTimeMillis();
			
			//Assigning a base color intensity (intensity of black) to compare the colosensor readings
			float intensity[]= new float[3];
			
			//Retrieving the color sensor value
			colorSensor.getRGBMode().fetchSample(intensity, 0);
			
			//if statement determines if the intensity reading corresponds to being a dark intensity (suggests that the colorsensor detects a line)
			if(intensity[0]+intensity[1]+intensity[2]<0.30){
				Sound.beep();
				linecount++;
				
				// If statement checks number of line encounters and uses it to determine when to apply the odometer correction when performing a square 
				if(linecount==2||linecount==3||linecount==5||linecount==6||linecount==8||linecount==9||linecount==11||linecount==12)
				correction(Xval,Yval);
				
				//obtaining new values every while loop run to use with the correction method
				Xval=odometer.getX();
				Yval=odometer.getY();
			}

			
			
			
			
			// this ensure the odometry correction occurs only once every period
			correctionEnd = System.currentTimeMillis();
			if (correctionEnd - correctionStart < CORRECTION_PERIOD) {
				try {
					Thread.sleep(CORRECTION_PERIOD
							- (correctionEnd - correctionStart));
				} catch (InterruptedException e) {
					// there is nothing to be done here because it is not
					// expected that the odometry correction will be
					// interrupted by another thread
				}
			}
		}
	}
	
	//Correction method
	public void correction(double xvalue,double yvalue){
		
		//Setting the angle to degrees using the Math toDegrees method
		double angle=Math.toDegrees(odometer.getTheta());
		
		//The square length for each square panel
		double squareside=30.48;
		
		//Initially, while traveling straight up along the Y, set odometer correction to the Y if its below 10 degrees from the initial
		if(angle<10){
			odometer.setY(yvalue+squareside);
		}
		
		// Changes the corrections to apply in the x direction if the angle turned is within range of 80 to 110 degrees
		else if(80<=angle && angle<110){
			odometer.setX(xvalue+squareside);
		}
		
		//While traveling down along the Y, set odometer correction to the Y if its rotation lies between 150 degrees and 200
		else if(150<=angle && angle<200){
			odometer.setY(yvalue-squareside);
		}
		

		// Changes the corrections to apply in the x direction if the angle turned is within range of 240 to 290 degrees to head back towards the origin
		
		else if(240<=angle && angle<290){
			odometer.setX(xvalue-squareside);
		}
		
		
	}
}