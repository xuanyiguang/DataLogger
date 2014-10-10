package com.xuan.dataloggerwithsetting;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.FloatMath;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Logger extends Activity {
	
	private EditText dirNameInput;
	private Button startButton,finishButton,settingButton;
	private TextView statusDisplay,statusAcc,statusMag;
	
	private SensorManager mSensorManager;
	private AccelerometerListener mAccelerometerListener;
	private MagnetometerListener mMagnetometerListener;	
	private LocationManager mLocationManager;
	private LocationListener mLocationListener;
	
	private String dirName;
	private File dir,fileAcc,fileMag,fileGps,fileNet;
	private FileWriter fwAcc,fwMag,fwGps,fwNet;
	
	private Boolean flagAcc = false;
	private Boolean flagMag = false;
	private Boolean flagGps = false;	
	private Boolean flagNet = false;
	private Long prevTimeAcc,prevTimeMag,prevTimeGps,prevTimeNet;

	final static private int REQUEST_SETTING = 1;
	
	private Integer rateAcc = SensorManager.SENSOR_DELAY_NORMAL;
	private Integer rateMag = SensorManager.SENSOR_DELAY_NORMAL;
	private Integer minGPSTime = 0;
	private Integer minGPSDist = 0;
	private Integer minNetTime = 0;
	private Integer minNetDist = 0;
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if ((requestCode == REQUEST_SETTING) && (resultCode == RESULT_OK)) {
			if (data.hasExtra("rateAcc")) {
				rateAcc = data.getIntExtra("rateAcc", 3);
			}
			if (data.hasExtra("rateMag")) {
				rateMag = data.getIntExtra("rateMag", 3);
			}
			if (data.hasExtra("minGPSTime")) {
				minGPSTime = data.getIntExtra("minGPSTime", 0);
			}
			if (data.hasExtra("minGPSDist")) {
				minGPSDist = data.getIntExtra("minGPSDist", 0);
			}
			if (data.hasExtra("minNetTime")) {
				minNetTime = data.getIntExtra("minNetTime", 0);
			}
			if (data.hasExtra("minNetDist")) {
				minNetDist = data.getIntExtra("minNetDist", 0);
			}
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logger);
        
    	//Initialize the views
        dirNameInput = (EditText) findViewById(R.id.dirNameInput);
        settingButton = (Button) findViewById(R.id.setting);
        startButton = (Button) findViewById(R.id.start);
        finishButton = (Button) findViewById(R.id.finish);
        statusDisplay = (TextView) findViewById(R.id.status);
        statusAcc = (TextView) findViewById(R.id.statusAcc);
        statusMag = (TextView) findViewById(R.id.statusMag);
        String d = new String();
        d += "phone-";
        d += String.format("%04d", Calendar.getInstance().get(Calendar.YEAR));
        d += String.format("%02d", Calendar.getInstance().get(Calendar.MONTH)+1);
		d += String.format("%02d", Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
		d += String.format("%02d", Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
		d += String.format("%02d", Calendar.getInstance().get(Calendar.MINUTE));
        dirNameInput.setText(d);
        
        //Register OnClickListener for the buttons
        settingButton.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v) {
				Intent intent = new Intent(Logger.this, Setting.class);
				intent.putExtra("rateAcc", rateAcc);
				intent.putExtra("rateMag", rateMag);
				intent.putExtra("minGPSTime", minGPSTime);
				intent.putExtra("minGPSDist", minGPSDist);
				intent.putExtra("minNetTime", minNetTime);
				intent.putExtra("minNetDist", minNetDist);
				startActivityForResult(intent, REQUEST_SETTING);
			}
        });
        
        startButton.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v) {
        		// when "Start" button is pressed
        		dirName = dirNameInput.getText().toString();
				
				if (dirName == null || dirName.length() == 0){
					Toast.makeText(Logger.this,"Input File Name First!",Toast.LENGTH_LONG).show();
				} else {
					dir = new File(Environment.getExternalStorageDirectory(),dirName); // add "/" to the end
					if (!dir.exists()){
						dir.mkdirs();
					}
					
					fileAcc = new File(dir,"Acc.csv");
					fileMag = new File(dir,"Mag.csv");
					fileGps = new File(dir,"Gps.csv");
					fileNet = new File(dir,"Net.csv");
					
					if (!fileAcc.exists()){
		    			try {
							fileAcc.createNewFile();
						} catch (IOException e) {
							e.printStackTrace();
						}
		    		}
					if (!fileMag.exists()){
		    			try {
							fileMag.createNewFile();
						} catch (IOException e) {
							e.printStackTrace();
						}
		    		}
					if (!fileGps.exists()){
		    			try {
							fileGps.createNewFile();
						} catch (IOException e) {
							e.printStackTrace();
						}
		    		}
					if (!fileNet.exists()){
		    			try {
							fileNet.createNewFile();
						} catch (IOException e) {
							e.printStackTrace();
						}
		    		}
					
					// make the file ready for write
					try {
						fwAcc = new FileWriter(fileAcc);
						fwMag = new FileWriter(fileMag);
						fwGps = new FileWriter(fileGps);
						fwNet = new FileWriter(fileNet);
					} catch (IOException e) {
						e.printStackTrace();
					}
			        mSensorManager.registerListener(mAccelerometerListener, 
			        		mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), 
			        		rateAcc);
			        mSensorManager.registerListener(mMagnetometerListener, 
			        		mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), 
			        		rateMag);
			        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minGPSTime*1000, minGPSDist, mLocationListener);
			        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minNetTime*1000, minNetDist, mLocationListener);

				} 
			}        	
        });        
        finishButton.setOnClickListener(new OnClickListener(){
        	public void onClick(View v) {
        		// when "Finish" button is pressed
        		try {
					fwAcc.close();
					fwMag.close();
					fwGps.close();
					fwNet.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				mSensorManager.unregisterListener(mAccelerometerListener);
				mSensorManager.unregisterListener(mMagnetometerListener);
				mLocationManager.removeUpdates(mLocationListener);
        		finish();
			}        	
        });
        
        //Initialize sensors
        mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometerListener = new AccelerometerListener();
        mMagnetometerListener = new MagnetometerListener();
        
        //Initialize and register GPS and Network-based Location Service
        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new LocationListener(){
			public void onLocationChanged(Location location) {
				//Record the Location from GPS or Network (Cell or Wifi)
				//timestamp, source (GPS, Network), location, accuracy level
				
				if (location.getProvider().equals("gps")){
					UpdateStatus(System.currentTimeMillis(),"Gps");
					String text = "";
			    	text += Long.toString(location.getTime()) + ",";
			    	text += Long.toString(System.currentTimeMillis()) + ",";
			    	text += Double.toString(location.getLatitude()) + ",";
			    	text += Double.toString(location.getLongitude()) + ",";
			    	text += Float.toString(location.getAccuracy()) + ",";
			    	text += Float.toString(location.getSpeed()) + ",";
			    	text += Float.toString(location.getBearing()) + ",";
			    	text += Double.toString(location.getAltitude()) + "\n";
			    	try {
						fwGps.write(text);
					} catch (IOException e) {
						e.printStackTrace();
					}					
				} else if (location.getProvider().equals("network")){
					UpdateStatus(System.currentTimeMillis(),"Net");
					String text = "";
			    	text += Long.toString(location.getTime()) + ",";
			    	text += Long.toString(System.currentTimeMillis()) + ",";
			    	text += Double.toString(location.getLatitude()) + ",";
			    	text += Double.toString(location.getLongitude()) + ",";
			    	text += Float.toString(location.getAccuracy()) + ",";
			    	text += Float.toString(location.getSpeed()) + ",";
			    	text += Float.toString(location.getBearing()) + ",";
			    	text += Double.toString(location.getAltitude()) + "\n";
			    	try {
						fwNet.write(text);
					} catch (IOException e) {
						e.printStackTrace();
					}					
				} 
			}

			public void onProviderDisabled(String provider) {}

			public void onProviderEnabled(String provider) {}

			public void onStatusChanged(String provider, int status,
					Bundle extras) {}        	
        };        
    }
    
    public class AccelerometerListener implements SensorEventListener{

		public void onAccuracyChanged(Sensor sensor, int accuracy) {}

		public void onSensorChanged(SensorEvent event) {
	    	// record the reading from Accelerometer and Magnetometer
	    	// timestamp, sensor type, values, accuracy level
			
			UpdateStatus(System.currentTimeMillis(),"Acc");
			String text = "";
	    	text += Long.toString(System.currentTimeMillis()) + ",";
	    	text += Float.toString(event.values[0]) + ",";
	    	text += Float.toString(event.values[1]) + ",";
	    	text += Float.toString(event.values[2]) + ",";
	    	text += Integer.toString(event.accuracy) + "\n";
	    	try {
				fwAcc.write(text);
			} catch (IOException e) {
				e.printStackTrace();
			}
			String str = "Acc = ";
			str += String.format("%5.2f", event.values[0]) + ", ";
			str += String.format("%5.2f", event.values[1]) + ", ";
			str += String.format("%5.2f", event.values[2]) + "; ";
			str += " A = " + String.format("%5.2f", FloatMath.sqrt(event.values[0]*event.values[0]+event.values[1]*event.values[1]+event.values[2]*event.values[2])) + "\n";
			statusAcc.setText(str);
//			The reading from Acc seems to be reversed
//			as if gravity is pointed up
		}
    	
    }
    
    public class MagnetometerListener implements SensorEventListener{

		public void onAccuracyChanged(Sensor sensor, int accuracy) {}

		public void onSensorChanged(SensorEvent event) {
	    	// record the reading from Accelerometer and Magnetometer
	    	// timestamp, sensor type, values, accuracy level
			
			UpdateStatus(System.currentTimeMillis(),"Mag");
			String text = "";	    	
	    	text += Long.toString(System.currentTimeMillis()) + ",";
	    	text += Float.toString(event.values[0]) + ",";
	    	text += Float.toString(event.values[1]) + ",";
	    	text += Float.toString(event.values[2]) + ",";
	    	text += Integer.toString(event.accuracy) + "\n";
	    	try {
				fwMag.write(text);
			} catch (IOException e) {
				e.printStackTrace();
			}
			String str = "Mag = ";
			str += String.format("%5.2f", event.values[0]) + ", ";
			str += String.format("%5.2f", event.values[1]) + ", ";
			str += String.format("%5.2f", event.values[2]) + "; ";
			str += " M = " + String.format("%5.2f", FloatMath.sqrt(event.values[0]*event.values[0]+event.values[1]*event.values[1]+event.values[2]*event.values[2])) + "\n";
			statusMag.setText(str);
//			The reading from Mag seems to be OK
//			magnetic north points to north and down
		}
    	
    }
    
    private void UpdateStatus(Long time, String type){
    	String str = "";
    	if (type.equals("Acc")){
    		str += "Processing Acc ..." + "\n\n";
    	} else if (type.equals("Mag")) {
    		str += "Processing Mag ..." + "\n\n";
    	} else if (type.equals("Gps")) {
    		str += "Processing Gps ..." + "\n\n";
    	} else if (type.equals("Net")) {
    		str += "Processing Net ..." + "\n\n";
    	}
    	
		if (flagAcc == false){
			str += "Acc: no data" + "\n";
		} else {
			str += "Acc: updated " + Long.toString(time-prevTimeAcc) + " millisec ago" + "\n";
		}
		if (flagMag == false){
			str += "Mag: no data" + "\n";
		} else {
			str += "Mag: updated " + Long.toString(time-prevTimeMag) + " millisec ago" + "\n";
		}
		if (flagGps == false){
			str += "Gps: no data" + "\n";
		} else {
			str += "Gps: updated " + Long.toString(time-prevTimeGps) + " millisec ago" + "\n";
		}
		if (flagNet == false){
			str += "Net: no data" + "\n";
		} else {
			str += "Net: updated " + Long.toString(time-prevTimeNet) + " millisec ago" + "\n";
		}
		statusDisplay.setText(str);
		
    	if (type.equals("Acc")){
    		flagAcc = true;
    		prevTimeAcc = time;
    	} else if (type.equals("Mag")) {
    		flagMag = true;
    		prevTimeMag = time;
    	} else if (type.equals("Gps")) {
    		flagGps = true;
    		prevTimeGps = time;
    	} else if (type.equals("Net")) {
    		flagNet = true;
    		prevTimeNet = time;
    	}
    }
    
}