package com.smashingboxes.ATUHiscore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.smashingboxes.utilities.ManageSharedPrefs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends Activity {
	
	final String LOG_TAG = "log_RegisterActivity";
	final Context context = this;

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
		
		//reset fields
		EditText fieldName = ((EditText)findViewById(R.id.register_field_name));
		EditText fieldEmail = ((EditText)findViewById(R.id.register_field_email));
		EditText fieldEmailConfirm = ((EditText)findViewById(R.id.register_field_email_confirm));
		EditText fieldPin = ((EditText)findViewById(R.id.register_field_pin));
		
		fieldName.setBackgroundResource(R.drawable.edit_text_good);
		fieldEmail.setBackgroundResource(R.drawable.edit_text_good);
		fieldEmailConfirm.setBackgroundResource(R.drawable.edit_text_good);
		fieldPin.setBackgroundResource(R.drawable.edit_text_good);
		
		
		//check for empty name and PIN fields
		if(fieldName.length() == 0) {
			isValid = false;
			
			//display empty field error
			fieldName.setBackgroundResource(R.drawable.edit_text_error);			
			Log.v(LOG_TAG, "## name field empty");
			
		}
		if(fieldPin.length() == 0) {
			isValid = false;
			//TODO display empty field error
			fieldPin.setBackgroundResource(R.drawable.edit_text_error);
			Log.v(LOG_TAG, "## pin field empty");
		}
		
		String email = fieldEmail.getText().toString();
		String emailConfirm = fieldEmailConfirm.getText().toString();
		
		//check for empty email and email_confirm fields before testing email - email_confirm match
		if(fieldEmail.length() == 0) {
			isValid = false;
			//TODO display empty field error
			fieldEmail.setBackgroundResource(R.drawable.edit_text_error);
			Log.v(LOG_TAG, "## email field empty");
		}
		
		if(!DataUtilities.isEmailValid(email)) {
			isValid = false;
			//TODO display empty field error
			fieldEmail.setBackgroundResource(R.drawable.edit_text_error);
			Log.v(LOG_TAG, "## email is invalid");
		}
		
		if(fieldEmailConfirm.length() == 0) {
			isValid = false;
			//TODO display empty field error
			Log.v(LOG_TAG, "## email confirm field empty");
		}
		
		if(!email.equals(emailConfirm)) {
			isValid = false;
			//TODO display email validation error
			fieldEmailConfirm.setBackgroundResource(R.drawable.edit_text_error);
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
		Log.v(LOG_TAG,"@ jsonData = " + jsonData);
		Log.v(LOG_TAG,"@ jsonData.isSuccess = " + jsonData.getString("isSuccess"));
		
		if(jsonData.getString("isSuccess").equals("yes")) {
			Log.v(LOG_TAG,"successful registration");
			// add user info to shared preferences
			Map<String,String> map = new HashMap<String,String>();
			map.put("hasSharedPrefs", "yes");
			map.put("name",jsonData.getString("name"));
			map.put("email", jsonData.getString("email"));
			map.put("PIN", jsonData.getString("PIN"));
		
			ManageSharedPrefs.setPreferences(getApplicationContext(), map);
			//Log.v(LOG_TAG,"sp.name = " + ManageSharedPrefs.getPreference(getApplicationContext(), "name"));
		} else {
			Log.v(LOG_TAG,"unsuccessful registration");
			String errors = "";
			if(jsonData.getString("msg").indexOf("duplicate name") > -1) {
				errors += "\nDisplay name already registered";
			}
			if(jsonData.getString("msg").indexOf("duplicate email") > -1) {
				errors += "\nEmail address already in use";
			}
			
			new AlertDialog.Builder( this )
			.setTitle( "Registration error" )
			.setMessage( "You could not be registered due to the following errors:\n"+errors+"\n\nPlease try again." )
			.setPositiveButton( "Okay", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			})
			.show();
		}
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
					new AlertDialog.Builder(context)
					.setTitle( "Form error" )
					.setMessage( "Please correct the highlighted fields." )
					.setPositiveButton( "Okay", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					})
					.show();
				}
			} else {
				launchMainActivity();
			}
		}
		
	};
	
}
