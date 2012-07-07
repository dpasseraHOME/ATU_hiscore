package com.smashingboxes.ATUHiscore;

import android.app.Activity;
import android.os.Bundle;

public class InstructionsActivity extends Activity {

	final String LOG_TAG = "log_InstructionsActivity";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.instructions_layout);
	}
	
}
