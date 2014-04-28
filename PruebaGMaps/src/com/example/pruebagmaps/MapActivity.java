package com.example.pruebagmaps;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.Session;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends Activity{

	private GoogleMap map;
	private String userEmail;
	private JSONArray fbEventsInfo;
	private int logstatus;
	private int typeUser;

	private contextEventsMap eventsMap;

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private GetEventsTask mAuthTask = null;

	HttpConnect post;


	String IP_Server="goingonapp.comuf.com";//IP DE NUESTRO SERVIDOR
	String URL_connect="http://"+IP_Server+"/getEventList.php";//Ruta en donde estan nuestros archivos


	private ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		map.setMyLocationEnabled(true);

		post= new HttpConnect();

		eventsMap = new contextEventsMap();

		userEmail = getIntent().getExtras().getString("userEmail");

		try {
			String obj = getIntent().getExtras().getString("fbEventsInfo");
			if (!obj.equals("null") && obj!= null) {
				fbEventsInfo = new JSONArray(getIntent().getExtras().getString("fbEventsInfo"));
				System.out.println("Json array dta es: "+fbEventsInfo);
				System.out.println("Json array length es: "+fbEventsInfo.length());
				for ( int i = 0; i < ( fbEventsInfo.length() ); i++ )
				{
					JSONObject json_obj = fbEventsInfo.getJSONObject( i );

					String id     = json_obj.getString( "id"           );
					String startTime   = json_obj.getString( "start_time"          );
					String rsvpStatus   = json_obj.getString( "rsvp_status"          );
					String location = json_obj.getString( "location"    );
					String endTime = json_obj.getString( "end_time"    );
					String name = json_obj.getString( "name"    );

					System.out.println("Id: "+id+", StartTime: "+startTime+", rsvp: "+rsvpStatus
							+", location: "+location+", endTime: "+endTime+", name: "+name+".\n");

					/*
					Event tempEvent = new Event(name, descr, id, latitude, longitude);
					eventsMap.insertEvent(tempEvent);
					 */
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			System.out.println(e);
		}

		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();

		Location myLocation = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
		if (myLocation != null)
		{
			centerMapOnMyLocation(myLocation); 
		}

		updateEventsInfo();


		map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
			@Override
			public void onInfoWindowClick(final Marker marker) {

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MapActivity.this);

				// set title
				alertDialogBuilder.setTitle("Event's information");

				// set dialog message
				alertDialogBuilder
				.setMessage("Go check event's info?")
				.setCancelable(false)
				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						goToEventProfile(marker.getTitle());
					}

				})
				.setNegativeButton("No",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						dialog.dismiss();
					}
				});

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();
			}

			/*
            	Toast.makeText(getBaseContext(), 
        			    "Info Window clicked@" + marker.getId(), 
        			    Toast.LENGTH_SHORT).show();

            	LayoutInflater layoutInflater 
        		= (LayoutInflater)getBaseContext()
        		.getSystemService(LAYOUT_INFLATER_SERVICE);  
        		View popupView = layoutInflater.inflate(R.layout.popup_event, null);  
        		final PopupWindow popupWindow = new PopupWindow(
        				popupView, 
        				LayoutParams.WRAP_CONTENT,  
        				LayoutParams.WRAP_CONTENT);  

        				Button btnGetDirection = (Button)popupView.findViewById(R.id.getDirection);
        				btnGetDirection.setOnClickListener(new Button.OnClickListener(){
        					@Override
        					public void onClick(View v) {
        						goToWaze(marker.getPosition());
        					}});
        				Button btnGetEventInfo = (Button)popupView.findViewById(R.id.getEventInfo);
        				btnGetDirection.setOnClickListener(new Button.OnClickListener(){
        					@Override
        					public void onClick(View v) {
        						//goToWaze(marker.getPosition());
        					}});

        			popupWindow.setAnimationStyle(R.style.Animation);
        		popupWindow.showAtLocation(popupView, Gravity.BOTTOM, 0, 0);
        			popupWindow.setFocusable(true);
        			popupWindow.update();*/
		});

	}	
	
	private void goToEventProfile(String title) {
		Intent intent = new Intent(this, EventProfile.class);
		intent.putExtra("userEmail", userEmail);
		intent.putExtra("eventsInfo", eventsMap);
		intent.putExtra("fbEventsInfo", getIntent().getExtras().getString("fbEventsInfo"));
		intent.putExtra("eventName", title);
		startActivity(intent);  
		
	}

	public void updateEventsInfo() {
		mAuthTask  = new GetEventsTask();
		mAuthTask.execute();
	}

	///		-------------------------Zooming camera to position user-----------------
	private void centerMapOnMyLocation(Location location) {

		map.animateCamera(CameraUpdateFactory.newLatLngZoom(
				new LatLng(location.getLatitude(), location.getLongitude()), 13));

		CameraPosition cameraPosition = new CameraPosition.Builder()
		.target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
		.zoom(17)                   // Sets the zoom
		.bearing(90)                // Sets the orientation of the camera to east
		.tilt(40)                   // Sets the tilt of the camera to 30 degrees
		.build();                   // Creates a CameraPosition from the builder
		map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

	}
	/////----------------------------------Zooming camera to position user-----------------


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}

	public void gotoAddEvent(MenuItem item) {
		Intent intent = new Intent(this, AddEventActivity.class);
		intent.putExtra("userEmail", userEmail);
		intent.putExtra("eventsInfo", eventsMap);
		intent.putExtra("fbEventsInfo", getIntent().getExtras().getString("fbEventsInfo"));
		startActivity(intent);
	}

	public void gotoProfileUser(MenuItem item){ 
		if (typeUser == 3){
			Intent intent = new Intent(this, ProfileMobileUserActivity.class);
			intent.putExtra("userEmail", userEmail);
			intent.putExtra("eventsInfo", eventsMap);
			intent.putExtra("fbEventsInfo", getIntent().getExtras().getString("fbEventsInfo"));
			startActivity(intent);    
		}
		else if (typeUser == 2){
			Intent intent = new Intent(this, ProfileLocalUserActivity.class);
			intent.putExtra("userEmail", userEmail);
			intent.putExtra("eventsInfo", eventsMap);
			intent.putExtra("fbEventsInfo", getIntent().getExtras().getString("fbEventsInfo"));
			startActivity(intent);  
		}
	}
	
	public void logOut(MenuItem item){
		if (Session.getActiveSession() != null) {
		    Session.getActiveSession().closeAndClearTokenInformation();
		}

		Session.setActiveSession(null);
		
		Intent intent = new Intent(this, LoginUserActivity.class);

		// Verify it resolves
		  PackageManager packageManager = getPackageManager();
		  List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
		  boolean isIntentSafe = activities.size() > 0;

		  // Start an activity if it's safe
		  if (isIntentSafe) {
		      startActivity(intent);}
		
	}


	public class GetEventsTask extends AsyncTask<String, String, Boolean> {

		@Override
		protected void onPreExecute() {
			//para el progress dialog
			pDialog = new ProgressDialog(MapActivity.this);
			pDialog.setMessage(getString(R.string.add_event_progress));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			//enviamos y recibimos y analizamos los datos en segundo plano.
			return getEventStatus();
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			pDialog.dismiss();			
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
		}
	}


	//Valida el estado del logueo solamente necesita como parametros el usuario y passw
	@SuppressLint("UseValueOf")
	public boolean getEventStatus() {

		logstatus=-1;

		//Creamos un ArrayList del tipo nombre valor para agregar los datos recibidos por los parametros anteriores
		// y enviarlo mediante POST a nuestro sistema para relizar la validacion
		ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();

		postparameters2send.add(new BasicNameValuePair("email",userEmail));

		//realizamos una peticion y como respuesta obtenes un array JSON
		JSONArray jdata=post.getserverdata(postparameters2send, URL_connect);
		System.out.println("jdata es " + jdata);
		SystemClock.sleep(950);

		//si lo que obtuvimos no es null
		if (jdata!=null && jdata.length() > 0){

			JSONObject json_data; //creamos un objeto JSON
			try {
				json_data = jdata.getJSONObject(0); //leemos el primer segmento en nuestro caso el unico
				logstatus=json_data.getInt("getEventStatus");//accedemos al valor 
				System.out.println("getEventStatus es " + logstatus);
				Log.e("getEventStatus","getEventStatus= "+logstatus);//muestro por log que obtuvimos
				typeUser = json_data.getInt("typeUser");
				Log.e("typeUser","typeUser= "+typeUser);//muestro por log que obtuvimos
			} catch (JSONException e) {
				e.printStackTrace();
			}		            

			//validamos el valor obtenido
			if (logstatus==0){// [{"logstatus":"0"}] 
				Log.e("getEventStatus ", "invalido");
				Toast.makeText(getApplicationContext(), R.string.error_user_info_unreachable, Toast.LENGTH_LONG).show();
				return false;
			}
			else{// [{"logstatus":"1"}]
				Log.e("getEventStatus ", "valido");
				try {
					json_data = jdata.getJSONObject(0); //leemos el primer segmento en nuestro caso el unico
					JSONArray events = json_data.getJSONArray("events");
					for (int i =0; i<events.length();i++){
						JSONObject currentEvent = events.getJSONObject(i);
						String name = (String)currentEvent.get("name");
						String descr = (String)currentEvent.get("description");
						int id = Integer.valueOf(currentEvent.get("idEvento").toString());
						double latitude = Double.valueOf(currentEvent.get("latitude").toString());
						double longitude = Double.valueOf(currentEvent.get("longitude").toString());
						System.out.println("Name is "+name+", descr is "+descr+", id is "+id+", latitude is "+latitude+", longitude is "+longitude);
						Event tempEvent = new Event(name, descr, id, latitude, longitude);
						eventsMap.insertEvent(tempEvent);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				updateUI();
				return true;
			}

		}else{	//json obtenido invalido verificar parte WEB.
			Log.e("JSON  ", "ERROR");
			return false;
		}
	}

	private void updateUI() {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i<eventsMap.getListEvents().size();i++){
					Event event = eventsMap.getListEvents().get(i);
					map.addMarker(new MarkerOptions()
					.position(new LatLng(event.getLatitude(), event.getLongitude()))
					.title(event.getNombre())
					.snippet(event.getDescripcion()));
				}
			}
		});    
	}
	
	@Override
	public void onBackPressed() {

	}
}
