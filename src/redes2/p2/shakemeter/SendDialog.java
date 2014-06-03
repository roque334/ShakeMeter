package redes2.p2.shakemeter;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SendDialog extends Dialog implements android.view.View.OnClickListener{
	
	Bundle args;
	
	EditText doctorID;
	Button sendButton;
	Button cancelButton;
	
	public SendDialog(Context context){
		super(context);
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
		if(v==sendButton){
			Toast.makeText(getContext(), "SendButton", Toast.LENGTH_LONG).show();
			this.dismiss();
		}
		
		if(v==cancelButton){
			Toast.makeText(getContext(), "CancelButton", Toast.LENGTH_LONG).show();
			this.dismiss();
		}
	}

}
