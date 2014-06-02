package redes2.p2.shakemeter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

public class SendDialog extends DialogFragment {
	
	Context mContext;
	
	public SendDialog(){
		mContext = getActivity();
	}
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreateDialog(savedInstanceState);
		
		Bundle mArgs = getArguments();
 
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
 
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
 
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.send_dialog, null))
                // Add action buttons
                .setPositiveButton(R.string.send,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            	//TODO:
                            	//if the device has wifi or internet conection
                            	//send the measure to the server.
                            	//if the device has not wifi or internet conection
                            	//raise a Toast indicating that at that moment the app
                            	//can not establish a conection with the server.
                            	SendDialog.this.dismiss();
                            }
                        })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SendDialog.this.dismiss();
                    }
                });
 
        // Create the AlertDialog object and return it
        return builder.create();
    }

}
