package redes2.p2.shakemeter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PreferencesViewActivity extends Activity {
	
	EditText medicalHistoryID;
	EditText waitingTime;
	EditText measureTime;
	Button saveButton;
	Button cancelButton;
	
	SharedPreferences sharedPreferences;
	Editor editor;
	Intent measurementViewActivity;
	
	String shakeMeterSettings = "ShakeMeterSettings";
	String medicalHistoryIDString = "medicalHistoryID";
	String waitingTimeString = "waitingTime";
	String measureTimeString = "measureTime";
	
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_view);
		
		medicalHistoryID = (EditText) findViewById(R.id.medical_history_et_pv);
		waitingTime = (EditText) findViewById(R.id.waiting_time_et_pv);
		measureTime = (EditText) findViewById(R.id.measure_time_et_pv);
		saveButton = (Button) findViewById(R.id.save_b_pv);
		cancelButton = (Button) findViewById(R.id.cancel_b_pv);
		
		sharedPreferences = getSharedPreferences(shakeMeterSettings, Context.MODE_PRIVATE);

	    if (sharedPreferences.contains(medicalHistoryIDString))
	    {
	       medicalHistoryID.setText(sharedPreferences.getString(medicalHistoryIDString, ""));
	    
	    }
	    if (sharedPreferences.contains(waitingTimeString))
	    {
	       waitingTime.setText(sharedPreferences.getString(waitingTimeString, ""));

	    }
	    if (sharedPreferences.contains(measureTimeString))
	    {
	       measureTime.setText(sharedPreferences.getString(measureTimeString, ""));

	    }		
		
		saveButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				measurementViewActivity = new Intent(getApplicationContext(), MeasurementsViewActivity.class);
				measurementViewActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				
				editor = sharedPreferences.edit();
			    editor.putString(medicalHistoryIDString, medicalHistoryID.getText().toString());
			    editor.putString(waitingTimeString, waitingTime.getText().toString());
			    editor.putString(measureTimeString, measureTime.getText().toString());

			    editor.commit();
			    
			    startActivity(measurementViewActivity);
				
			}
		});
		
		cancelButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				measurementViewActivity = new Intent(getApplicationContext(), MeasurementsViewActivity.class);
				measurementViewActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				
				startActivity(measurementViewActivity);
			}
		});
		
		
		
	}

}
