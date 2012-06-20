package com.smashingboxes.ATUHiscore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ATU_hiscoreActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        checkForUserData(false);
    }
    
    private void checkForUserData(boolean doRealCheck) {
    	if(doRealCheck) {
    		
    	} else {
    		activateButtons();
    	}
    }
    
    private void activateButtons() {
    	Button buttonRegister = (Button) findViewById(R.id.button_register);
    	buttonRegister.setOnClickListener(onButtonClicked);
    	
    	Button buttonSignIn = (Button) findViewById(R.id.button_signIn);
    	buttonRegister.setOnClickListener(onButtonClicked);
    }
    
    private void launchHomeActivity() {
    	Intent homeIntent = new Intent(getApplicationContext(), HomeActivity.class);
    	startActivity(homeIntent);
    }
    
    private void launchRegisterActivity() {
    	Intent registerIntent = new Intent(getApplicationContext(), RegisterActivity.class);
    	startActivity(registerIntent);
    }
    
    private void launchSignInActivity() {
    	Intent signInIntent = new Intent(getApplicationContext(), SignInActivity.class);
    	startActivity(signInIntent);
    }
    
    private OnClickListener onButtonClicked = new OnClickListener() {
		
		public void onClick(View v) {
			int id = v.getId();
			
			if(id == R.id.button_register) {
				launchRegisterActivity();
			} else {
				launchSignInActivity();
			}
		}
	};
    
}