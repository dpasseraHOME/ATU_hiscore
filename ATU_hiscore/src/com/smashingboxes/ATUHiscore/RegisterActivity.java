package com.smashingboxes.ATUHiscore;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
		} else if(((EditText)findViewById(R.id.register_field_email_confirm)).length() == 0) {
			isValid = false;
			//TODO display empty field error
			Log.v(LOG_TAG, "## email confirm field empty");
		} else if(!email.equals(emailConfirm)) {
			isValid = false;
			//TODO display email validation error
			Log.v(LOG_TAG, "## email confirm does not match email");
			Log.v(LOG_TAG, "@@ " + email + " <> " + emailConfirm);
		}
		
		Log.v(LOG_TAG, "@ isValid = " + isValid);
		return isValid;
	}
	
	private void submitForm() {
		Log.v(LOG_TAG, "# submitForm");
		
		String pName = ((EditText)findViewById(R.id.register_field_name)).getText().toString();
		String pEmail = ((EditText)findViewById(R.id.register_field_email)).getText().toString();
		String pPIN = ((EditText)findViewById(R.id.register_field_pin)).getText().toString();
		
		String result = "";
		InputStream inputstream = null;
		
		// data to send
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("action", "register"));
		nameValuePairs.add(new BasicNameValuePair("name", pName));
		nameValuePairs.add(new BasicNameValuePair("email", pEmail));
		nameValuePairs.add(new BasicNameValuePair("PIN", pPIN));
		
		
		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://www.monkeydriver.com/atu/site.php");
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			inputstream = entity.getContent();
		} catch(Exception e) {
			Log.v("log_post","Error in http connection " + e.toString());
		}
		
		// convert http post response to string
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputstream,"iso-8859-1"),8);
			StringBuilder stringbuilder = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null ) {
				stringbuilder.append(line + "\n");
			}
			inputstream.close();
			result = stringbuilder.toString();
		} catch(Exception e) {
			Log.v("log_post", "Error converting result " + e.toString());
		}
		
		// parse json data
		try {
			JSONArray jArray = new JSONArray(result);
			for(int i=0; i<jArray.length(); i++) {
				JSONObject jsonData = jArray.getJSONObject(i);
				Log.v("log_post","isSuccess: " + jsonData.getString("isSuccess") + ", " + "msg: " + jsonData.getString("msg"));
			}
		} catch(JSONException e) {
			Log.v("log_post", "Error parsing data " + e.toString());
		}
	}
	
	private OnClickListener onButtonClicked = new OnClickListener() {
		
		public void onClick(View v) {
			int id= v.getId();
			
			if(id == R.id.register_button_submit) {
				if(validateForm()) {
					submitForm();
				} else {
					
				}
			} else {
				launchMainActivity();
			}
		}
		
	};
	
}
