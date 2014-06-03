package redes2.p2.shakemeter;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateControlF2ViewActivity extends Activity {
	
	Bundle args;
	
	EditText gereralSymptonsLevelEditText;
	
	EditText muscularSymptonLevelEditText;
	
	Button nextButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form_2_view);
		
		 args = getIntent().getExtras().getBundle("Bundle");
		
		gereralSymptonsLevelEditText = (EditText) findViewById(R.id.general_sypmtons_et_f2);
		muscularSymptonLevelEditText = (EditText) findViewById(R.id.muscular_sypmtons_et_f2);
		nextButton = (Button) findViewById(R.id.next_b_f2);
		
		nextButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent createControlF3ViewActivity;
				
				int generalSymptonLevel;
				int muscularSymptonLevel;
				
				if((gereralSymptonsLevelEditText.length() != 0) &&
						muscularSymptonLevelEditText.length() != 0){
					generalSymptonLevel = Integer.parseInt(gereralSymptonsLevelEditText.getText().toString());
					muscularSymptonLevel = Integer.parseInt(muscularSymptonLevelEditText.getText().toString());
					
					if ((generalSymptonLevel>-1) && (generalSymptonLevel<11) &&
							(muscularSymptonLevel>-1) && (muscularSymptonLevel<11)){
						args.putString(DatabaseOpenHelper.GENERAL_SYMPTONS_LEVEL, String.valueOf(generalSymptonLevel));
						args.putString(DatabaseOpenHelper.MUSCULAR_SYMPTONS_LEVEL, String.valueOf(muscularSymptonLevel));
						
						createControlF3ViewActivity = new Intent(getApplicationContext(), CreateControlF3ViewActivity.class);
						createControlF3ViewActivity.putExtra("Bundle", args);
						
						startActivity(createControlF3ViewActivity);
					}else{
						Toast.makeText(getApplicationContext(), "Debe colocar enteros entre 0 y 10", Toast.LENGTH_LONG).show();
					}
					
				}else{
					Toast.makeText(getApplicationContext(), "Debe llenar todos los campos para poder avanzar.", Toast.LENGTH_LONG).show();
				}	
			}
		});
		
		
		
		
	}

}
