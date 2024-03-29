package com.example.pruebagmaps;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.DownloadManager.Request;
import android.content.Intent;
import android.os.Bundle;
import android.service.textservice.SpellCheckerService.Session;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.HttpMethod;
import com.facebook.Response;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

public class FbLoginFragment extends Fragment {
	
	private static final String TAG = "FbLoginFragment";
	private static final String FB_LOGIN = "fblogin";
	
	private JSONArray events = null;
	
	private UiLifecycleHelper uiHelper;
	
	@Override
	public void onResume() {
	    super.onResume();
	 // For scenarios where the main activity is launched and user
	    // session is not null, the session state change notification
	    // may not be triggered. Trigger it if it's open/closed.
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
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    uiHelper = new UiLifecycleHelper(getActivity(), callback);
	    uiHelper.onCreate(savedInstanceState);
	}

	private Session.StatusCallback callback = new Session.StatusCallback() {
	    @Override
	    public void call(Session session, SessionState state, Exception exception) {
	        onSessionStateChange(session, state, exception);
	    }
	};
	
	private void onSessionStateChange(final Session session, SessionState state, Exception exception) {
	    if (state.isOpened()) {
	        Log.i(TAG, "Logged in...");
            getEvents(session);

	        // Request user data and show the results
	        Request.newMeRequest(session, new Request.GraphUserCallback() {

	            @Override
	            public void onCompleted(GraphUser user, Response response) {
	                if (user != null) {
						// Display the parsed user info
	                    System.out.println("" + buildUserInfoDisplay(user, session));
	                    String userMail = user.getProperty("email").toString();
	                    String userName = user.getName();
	                    LoginUserActivity activity = (LoginUserActivity) getActivity();
	            	    System.out.println("events es "+events);

	                    activity.setFbEventsInfo(events);
	                    activity.executeTaskFb(userMail, userName, FB_LOGIN);
	                }
	            }
	        }).executeAsync();
	        
	       
	    } else if (state.isClosed()) {
	    	System.out.println("" + exception);
	        Log.i(TAG, "Logged out...");
	    }
	}
	
	private void getEvents(Session session) {
		new Request(
	    	    session,
	    	    "/me/events",
	    	    null,
	    	    HttpMethod.GET,
	    	    new Request.Callback() {
	    	        public void onCompleted(Response response) {
	    	        	parseEventFromFQLResponse(response);
	    	        }
	    	    }
	    	).executeAsync();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, 
	        ViewGroup container, 
	        Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.fragment_fb_login, container, false);
	    LoginButton authButton = (LoginButton) view.findViewById(R.id.authButton);
	    authButton.setFragment(this);
	    //permissions
	    List<String> permissions = new ArrayList<String>();
	    permissions.add("basic_info");
	    permissions.add("email");
	    permissions.add("user_events");
	    authButton.setReadPermissions(permissions);
	    
	    return view;
	}
	
	private String buildUserInfoDisplay(GraphUser user, Session session) {
	    StringBuilder userInfo = new StringBuilder("");

	    // Example: typed access (name)
	    // - no special permissions required
	    userInfo.append(String.format("Name: %s\n\n", 
	        user.getName()));

	    // Example: typed access (birthday)
	    // - requires user_birthday permission
	    userInfo.append(String.format("Birthday: %s\n\n", 
	        user.getBirthday()));

	    // Example: partially typed access, to location field,
	    // name key (location)
	    // - requires user_location permission
	    userInfo.append(String.format("Location: %s\n\n", 
	        user.getLocation().getProperty("name")));

	    // Example: access via property name (locale)
	    // - no special permissions required
	    userInfo.append(String.format("Locale: %s\n\n", 
	        user.getProperty("locale")));
	    
	 // Example: access via property name (email)
	    // - Email special permissions required
	    userInfo.append(String.format("Email: %s\n\n", 
	        user.getProperty("email")));

	    // Example: access via key for array (languages) 
	    // - requires user_likes permission
	    JSONArray languages = (JSONArray)user.getProperty("languages");
	    if (languages.length() > 0) {
	        ArrayList<String> languageNames = new ArrayList<String> ();
	        for (int i=0; i < languages.length(); i++) {
	            JSONObject language = languages.optJSONObject(i);
	            // Add the language name to a list. Use JSON
	            // methods to get access to the name field. 
	            languageNames.add(language.optString("name"));
	        }           
	        userInfo.append(String.format("Languages: %s\n\n", 
	        languageNames.toString()));
	    }
	    
	    return userInfo.toString();
	}
	
	/************************************************************************
	*   Parses the user data from the facebook FQL
	*
	*   "SELECT uid, name, pic_square FROM user WHERE uid IN ( SELECT uid2 FROM friend WHERE uid1 = me() )"
	*
	*   This is the example query from
	*   {@link http://developers.facebook.com/docs/howtos/androidsdk/3.0/run-fql-queries/}
	*
	*   @param  response    The facebook response from the FQL
	************************************************************************/
	public final void parseEventFromFQLResponse( Response response )
	{
	    try
	    {
	        GraphObject go  = response.getGraphObject();
	        JSONObject  jso = go.getInnerJSONObject();
	        JSONArray arr = jso.getJSONArray( "data" );;
	        /*for ( int i = 0; i < ( arr.length() ); i++ )
	        {
	            JSONObject json_obj = arr.getJSONObject( i );

	            String id     = json_obj.getString( "id"           );
	            String startTime   = json_obj.getString( "start_time"          );
	            String rsvpStatus   = json_obj.getString( "rsvp_status"          );
	            String location = json_obj.getString( "location"    );
	            String endTime = json_obj.getString( "end_time"    );
	            String name = json_obj.getString( "name"    );
	            
	            System.out.println("Id: "+id+", StartTime: "+startTime+", rsvp: "+rsvpStatus
	            		+", location: "+location+", endTime: "+endTime+", name: "+name+".\n");
	        }*/
	        events = arr;
	    }
	    catch ( Throwable t )
	    {
	        t.printStackTrace();
	    }
	}
	
}

