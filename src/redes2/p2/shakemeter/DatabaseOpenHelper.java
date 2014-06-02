package redes2.p2.shakemeter;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseOpenHelper extends SQLiteOpenHelper {
	
	private String TAG = "Debug";
	
	final private Context mContext;
	
	final static int DATABASE_VERSION = 1;
	final static String DATABASE_NAME="ShakeMeter";
	
	final static String TABLE_CONTROL = "control";
	
	final static String CONTROL_ID = "controlID";
	final static String MEDICAL_HISTORY_ID=  "medicalHistoryID";
	final static String MEDICINE = "medicine";
	final static String DOSE_MG = "doseMg";
	final static String DOSE_ML = "doseMl";
	final static String HOURS_BETWEEN = "hoursBetween";
	final static String ADMINISTRATION_ROUTE = "administrationRoute";
	final static String GENERAL_SYMPTONS_LEVEL = "generalSymptonsLevel";
	final static String MUSCULAR_SYMPTONS_LEVEL = "muscularSymptonsLevel";
	final static String DATE = "date";
	final static String SENT = "sent";
	final static String MEASUREMENT_FILE = "measurementFile";
	
	final static String CREATE_TABLE_CONTROL = "CREATE TABLE "
            + TABLE_CONTROL + "(" + CONTROL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + MEDICAL_HISTORY_ID
            + " TEXT, " + MEDICINE + " TEXT, " + DOSE_MG + " TEXT, " + DOSE_ML
            + " TEXT, " + HOURS_BETWEEN + " TEXT, " + ADMINISTRATION_ROUTE 
            + " TEXT, " + GENERAL_SYMPTONS_LEVEL + " TEXT, " + MUSCULAR_SYMPTONS_LEVEL
            + " TEXT, " + DATE + " TEXT, " + SENT + " INTEGER, " + MEASUREMENT_FILE
            + " TEXT" + ")";
	
	final static String GET_ALL_CONTROLS_QUERY = "SELECT * FROM " + TABLE_CONTROL;

	public DatabaseOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTROL);
		db.execSQL(CREATE_TABLE_CONTROL);
		Log.d(TAG, "DATABASE CREATED");
		

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTROL);
		
		onCreate(db);
		Log.d(TAG, "DATABASE UPDATED");

	}
	
	public ArrayList<MeasurementRecord> getAllMeasures(){
		Log.d(TAG, "GETALLEVENTS BEGIN");
		ArrayList<MeasurementRecord> list = new ArrayList<MeasurementRecord>();
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(GET_ALL_CONTROLS_QUERY, null);
		
		while(cursor.moveToNext()){
			MeasurementRecord mr = new MeasurementRecord(
					cursor.getString(cursor.getColumnIndex(CONTROL_ID)),
					cursor.getString(cursor.getColumnIndex(MEDICAL_HISTORY_ID)),
					cursor.getString(cursor.getColumnIndex(MEDICINE)),
					cursor.getString(cursor.getColumnIndex(DOSE_MG)),
					cursor.getString(cursor.getColumnIndex(DOSE_ML)),
					cursor.getString(cursor.getColumnIndex(HOURS_BETWEEN)),
					cursor.getString(cursor.getColumnIndex(ADMINISTRATION_ROUTE)),
					cursor.getString(cursor.getColumnIndex(GENERAL_SYMPTONS_LEVEL)),
					cursor.getString(cursor.getColumnIndex(MUSCULAR_SYMPTONS_LEVEL)),
					cursor.getString(cursor.getColumnIndex(MEASUREMENT_FILE)),
					null,
					cursor.getString(cursor.getColumnIndex(DATE)),
					cursor.getInt(cursor.getColumnIndex(SENT))
					);
			list.add(mr);
		}
		db.close();
		Log.d(TAG, "GETALLMEASURES END");
		return list;
	}
	
	public void insertRecord(String TABLE_NAME, String nullColumnHack, ContentValues values){
		SQLiteDatabase db = this.getWritableDatabase();
		db.insert(TABLE_NAME, nullColumnHack, values);
		db.close();
		Log.d(TAG, "REGISTER INSERTED");
		
	}
	
	public void delete(String table, String whereClause, String[] whereArgs){
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(table, whereClause, whereArgs);
		db.close();
		
	}
	
	void deleteDatabase(){
		mContext.deleteDatabase(DATABASE_NAME);
	}
	

}
