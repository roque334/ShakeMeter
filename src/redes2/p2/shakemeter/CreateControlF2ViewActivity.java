package redes2.p2.shakemeter;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.NumberPicker;

public class CreateControlF2ViewActivity extends Activity {
	
	Bundle args;
	
	NumberPicker gereralSymptonsLevelNumberPicker;
	
	NumberPicker muscularSymptonLevelNumberPicker;
	
	Button nextButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form_2_view);
		
		 args = getIntent().getExtras().getBundle("Bundle");
		
		gereralSymptonsLevelNumberPicker = (NumberPicker) findViewById(R.id.general_sypmtons_np_f2);
		muscularSymptonLevelNumberPicker = (NumberPicker) findViewById(R.id.muscular_sypmtons_np_f2);
		nextButton = (Button) findViewById(R.id.next_b_f2);
		
		gereralSymptonsLevelNumberPicker.setMinValue(0);
		muscularSymptonLevelNumberPicker.setMinValue(0);
		gereralSymptonsLevelNumberPicker.setMaxValue(10);
		muscularSymptonLevelNumberPicker.setMaxValue(10);
		
		nextButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent createControlF3ViewActivity;
				
				String generalSymptonLevel;
				String muscularSymptonLevel;
				
				generalSymptonLevel = "" + gereralSymptonsLevelNumberPicker.getValue() + "";
				muscularSymptonLevel = "" + muscularSymptonLevelNumberPicker.getValue() + "";
				
				args.putString(DatabaseOpenHelper.GENERAL_SYMPTONS_LEVEL, generalSymptonLevel);
				args.putString(DatabaseOpenHelper.MUSCULAR_SYMPTONS_LEVEL, muscularSymptonLevel);
				
				createControlF3ViewActivity = new Intent(getApplicationContext(), CreateControlF3ViewActivity.class);
				createControlF3ViewActivity.putExtra("Bundle", args);
				
				startActivity(createControlF3ViewActivity);
				
				
			}
		});
		
		
		
		
	}

}
