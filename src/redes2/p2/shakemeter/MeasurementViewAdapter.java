package redes2.p2.shakemeter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class MeasurementViewAdapter extends BaseAdapter {
	
	private ArrayList<MeasurementRecord> list = new ArrayList<MeasurementRecord>(); 
	private static LayoutInflater inflater = null;
	private Context mContext;
	
	public MeasurementViewAdapter(Context context){
		mContext = context;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View newView = convertView;
		ViewHolder holder;
		
		MeasurementRecord curr = list.get(position);
		
		if(convertView == null){
			holder = new ViewHolder();
			newView = inflater.inflate(R.layout.measurement_view, null);
			holder.medicalHistoryTV = (TextView) newView.findViewById(R.id.medical_history_tv_mv);
			holder.dateTV = (TextView) newView.findViewById(R.id.measurement_date_tv_mv);
			holder.sentCB = (CheckBox) newView.findViewById(R.id.sent_cb_mv);
			newView.setTag(holder);
		}else{
			holder = (ViewHolder) newView.getTag();
		}
		
		holder.medicalHistoryTV.setText(curr.getMedicalHistoryID());
		holder.dateTV.setText(curr.getDate());
		
		if(curr.getSent()==1){
			holder.sentCB.setChecked(true);
		}else{
			holder.sentCB.setChecked(false);
		}
				
		return newView;
	}
	
	static class ViewHolder {
		TextView medicalHistoryTV;
		TextView dateTV;
		CheckBox sentCB;
		
	}
	
	public ArrayList<MeasurementRecord> getList(){
		return list;
	}
	
	public void add(MeasurementRecord listItem) {
		list.add(listItem);
		notifyDataSetChanged();
	}
	
	public void add(ArrayList<MeasurementRecord> events, Context mcontext){
		list = events;
		notifyDataSetChanged();
	}
	

}
