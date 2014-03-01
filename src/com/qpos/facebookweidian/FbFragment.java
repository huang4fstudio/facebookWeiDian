package com.qpos.facebookweidian;

import java.util.Arrays;

import com.facebook.*;
import com.facebook.model.*;
import com.facebook.widget.*;

import android.support.v4.app.Fragment;
// import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.os.Bundle;

public class FbFragment extends Fragment {
	
	
	private String userId = null;
	private boolean loggedin = false;
	// private static final String TAG = "FbFragment";
	private UiLifecycleHelper uiHelper; // Facebook UiLifecycle Helper method that handles the Fragment's life cycle.
	
	public boolean getLoginStatus(){
		return loggedin;
	}
	/**
	 * Session.StatusCallback is the class that handles whenever a Facebook Session changes due to a change in user authentication, it 
	 * calls back with the Active Session, state and possibly exception.
	 */
	private Session.StatusCallback callback = new Session.StatusCallback() {
	    @Override
	    public void call(Session session, SessionState state, Exception exception) {
	        onSessionStateChange(session, state, exception);
	        loggedin = true;
	    }

	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.activity_main, container, false);
		LoginButton authButton = (LoginButton) view.findViewById(R.id.authButton);
		authButton.setReadPermissions(Arrays.asList("basic_info"));
		authButton.setFragment(this);
		authButton.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback(){

		    @Override
		    public void onUserInfoFetched(GraphUser user) {
		      
		        if (user != null) {
		        	((MainActivity)getActivity()).authRegisteration(user.getId());
		        }
		    }
		});

		 Session session = Session.getActiveSession();
		    if (session != null && session.isOpened()) {
		        // Get the user's data
		        parseUserID(session);
		    }
		    
	    return view;
	}

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	   
	    uiHelper = new UiLifecycleHelper(getActivity(), callback);
	    uiHelper.onCreate(savedInstanceState);
	}
	
	
	@Override
	public void onResume() {
	    super.onResume();
	    Session session = Session.getActiveSession();
	    session.addCallback(callback);
	    if (session != null &&
	           (session.isOpened() || session.isClosed()) ) {
	        onSessionStateChange(session, session.getState(), null);
	    }

	    uiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    uiHelper.onActivityResult(requestCode, resultCode, data);
	  
	    
	}

	@Override
	public void onPause() {
	    super.onPause();
	    uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    uiHelper.onSaveInstanceState(outState);
	}
	

	/**
	 * This part handles the Change in authentication that is called by statusCallBack, this is the main part where the UI Changes.
	 * @param session Active Facebook Session
	 * @param state State of Active Facebook Session, i.e. Logged in/Logged Out
	 * @param exception Exception of the Session
	 */
	
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
		
		
		if (session != null && session.isOpened()) {
			loggedin = true;
			parseUserID(session);
	        // Log.i(TAG, "Logged in...");
	    } else if (session != null && session.isClosed()) {
	    	 loggedin = false;
	    	 ((MainActivity)getActivity()).logout();
	    	// Log.i(TAG, "Logged out...");
	       
	    }
	}
	
	private void parseUserID(final Session session){
		Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {

			  // callback after Graph API response with user object
			  @Override
			  public void onCompleted(GraphUser user, Response response) {
				  if (session == Session.getActiveSession()){
				  if (user != null) {
			     userId = user.getId();
			    }
				 
			  }
			  }

			});
			request.executeAsync();
		if(userId != null)
		 ((MainActivity)getActivity()).authRegisteration(userId);
	}
	
	

}
