package redes2.p2.shakemeter;

import java.util.ArrayList;

public class MeasurementRecord {
	
	private String controlID;

	private String medicalHistoryID;
	
	private String medicine;
	
	private String doseMg;
	
	private String doseMl;
	
	private String hoursBetween;
	
	private String administrationRoute;
	
	private String generalSymptonsLevel;
	
	private String muscularSymptonsLevel;
	
	private String measurmentFile;
	
	private ArrayList<String> measurements;
	
	private String date;
	
	private int sent;
	
	public MeasurementRecord(String controlID, String medicalHistoryID, 
			String medicine, String doseMg, String doseMl, String hoursBetween,
			String administrationRoute, String generalSymptonsLevel,
			String muscularSymptonsLevel, String measurementFile,
			ArrayList<String> measurements, String date, int sent) {
		this.controlID = controlID;
		this.medicalHistoryID = medicalHistoryID;
		this.medicine = medicine;
		this.doseMg = doseMg;
		this.doseMl = doseMl;
		this.hoursBetween = hoursBetween;
		this.administrationRoute = administrationRoute;
		this.generalSymptonsLevel = generalSymptonsLevel;
		this.muscularSymptonsLevel = muscularSymptonsLevel;
		this.measurmentFile = measurementFile;
		if (measurements == null) {
			this.measurements = null;
		}else{
			this.measurements = (ArrayList<String>) measurements.clone();
		}
		this.date = date;
		this.sent = sent;
	}
	
	public String getControlID() {
		return controlID;
	}

	public void setControlID(String controlID) {
		this.controlID = controlID;
	}

	public String getMedicalHistoryID() {
		return medicalHistoryID;
	}

	public void setMedicalHistoryID(String medicalHistoryID) {
		this.medicalHistoryID = medicalHistoryID;
	}

	public String getMedicine() {
		return medicine;
	}

	public void setMedicine(String medicine) {
		this.medicine = medicine;
	}

	public String getDoseMg() {
		return doseMg;
	}

	public void setDoseMg(String doseMg) {
		this.doseMg = doseMg;
	}

	public String getDoseMl() {
		return doseMl;
	}

	public void setDoseMl(String doseMl) {
		this.doseMl = doseMl;
	}

	public String getHoursBetween() {
		return hoursBetween;
	}

	public void setHoursBetween(String hoursBetween) {
		this.hoursBetween = hoursBetween;
	}

	public String getAdministrationRoute() {
		return administrationRoute;
	}

	public void setAdministrationRoute(String administrationRoute) {
		this.administrationRoute = administrationRoute;
	}

	public String getGeneralSymptonsLevel() {
		return generalSymptonsLevel;
	}

	public void setGeneralSymptonsLevel(String generalSymptonsLevel) {
		this.generalSymptonsLevel = generalSymptonsLevel;
	}

	public String getMuscularSymptonsLevel() {
		return muscularSymptonsLevel;
	}

	public void setMuscularSymptonsLevel(String muscularSymptonsLevel) {
		this.muscularSymptonsLevel = muscularSymptonsLevel;
	}
	
	public String getMeasurmentFile() {
		return measurmentFile;
	}

	public void setMeasurmentFile(String measurmentFile) {
		this.measurmentFile = measurmentFile;
	}

	public ArrayList<String> getMeasurements() {
		if (measurements == null){
			return null;
		}else{
			return (ArrayList<String>) measurements.clone();
		}
	}

	public void setMeasurements(ArrayList<String> measurements) {
		this.measurements = (ArrayList<String>) measurements.clone();
	} 

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getSent() {
		return sent;
	}

	public void setSent(int sent) {
		this.sent = sent;
	}

}
