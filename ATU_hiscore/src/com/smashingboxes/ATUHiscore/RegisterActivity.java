package com.smashingboxes.ATUHiscore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.smashingboxes.utilities.ManageSharedPrefs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends Activity {
	
	final String LOG_TAG = "log_RegisterActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_layout);
		
		Button buttonSubmit = (Button) findViewById(R.id.register_button_submit);
		buttonSubmit.setOnClickListener(onButtonClicked);
		
		Button buttonCancel = (Button) findViewById(R.id.register_button_cancel);
		buttonCancel.setOnClickListener(onButtonClicked);
	}
	
	private void launchMainActivity() {
		Intent mainIntent = new Intent(getApplicationContext(), ATU_hiscoreActivity.class);
		startActivity(mainIntent);
	}
	
	private Boolean validateForm() {
		Log.v(LOG_TAG, "# validateForm");
		
		Boolean isValid = true;
		
		//check for empty name and PIN fields
		if(((EditText)findViewById(R.id.register_field_name)).length() == 0) {
			isValid = false;
			//TODO display empty field error
			Log.v(LOG_TAG, "## name field empty");
		}
		if(((EditText)findViewById(R.id.register_field_pin)).length() == 0) {
			isValid = false;
			//TODO display empty field error
			Log.v(LOG_TAG, "## pin field empty");
		}
		
		String email = ((EditText)findViewById(R.id.register_field_email)).getText().toString();
		String emailConfirm = ((EditText)findViewById(R.id.register_field_email_confirm)).getText().toString();
		
		//check for empty email and email_confirm fields before testing email - email_confirm match
		if(((EditText)findViewById(R.id.register_field_email)).length() == 0) {
			isValid = false;
			//TODO display empty field error
			Log.v(LOG_TAG, "## email field empty");
		}
		
		if(!DataUtilities.isEmailValid(email)) {
			isValid = false;
			//TODO display empty field error
			Log.v(LOG_TAG, "## email is invalid");
		}
		
		if(((EditText)findViewById(R.id.register_field_email_confirm)).length() == 0) {
			isValid = false;
			//TODO display empty field error
			Log.v(LOG_TAG, "## email confirm field empty");
		}
		
		if(!email.equals(emailConfirm)) {
			isValid = false;
			//TODO display email validation error
			Log.v(LOG_TAG, "## email confirm does not match email");
			Log.v(LOG_TAG, "@@ " + email + " <> " + emailConfirm);
		}
		
		Log.v(LOG_TAG, "@ isValid = " + isValid);
		return isValid;
	}
	
	private void submitForm() throws Exception {
		Log.v(LOG_TAG, "# submitForm");
		
		String pName = ((EditText)findViewById(R.id.register_field_name)).getText().toString();
		String pEmail = ((EditText)findViewById(R.id.register_field_email)).getText().toString();
		String pPIN = DataUtilities.md5EncryptString(((EditText)findViewById(R.id.register_field_pin)).getText().toString());
		
		
		// data to send
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("action", "register"));
		nameValuePairs.add(new BasicNameValuePair("name", pName));
		nameValuePairs.add(new BasicNameValuePair("email", pEmail));
		nameValuePairs.add(new BasicNameValuePair("PIN", pPIN));
		
		JSONObject jsonData = DataUtilities.post("http://www.monkeydriver.com/atu/site.php", nameValuePairs);
		//Log.v(LOG_TAG,"@ jsonData = " + jsonData);
		//Log.v(LOG_TAG,"@ jsonData.isSuccess = " + jsonData.getString("isSuccess"));
		
		// add user info to shared preferences
		Map<String,String> map = new HashMap<String,String>();
		map.put("hasSharedPrefs", "yes");
		map.put("name",jsonData.getString("name"));
		map.put("email", jsonData.getString("email"));
		map.put("PIN", jsonData.getString("PIN"));
		
		ManageSharedPrefs.setPreferences(getApplicationContext(), map);
		//Log.v(LOG_TAG,"sp.name = " + ManageSharedPrefs.getPreference(getApplicationContext(), "name"));
	}
	
	private OnClickListener onButtonClicked = new OnClickListener() {
		
		public void onClick(View v) {
			int id= v.getId();
			
			if(id == R.id.register_button_submit) {
				if(validateForm()) {
					try {
						submitForm();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					
				}
			} else {
				launchMainActivity();
			}
		}
		
	};
	
}
