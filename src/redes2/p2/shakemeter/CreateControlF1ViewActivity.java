package redes2.p2.shakemeter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CreateControlF1ViewActivity extends Activity {
	
	private String TAG = "Debug"; 
	
	private EditText medicalHistoryEditText;
	private EditText medicineEditText;
	private EditText doseEditText;
	private RadioButton mgRadioButton;
	private RadioButton mlRadioButton;
	private EditText hoursBetweenEditText;
	private Spinner administrationRouteSpinner;
	private Button nextButton;
	
	SharedPreferences sharedPreferences;
	String shakeMeterSettings = "ShakeMeterSettings";
	String medicalHistoryIDString = "medicalHistoryID";
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form_1_view);
		
		medicalHistoryEditText = (EditText) findViewById(R.id.medical_history_et_f1);
		medicineEditText = (EditText) findViewById(R.id.medicine_et_f1);
		doseEditText = (EditText) findViewById(R.id.dose_et_f1);
		mgRadioButton = (RadioButton) findViewById(R.id.mg_rb_f1);
		mlRadioButton = (RadioButton) findViewById(R.id.ml_rb_f1);
		hoursBetweenEditText = (EditText) findViewById(R.id.hours_et_f1);
		administrationRouteSpinner = (Spinner) findViewById(R.id.administration_route_s_f1);
		nextButton = (Button) findViewById(R.id.next_b_f1);
		
		sharedPreferences = getSharedPreferences(shakeMeterSettings, Context.MODE_PRIVATE);

	    if (sharedPreferences.contains(medicalHistoryIDString))
	    {
	       medicalHistoryEditText.setText(sharedPreferences.getString(medicalHistoryIDString, ""));
	    
	    }
		
		nextButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent createControlF2ViewActivity;
				Bundle args;
				String medicalHistoryID;
				String medicine;
				String doseMg;
				String doseMl;
				String hoursBetween;
				String administrationRoute;
				
				medicalHistoryID = "";
				medicine = "";
				doseMg = "";
				doseMl = "";
				hoursBetween = "";
				administrationRoute = "";
				
				if(medicalHistoryEditText.length() != 0){
					medicalHistoryID = medicalHistoryEditText.getText().toString();
				}
				
				if(medicineEditText.length() != 0){
					medicine = medicineEditText.getText().toString();
				}
				
				if(mgRadioButton.isChecked()){
					if(doseEditText.length()!=0){
						doseMg = doseEditText.getText().toString();
					}
				}else{
					if(doseEditText.length()!=0){
						doseMl = doseEditText.getText().toString();
					}
				}
				
				if(hoursBetweenEditText.length()!=0){
					hoursBetween = hoursBetweenEditText.getText().toString();
				}
				
				administrationRoute = administrationRouteSpinner.getSelectedItem().toString();
				
				Log.d(TAG, "MedicalHistoryID: " + medicalHistoryID);
				Log.d(TAG, "Medicine: " + medicine);
				Log.d(TAG, "DoseMg: " + doseMg);
				Log.d(TAG, "DoseMl: " + doseMl);
				Log.d(TAG, "HoursBetween: " + hoursBetween);
				Log.d(TAG, "AdministrationRoute: " + administrationRoute);
				
				if(!medicalHistoryID.isEmpty() && !medicine.isEmpty() 
						&& (!doseMg.isEmpty() || !doseMl.isEmpty())
						&& !hoursBetween.isEmpty() && !administrationRoute.isEmpty()){
					
					Log.d(TAG, "ALL FIELDS HAVE DATA");
					
					createControlF2ViewActivity = new Intent(getApplicationContext(), CreateControlF2ViewActivity.class);
					
					args = new Bundle();
					args.putString(DatabaseOpenHelper.MEDICAL_HISTORY_ID, medicalHistoryID);
					args.putString(DatabaseOpenHelper.MEDICINE, medicine);
					args.putString(DatabaseOpenHelper.DOSE_MG, doseMg);
					args.putString(DatabaseOpenHelper.DOSE_ML, doseMl);
					args.putString(DatabaseOpenHelper.HOURS_BETWEEN, hoursBetween);
					args.putString(DatabaseOpenHelper.ADMINISTRATION_ROUTE, administrationRoute);
					
					createControlF2ViewActivity.putExtra("Bundle", args);
					
					startActivity(createControlF2ViewActivity);
					
				}else{
					Log.d(TAG, "SOME FIELDS DOESNT HAVE DATA");
					
					Toast.makeText(getApplicationContext(), 
							"Debe llenar todos los campos para poder avanzar.", 
							Toast.LENGTH_LONG).show();
				}
			}
		});
	}

}
