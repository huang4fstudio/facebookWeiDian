package com.qpos.facebookweidian;

import java.util.Arrays;

import com.facebook.*;
import com.facebook.model.*;
import com.facebook.widget.LoginButton;

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
		
		
		if (state.isOpened()) {
			loggedin = true;
			Request.newMeRequest(session, new Request.GraphUserCallback() {

				  // callback after Graph API response with user object
				  @Override
				  public void onCompleted(GraphUser user, Response response) {
				    if (user != null) {
				     userId = user.getId();
				    }
				  }
				}).executeAsync();
			if(userId != null)
			 ((MainActivity)getActivity()).authRegisteration(userId);
	        // Log.i(TAG, "Logged in...");
	    } else if (state.isClosed()) {
	    	 loggedin = false;
	    	 ((MainActivity)getActivity()).logout();
	    	// Log.i(TAG, "Logged out...");
	       
	    }
	}
	
	

}
