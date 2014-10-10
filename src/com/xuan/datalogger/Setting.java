package com.xuan.datalogger;

import com.xuan.dataloggerwithsetting.R;

import android.app.Activity;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

public class Setting extends Activity {

	private Button doneButton;
	private EditText viewMinGPSTime, viewMinGPSDist, viewMinNetTime, viewMinNetDist;
	private RadioGroup viewRateAcc, viewRateMag;

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        
        viewRateAcc = (RadioGroup) this.findViewById(R.id.rateAcc);
        viewRateMag = (RadioGroup) this.findViewById(R.id.rateMag);
        viewMinGPSTime = (EditText) this.findViewById(R.id.minGPSTime);
        viewMinGPSDist = (EditText) this.findViewById(R.id.minGPSDist);
        viewMinNetTime = (EditText) this.findViewById(R.id.minNetTime);
        viewMinNetDist = (EditText) this.findViewById(R.id.minNetDist);
        doneButton = (Button) this.findViewById(R.id.doneSetting);
        
        Intent intent = getIntent();
        int rateAcc = intent.getIntExtra("rateAcc", SensorManager.SENSOR_DELAY_NORMAL);
        if (rateAcc == SensorManager.SENSOR_DELAY_FASTEST) {
        	viewRateAcc.check(R.id.fastestAcc);
        } else if (rateAcc == SensorManager.SENSOR_DELAY_GAME) {
        	viewRateAcc.check(R.id.gameAcc);
        } else if (rateAcc == SensorManager.SENSOR_DELAY_NORMAL) {
        	viewRateAcc.check(R.id.normalAcc);
        } else if (rateAcc == SensorManager.SENSOR_DELAY_UI) {
        	viewRateAcc.check(R.id.uiAcc);
        } else {
        	viewRateAcc.check(R.id.normalAcc);
        }
        
        int rateMag = intent.getIntExtra("rateMag", SensorManager.SENSOR_DELAY_NORMAL);
        if (rateMag == SensorManager.SENSOR_DELAY_FASTEST) {
        	viewRateMag.check(R.id.fastestMag);
        } else if (rateMag == SensorManager.SENSOR_DELAY_GAME) {
        	viewRateMag.check(R.id.gameMag);
        } else if (rateMag == SensorManager.SENSOR_DELAY_NORMAL) {
        	viewRateMag.check(R.id.normalMag);
        } else if (rateMag == SensorManager.SENSOR_DELAY_UI) {
        	viewRateMag.check(R.id.uiMag);
        } else {
        	viewRateMag.check(R.id.normalMag);
        }
        
        Integer minGPSTime = intent.getIntExtra("minGPSTime",0);
        viewMinGPSTime.setText(minGPSTime.toString());
        Integer minGPSDist = intent.getIntExtra("minGPSDist",0);
        viewMinGPSDist.setText(minGPSDist.toString());
        Integer minNetTime = intent.getIntExtra("minNetTime",0);
        viewMinNetTime.setText(minNetTime.toString());
        Integer minNetDist = intent.getIntExtra("minNetDist",0);
        viewMinNetDist.setText(minNetDist.toString());
        
        doneButton.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v) {
				Intent intent = new Intent();
				
				Integer rateAcc = 0;
				switch (viewRateAcc.getCheckedRadioButtonId()) {
				case R.id.fastestAcc: rateAcc = SensorManager.SENSOR_DELAY_FASTEST;
				break;
				case R.id.gameAcc: rateAcc = SensorManager.SENSOR_DELAY_GAME;
				break;
				case R.id.normalAcc: rateAcc = SensorManager.SENSOR_DELAY_NORMAL;
				break;
				case R.id.uiAcc: rateAcc = SensorManager.SENSOR_DELAY_UI;
				break;
				}
				intent.putExtra("rateAcc", rateAcc);

				Integer rateMag = 0;
				switch (viewRateMag.getCheckedRadioButtonId()) {
				case R.id.fastestMag: rateMag = SensorManager.SENSOR_DELAY_FASTEST;
				break;
				case R.id.gameMag: rateMag = SensorManager.SENSOR_DELAY_GAME;
				break;
				case R.id.normalMag: rateMag = SensorManager.SENSOR_DELAY_NORMAL;
				break;
				case R.id.uiMag: rateMag = SensorManager.SENSOR_DELAY_UI;
				break;
				}
				intent.putExtra("rateMag", rateMag);
				
				intent.putExtra("minGPSTime",Integer.parseInt(viewMinGPSTime.getText().toString()));
				intent.putExtra("minGPSDist",Integer.parseInt(viewMinGPSDist.getText().toString()));
				intent.putExtra("minNetTime",Integer.parseInt(viewMinNetTime.getText().toString()));
				intent.putExtra("minNetDist",Integer.parseInt(viewMinNetDist.getText().toString()));
				
				setResult(RESULT_OK, intent);
				finish();
			}
        });

	}
}
