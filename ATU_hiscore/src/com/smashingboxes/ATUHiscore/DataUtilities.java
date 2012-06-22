package com.smashingboxes.ATUHiscore;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class DataUtilities {
	
	private final static String LOG_TAG = "log_DataUtilities";
	private final static Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile("[a-zA-Z0-9+._%-+]{1,256}" +
            "@" +
            "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" +
            "(" +
            "." +
            "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" +
            ")+");

	public static JSONObject post(String urlStr, ArrayList<NameValuePair> nameValuePairs) {
		JSONObject jsonData = null;
		
		String result = "";
		InputStream inputstream = null;		
		
		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(urlStr);
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			inputstream = entity.getContent();
		} catch(Exception e) {
			Log.v(LOG_TAG,"Error in http connection " + e.toString());
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
			Log.v(LOG_TAG, "Error converting result " + e.toString());
		}
		
		// parse json data
		try {
			jsonData = new JSONObject(result);
			Log.v(LOG_TAG, "isSucces: " + jsonData.getString("isSuccess"));
		} catch(JSONException e) {
			Log.v(LOG_TAG, "Error parsing data " + e.toString());
		}
		
		return jsonData;
	}
	
	public static String md5EncryptString(String str) throws Exception {
		String password = "tinybaby17mountain!!";
		
		MessageDigest m = MessageDigest.getInstance("MD5");
		m.update(password.getBytes("UTF8"));
		byte s[] = m.digest();
		
		String result = "";
		
		for(int i=0; i<s.length; i++) {
			result += Integer.toHexString((0x000000ff & s[i]) | 0xffffff00).substring(6);
		}
		
		Log.v(LOG_TAG, "@ result = " + result);
		
		return result;
	}
	
	public static Boolean isEmailValid(String email) {		
		 /*Pattern p = Pattern.compile(" (?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|"
	              +"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")"
	                     + "@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\]");
		*/
		
		 Matcher m = EMAIL_ADDRESS_PATTERN.matcher(email);
		
		Boolean isMatchFound = m.matches();
		
		return isMatchFound;
	}
	
}
