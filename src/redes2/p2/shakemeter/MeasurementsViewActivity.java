package redes2.p2.shakemeter;

import java.util.ArrayList;

import redes2.p2.shakemeter.R;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class MeasurementsViewActivity extends ListActivity {
	
	private String TAG = "Debug";
	
	private DatabaseOpenHelper mDbHelper;
	
	private MeasurementViewAdapter mAdapter;
	
	private TextView mHeaderView;
	
	ListView mListView;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		mDbHelper = new DatabaseOpenHelper(getApplicationContext());
		
//		clearAll();
		
		ArrayList<MeasurementRecord> measurements = mDbHelper.getAllMeasures();
		
		mAdapter = new MeasurementViewAdapter(getApplicationContext());
		
		mAdapter.add(measurements, getApplicationContext());
		
		LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
		mHeaderView = (TextView) inflater.inflate(R.layout.header_measurement_listview, null);
		
		ListView mListView = getListView();
		mListView.addHeaderView(mHeaderView);
		
		mHeaderView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), CreateControlF1ViewActivity.class);
				startActivity(intent);
			}
		});
		
		setListAdapter(mAdapter);
		
		Log.d(TAG, "MEASUREMENTSVIEWACTIVITY COMPLETE.");
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		MeasurementRecord er = (MeasurementRecord)l.getItemAtPosition(position);
		Bundle args = new Bundle();
		args.putStringArrayList("Medidas", er.getMeasurements());
		
		SendDialog sendDialog = new SendDialog();
		sendDialog.setArguments(args);
		sendDialog.show(getFragmentManager(), "Dialog");
		Log.d(TAG, "MEASUREMENT SENT");
		
	}
	
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
