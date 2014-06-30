package redes2.p2.shakemeter;


import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class SendDialog extends Dialog implements android.view.View.OnClickListener{
	
	Bundle args;
	Context context;
	
	EditText doctorID;
	Button sendButton;
	Button cancelButton;
	
	String medicalHistoryID;
	String medicine;
	String doseMG;
	String doseML;
	String hoursBetween;
	String administrationRoute;
	String generalSymptonsLevel;
	String muscularSymptonsLevel;
	String measurmentFile;
	String date;
	
	
	public SendDialog(Context context, Bundle args){
		super(context);
		this.context = context;
		this.args = args;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    setContentView(R.layout.send_dialog);
	    
	    doctorID = (EditText) findViewById(R.id.doctor_id_et_sd);
	    sendButton = (Button) findViewById(R.id.send_b_sd);
	    cancelButton = (Button) findViewById(R.id.cancel_b_sd);
	    
	    sendButton.setOnClickListener(this);
	    cancelButton.setOnClickListener(this);
	}
	
	
	@Override
	public void onClick(View v){
		String message;
		DatabaseOpenHelper mDbHelper;
		ContentValues cv = new ContentValues();
		if(v==sendButton){
			if (doctorID.length() != 0){
				mDbHelper = new DatabaseOpenHelper(context);
				
				String doctorEmail = doctorID.getText().toString();
				Toast.makeText(getContext(), "SendButton", Toast.LENGTH_LONG).show();
				Intent emailIntent = new Intent(Intent.ACTION_SEND);
				emailIntent.setType("text/plaint");
				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Measurement: "+args.getString(DatabaseOpenHelper.MEASUREMENT_FILE));
		        String to[] = { doctorEmail };
		        emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
		        message = buildMessage(args.getString(DatabaseOpenHelper.MEASUREMENT_FILE));
		        emailIntent.putExtra(Intent.EXTRA_TEXT, message);
		        
		        try {
		            context.startActivity(Intent.createChooser(emailIntent, "Enviar correo..."));
		            Log.i("Finished sending email...", "");
		            cv.put(DatabaseOpenHelper.SENT, 1);
		            mDbHelper.updateRecord(DatabaseOpenHelper.TABLE_CONTROL, cv, DatabaseOpenHelper.CONTROL_ID +" = "+args.getString(DatabaseOpenHelper.CONTROL_ID), null);
		            this.dismiss();
		         } catch (android.content.ActivityNotFoundException ex) {
		            Toast.makeText(context, 
		            "There is no email client installed.", Toast.LENGTH_SHORT).show();
		         }
			}else{
				Toast.makeText(context, "Debe especificar un correo", Toast.LENGTH_LONG).show();
			}
		}
		
		if(v==cancelButton){
			Toast.makeText(getContext(), "CancelButton", Toast.LENGTH_LONG).show();
			this.dismiss();
		}
	}

	public String buildMessage(String fileName){
		String measurements;
		String message = "";
		message += DatabaseOpenHelper.MEDICAL_HISTORY_ID + "= " + args.getString(DatabaseOpenHelper.MEDICAL_HISTORY_ID);
		message += "\n";
		message += DatabaseOpenHelper.DATE + "= " + args.getString(DatabaseOpenHelper.DATE);
		message += "\n";
		message += DatabaseOpenHelper.MEDICINE + "= " + args.getString(DatabaseOpenHelper.MEDICINE);
		message += "\n";
		message += DatabaseOpenHelper.DOSE_MG + "= " + args.getString(DatabaseOpenHelper.DOSE_MG);
		message += "\n";
		message += DatabaseOpenHelper.DOSE_ML + "= " + args.getString(DatabaseOpenHelper.DOSE_ML);
		message += "\n";
		message += DatabaseOpenHelper.HOURS_BETWEEN + "= " + args.getString(DatabaseOpenHelper.HOURS_BETWEEN);
		message += "\n";
		message += DatabaseOpenHelper.ADMINISTRATION_ROUTE + "= " + args.getString(DatabaseOpenHelper.ADMINISTRATION_ROUTE);
		message += "\n";
		message += DatabaseOpenHelper.GENERAL_SYMPTONS_LEVEL + "= " + args.getString(DatabaseOpenHelper.GENERAL_SYMPTONS_LEVEL);
		message += "\n";
		message += DatabaseOpenHelper.MUSCULAR_SYMPTONS_LEVEL + "= " + args.getString(DatabaseOpenHelper.MUSCULAR_SYMPTONS_LEVEL);
		message += "\n";
		
		measurements = readFile(fileName);
		
		message += DatabaseOpenHelper.MEASUREMENTS + "\n";
		
		message += measurements;
		
		return message;	
	}
	
	private String readFile(String fileName){		
		FileInputStream fis;
		BufferedReader br;
		String measurements = "";
			try {
				fis= context.openFileInput(fileName);
				br= new BufferedReader(new InputStreamReader(fis));

				String line = "";

				while (null != (line = br.readLine())) {
					measurements += line + "\n";
				}

				br.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return measurements;
	}
}
