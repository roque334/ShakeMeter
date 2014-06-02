package redes2.p2.shakemeter;

import java.util.ArrayList;

import redes2.p2.shakemeter.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MeasurementsViewActivity extends Activity{
	
	private String TAG = "Debug";
	
	private DatabaseOpenHelper mDbHelper;
	
	private MeasurementViewAdapter mAdapter;
	
	private TextView mHeaderView;
	
	private Button settingsButton;
	
	private Button deleteButton;
	
	ListView mListView;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.measurements_view);
		
		mDbHelper = new DatabaseOpenHelper(getApplicationContext());
		
//		clearAll();
		
		ArrayList<MeasurementRecord> measurements = mDbHelper.getAllMeasures();
		
		mAdapter = new MeasurementViewAdapter(getApplicationContext());
		
		mAdapter.add(measurements, getApplicationContext());

		LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
		mHeaderView = (TextView) inflater.inflate(R.layout.header_measurement_listview, null);
				
		mListView = (ListView) findViewById(R.id.measures_lv_msv);
		mListView.addHeaderView(mHeaderView);
		
		mHeaderView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), CreateControlF1ViewActivity.class);
				startActivity(intent);
			}
		});
		
		mListView.setAdapter(mAdapter);
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int listPosition = position-1;
				
				Toast.makeText(MeasurementsViewActivity.this, "Item Pressed", Toast.LENGTH_LONG).show();
				
				MeasurementRecord mr = (MeasurementRecord) mAdapter.getList().get(listPosition);
				Log.d(TAG, mr.getMedicalHistoryID());
				Bundle args = new Bundle();
				args.putStringArrayList("Medidas", mr.getMeasurements());
				
				SendDialog sendDialog = new SendDialog();
				sendDialog.setArguments(args);
				sendDialog.show(getFragmentManager(), "Dialog");
				Log.d(TAG, "MEASUREMENT SENT");
				
			}
		});
		
		settingsButton = (Button) findViewById(R.id.settings_b_msv);
		deleteButton = (Button) findViewById(R.id.detele_b_msv);
		
		settingsButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent preferencesViewActivity = new Intent(getApplicationContext(), PreferencesViewActivity.class);
				startActivity(preferencesViewActivity);
				
			}
		});
		
		Log.d(TAG, "MEASUREMENTSVIEWACTIVITY COMPLETE.");
	}
	
//	@Override
//	protected void onListItemClick(ListView l, View v, int position, long id) {
////		super.onListItemClick(l, v, position, id);
//		
//		Toast.makeText(MeasurementsViewActivity.this, "Item Pressed", Toast.LENGTH_LONG).show();
//		
//		MeasurementRecord er = (MeasurementRecord)l.getItemAtPosition(position);
//		Bundle args = new Bundle();
//		args.putStringArrayList("Medidas", er.getMeasurements());
//		
//		SendDialog sendDialog = new SendDialog();
//		sendDialog.setArguments(args);
//		sendDialog.show(getFragmentManager(), "Dialog");
//		Log.d(TAG, "MEASUREMENT SENT");
//		
//	}
	
	// Delete all records
	private void clearAll() {
	
		mDbHelper.delete(DatabaseOpenHelper.TABLE_CONTROL, null, null);
		Log.d(TAG, "DATABASE DATA DELETED.");
		
	}

//	// Close database
//	@Override
//	protected void onDestroy() {
//
//		mDbHelper.deleteDatabase();
//		Log.d(TAG, "DATABASE DELETED.");
//		
//		super.onDestroy();
//
//	}

}
