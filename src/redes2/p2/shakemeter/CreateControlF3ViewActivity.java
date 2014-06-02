package redes2.p2.shakemeter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources.Theme;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class CreateControlF3ViewActivity extends Activity implements
		SensorEventListener{
	
	private String TAG = "Debug";
	
	private Bundle args;
	
	private boolean meterButtonPressed = false;

	//Variables para el beep
	AudioManager audioManager;
	private static SoundPool soundPool;
	private int soundID;
	private boolean loaded = false;
	private float volume;
	
	//Variable para la barra de progreso
	private ProgressDialog progress1;
	private ProgressDialog progress2;
	Handler handleProgress2 = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		    progress2.incrementProgressBy(1);
		}
	};
	
	//Variables para el acelerometro
	private SensorManager sensorManager;
	private Sensor accelerometer;
	
	//Variable para filtrar

	private final float alpha = 0.8f;

	//Arreglos para almacenar variables filtradas
	private float[] gravity = new float[3];
	private float[] accel = new float[3];
	
	//Variables para controlar la captura de datos del acelerometro
	private long lastSensorUpdate;
	private long lastProgressBarUpdate;
	private int progressValue;
	private boolean measured= false;
	
	private ArrayList<Measurement> measurements;
	
	//Variable con el nombre del archivo
	private String fileName = "measure_";
	FileOutputStream fos;
	
	Button measureOutButton;
	Button doneButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form_3_view);
		
		args = getIntent().getExtras().getBundle("Bundle");		
		
		//Preparacion del acelerometro
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		if (null == (accelerometer)){
			finish();
		}
		//Fin de la preparacion del acelerometro
		
		Log.d(TAG, "SENSOR PREPARED");
		
		//Preparacion del sonido
		soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId,
					int status) {
				loaded = true;
			}
		});
		soundID = soundPool.load(getApplicationContext(), R.raw.beep, 1);
		//Fin de la preparacion del sonido
		Log.d(TAG, "SOUND PREPARED");
		

		//Preparacion de la barra de progreso
		progress1 = new ProgressDialog(CreateControlF3ViewActivity.this);
		progress1.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	    progress1.setIndeterminate(false);
	    progress1.setMessage("Preparece para la medición");
		progress1.setMax(10);
		progress1.setProgress(0);
		progress1.setCancelable(false);
		
		progress2 = new ProgressDialog(CreateControlF3ViewActivity.this);
		progress2.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	    progress2.setIndeterminate(false);
	    progress2.setMessage("Medición en proceso");
		progress2.setMax(30);
		progress2.setProgress(0);
		progress2.setCancelable(false);
		//Fin de la preparacion de la barra de progreso
		Log.d(TAG, "PROGESSDIALOG PREPARED");
		
	      
		measureOutButton = (Button) findViewById(R.id.measure_out_b_f3);
		doneButton = (Button) findViewById(R.id.done_b_f3);
		
		measureOutButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AudioManager audioManager;
				float actualVolume;
				float maxVolume;
				
				Toast.makeText(CreateControlF3ViewActivity.this, "La medición comenzará en 10 segundos.", Toast.LENGTH_LONG).show();
				
				meterButtonPressed= true;
				
				audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
				actualVolume = (float) audioManager
						.getStreamVolume(AudioManager.STREAM_MUSIC);
				maxVolume = (float) audioManager
						.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
				volume = actualVolume / maxVolume;
				
				//Primer beep
				if(loaded){
					soundPool.play(soundID, volume, volume, 0, 0, 1);
				
						
					progress1.show();
					WaitingThread t1 = new WaitingThread(progress1);
					t1.start();
					try {
						t1.join();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					progress1.dismiss();
					Log.d(TAG, "FIRST BEEP ENDED");
				
					//Preparacion del arreglo de measurements
					if (measurements == null){
						measurements = new ArrayList<Measurement>();
					}else{
						measurements.clear();
					}
				
					//Segundo beep
					soundPool.play(soundID, volume, volume, 0, 1, 1);
					
					Log.d(TAG, "THE MEASURE BEGIN NOW");
					//Comienzo de la medicion
					progressValue = 0;
					
					progress2.show();
					lastSensorUpdate = System.currentTimeMillis();
					lastProgressBarUpdate = lastSensorUpdate;
					sensorManager.registerListener(CreateControlF3ViewActivity.this, accelerometer,
												   SensorManager.SENSOR_DELAY_UI);
					
				}
			}
		});
		
		doneButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SimpleDateFormat dateFormat;
				Calendar calendar;
				ContentValues contentValues;
				String date;
				DatabaseOpenHelper mDbHelper;
				Intent measurementViewActivity;
				
				if (measured){
					
					Log.d(TAG, "DONE AND MEASURED");
					
					measurementViewActivity = new Intent(getApplicationContext(), MeasurementsViewActivity.class);
					measurementViewActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
					
					//Obtener instacia de DatabaseOpenHelper
					mDbHelper = new DatabaseOpenHelper(getApplicationContext());
					
					//Obtener la fecha actual de la medicion
					dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
					calendar = Calendar.getInstance();
					date = dateFormat.format(calendar.getTime());
					
					//Nombre del archivo
					fileName += args.getString(DatabaseOpenHelper.MEDICAL_HISTORY_ID + "_" +
							args.getString(DatabaseOpenHelper.DATE) + ".data");
					
					args.putString(DatabaseOpenHelper.DATE, date);
					args.putString(DatabaseOpenHelper.MEASUREMENT_FILE, fileName);
					args.putInt(DatabaseOpenHelper.SENT, 0);
					
					
					contentValues = new ContentValues();
					contentValues.put(DatabaseOpenHelper.MEDICAL_HISTORY_ID, args.getString(DatabaseOpenHelper.MEDICAL_HISTORY_ID));
					contentValues.put(DatabaseOpenHelper.MEDICINE, args.getString(DatabaseOpenHelper.MEDICINE));
					contentValues.put(DatabaseOpenHelper.DOSE_MG, args.getString(DatabaseOpenHelper.MEDICINE));
					contentValues.put(DatabaseOpenHelper.DOSE_ML, args.getString(DatabaseOpenHelper.DOSE_MG));
					contentValues.put(DatabaseOpenHelper.HOURS_BETWEEN, args.getString(DatabaseOpenHelper.DOSE_ML));
					contentValues.put(DatabaseOpenHelper.ADMINISTRATION_ROUTE, args.getString(DatabaseOpenHelper.ADMINISTRATION_ROUTE));
					contentValues.put(DatabaseOpenHelper.GENERAL_SYMPTONS_LEVEL, args.getString(DatabaseOpenHelper.GENERAL_SYMPTONS_LEVEL));
					contentValues.put(DatabaseOpenHelper.MUSCULAR_SYMPTONS_LEVEL, args.getString(DatabaseOpenHelper.MUSCULAR_SYMPTONS_LEVEL));
					contentValues.put(DatabaseOpenHelper.MEASUREMENT_FILE, args.getString(DatabaseOpenHelper.MEASUREMENT_FILE));
					contentValues.put(DatabaseOpenHelper.DATE, args.getString(DatabaseOpenHelper.DATE));
					contentValues.put(DatabaseOpenHelper.SENT, args.getInt(DatabaseOpenHelper.SENT));
					
					writeFile();
					
					//Almacenar control en la base de datos
					mDbHelper.insertRecord(DatabaseOpenHelper.TABLE_CONTROL, null, contentValues);
					
					Log.d(TAG, "DONE AND MEASURED ENDED");
					if(getFileStreamPath(fileName).exists()){
						Log.d(TAG, "DOES THE FILE EXISTS?" + getFileStreamPath(fileName).exists());
						readFile();
					}
					
					
					
					//Volver a la vista de medidas como actividad principal
					startActivity(measurementViewActivity);
					
				}else{
					Toast.makeText(getApplicationContext(), "Debe realizar la medición", Toast.LENGTH_LONG).show();
				}
			}
		});
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		long actualTime;
		float rawX;
		float rawY;
		float rawZ;
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

			actualTime = System.currentTimeMillis();
			
			//Si paso un segundo entonces actualizar
			//la barra de progreso
			if (actualTime - lastProgressBarUpdate > 1000){
				lastProgressBarUpdate=actualTime;
				progressValue+=1;
				handleProgress2.sendMessage(handleProgress2.obtainMessage());
			}

			// Si ya pasaron mas de 30 segundos apago el sensor,
			// oculto la barra de progreso, suenan tres beeps e 
			// indico que la medicion fue realizada.
			if (progressValue > 30){
				sensorManager.unregisterListener(CreateControlF3ViewActivity.this);
				progress2.dismiss();
				if(loaded){
					soundPool.play(soundID, volume, volume, 0, 2, 1);
				}
				measured=true;
				meterButtonPressed = false;
				
			}else{
				//Medir cada 250 mseg
				if (actualTime - lastSensorUpdate > 250) {					
					
					lastSensorUpdate = actualTime;

					rawX = event.values[0];
					rawY = event.values[1];
					rawZ = event.values[2];

					// Gravedad
					gravity[0] = lowPass(rawX, gravity[0]);
					gravity[1] = lowPass(rawY, gravity[1]);
					gravity[2] = lowPass(rawZ, gravity[2]);

					// Aceleracion sin gravidad
					accel[0] = highPass(rawX, gravity[0]);
					accel[1] = highPass(rawY, gravity[1]);
					accel[2] = highPass(rawZ, gravity[2]);
					
					measurements.add(new Measurement(accel[0], accel[1], accel[2]));
				
				}
			}
		}
		
	}
	
	// Deemphasize transient forces
	private float lowPass(float current, float gravity) {
		return gravity * alpha + current * (1 - alpha);
	}

	// Deemphasize constant forces
	private float highPass(float current, float gravity) {
		return current - gravity;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		
	}
	
	private void writeFile() {
		PrintWriter pw;
		try {
			fos = openFileOutput(fileName, MODE_PRIVATE);
			pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(fos)));
			
			for(int i=0; i < measurements.size(); i++){
				pw.println(measurements.get(i).getX() +" "+
						   measurements.get(i).getY() +" "+ 
						   measurements.get(i).getZ());
			}
			
			pw.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), "No se pudo crear el control.", Toast.LENGTH_LONG).show();
			finish();
		}
		
	}
	
	private void readFile(){

		FileInputStream fis;
		BufferedReader br;
		Log.d(TAG, "Size: " + measurements.size());
			try {
				fis= openFileInput(fileName);
				br= new BufferedReader(new InputStreamReader(fis));

				String line = "";

				while (null != (line = br.readLine())) {
				
					Log.d(TAG, line);

				}

				br.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			

	}


	@Override
	public void onBackPressed() {
		if(meterButtonPressed){
			Toast.makeText(CreateControlF3ViewActivity.this, "Medición en proceso, por favor espere a que termine.", Toast.LENGTH_LONG).show();
		}else{
			super.onBackPressed();
		}
	}
}
