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
			
			//final int colorId = colorSensor.getColorID();
			// put your correction code here
			
			
			//SensorMode lightValue=colorSensor.getColorIDMode();
			
		//	colorSensor.setFloodlight(lejos.robotics.Color.WHITE);
			float intensity[]= new float[3];
			colorSensor.getRGBMode().fetchSample(intensity, 0);
		
			if(intensity[0]+intensity[1]+intensity[2]<0.30){
				Sound.beep();
				linecount++;
				
				if(linecount==2||linecount==3||linecount==5||linecount==6||linecount==8||linecount==9||linecount==11||linecount==12)
				correction(Xval,Yval);
				
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
	
	public void correction(double xvalue,double yvalue){
		double angle=Math.toDegrees(odometer.getTheta());
		double squareside=30.48;
		if(angle<10){
			odometer.setY(yvalue+squareside);
			
		}
		
		else if(80<=angle && angle<110){
			odometer.setX(xvalue+squareside);
		}
		
		else if(150<=angle && angle<200){
			odometer.setY(yvalue-squareside);
		}
	
		else if(240<=angle && angle<290){
			odometer.setX(xvalue-squareside);
		}
		
		
	}
}