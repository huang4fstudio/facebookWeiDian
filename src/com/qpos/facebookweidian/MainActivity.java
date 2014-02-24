package com.qpos.facebookweidian;
/*
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
*/


import android.os.Bundle;
// import android.app.Activity;
// import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {

	private boolean loggedin;
	
	private FbFragment loginFragment;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState == null) {

	        // Add the fragment on initial activity setup
	        loginFragment = new FbFragment();
	        getSupportFragmentManager()
	        .beginTransaction()
	        .add(android.R.id.content, loginFragment)
	        .commit();
	        
	    } else {
	        // Or set the fragment from restored state info
	        loginFragment = (FbFragment) getSupportFragmentManager()
	        .findFragmentById(android.R.id.content);
	        
	    }
		loggedin = loginFragment.getLoginStatus();
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	//Custom Methods Below:
	
	public void authRegisteration(String userId){
		// Implement code that authorizes User ID from the main server, the following part is to be modified according to server-side App requirements
		/* boolean registered;
		String adr = getResources().getString(R.string.main_server_address); //IP Address of the Server for future Application use
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(adr);
		
		
	    try {
	        // Add your data
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
	        nameValuePairs.add(new BasicNameValuePair("id", userId));
	        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	        // Execute HTTP Post Request
	        HttpResponse response = httpClient.execute(httpPost);
	        InputStream is = response.getEntity().getContent();
	        BufferedReader r = new BufferedReader(new InputStreamReader(is));
	        String result = r.readLine();
	        
	        if(result.equals("TRUE"))
	        	registered = true;
	        else
	        	registered = false;

	        
	    } catch (ClientProtocolException e) {
	        // TODO Auto-generated catch block
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	    }
	    */
		loggedin = true; 
		setupWeiDianFragment(true, userId); // True or False depends on whether the user has registered, should use setupWeidianFragment(registered, userId instead)
		
	}
	
	public void logout(){
		loggedin = false;
		// logging out the WeiDian
	}
	
	
	private void setupWeiDianFragment(boolean registered, String userId){
		TextView mainText2;
		mainText2 = (TextView) findViewById(R.id.mainTextView);
	    if(loggedin == true){
	    	if(registered && userId != null){
			mainText2.setText(userId + "is Registered, Welcome!");// sets up the WeiDianFragment
		}
		
		else{
	
			mainText2.setText(userId + "is not Registered.");// complete the registration process
		}
	    }
	}

}
