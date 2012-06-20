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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class PHPPostUtility {

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
			/*
			JSONArray jArray = new JSONArray(result);
			for(int i=0; i<jArray.length(); i++) {
				jsonData = jArray.getJSONObject(i);
				Log.v("log_post","isSuccess: " + jsonData.getString("isSuccess") + ", " + "msg: " + jsonData.getString("msg"));
			}
			*/
			jsonData = new JSONObject(result);
			Log.v("log_post", "isSucces: " + jsonData.getString("isSuccess"));
		} catch(JSONException e) {
			Log.v("log_post", "Error parsing data " + e.toString());
		}
		
		return jsonData;
	}
	
}
