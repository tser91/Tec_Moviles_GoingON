package com.example.pruebagmaps;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends Activity {
	
	private GoogleMap map;
	private String userEmail;
	private int logstatus;
	//private TextView mGetEventsMessageView;
	
	private contextEventsMap eventsMap;
	
	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	//private GetEventsTask mAuthTask = null;
	
	HttpConnect post;
	
	/*
	String IP_Server="goingonapp.comuf.com";//IP DE NUESTRO SERVIDOR
    String URL_connect="http://"+IP_Server+"/getEventList.php";//Ruta en donde estan nuestros archivos
    */
    
    private ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
				setContentView(R.layout.activity_map);
		 map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
			        .getMap();
	    map.setMyLocationEnabled(true);
	    
	    eventsMap = new contextEventsMap();
	    
	   // mGetEventsMessageView = (TextView) findViewById(R.id.add_event_status_message);
	    
	    userEmail = getIntent().getExtras().getString("userEmail");
	    
	    Event evento1;
	    Event evento2;
	    
        // Introduzco los datos para pruebas
        
        evento1 = new Event(getResources().getDrawable(R.drawable.ic_concierto), "DJ Tiesto", "Costa Rica presenta al mejor DJ del mundo");
        evento2 = new Event(getResources().getDrawable(R.drawable.ic_cafeteria), "Capuccinos 2x1", "Disfrute de un delicioso capuccino con la mejor compania");

        
	    OnMapLongClickListener listener = new OnMapLongClickListener() {
			
	    	@Override
			public void onMapLongClick(LatLng point) {
				try
				{
				   String url = "waze://?q=" + point.latitude + "," + point.longitude;
				   Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( url ) );
				   startActivity( intent );
				}
				catch ( ActivityNotFoundException ex  )
				{
				  Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( "market://details?id=com.waze" ) );
				  startActivity(intent);
				}
			}
		};
		
	    map.setOnMapLongClickListener(listener);
	    
	    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        Location myLocation = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (myLocation != null)
        {
        	map.addMarker(new MarkerOptions()
	        .position(new LatLng(40.7527371, -73.9786022))
	        .title("" + evento1.getNombre() ));
        	
        	map.addMarker(new MarkerOptions()
	        .position(new LatLng(12.1278622,-86.2650596))
	        .title("" + evento2.getNombre() ));
        	
        	centerMapOnMyLocation(myLocation); 
        	
        	//mGetEventsMessageView.setText(R.string.add_event_progress);
        	//mAuthTask  = new GetEventsTask();
    		//mAuthTask.execute();
        }
        
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
    	startActivity(intent);
	}
	
	public void gotoProfileUser(MenuItem item){      
        Intent intent = new Intent(this, ProfileMobileUserActivity.class);
        intent.putExtra("userEmail", userEmail);
    	startActivity(intent);
	}
	
	/*
public class GetEventsTask extends AsyncTask<String, String, Boolean> {
				
		@Override
		protected void onPreExecute() {
        	//para el progress dialog
            pDialog = new ProgressDialog(MapActivity.this);
            pDialog.setMessage("Getting events info....");
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
*/
	/*
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

	
  	SystemClock.sleep(950);
	    		
	//si lo que obtuvimos no es null
	if (jdata!=null && jdata.length() > 0){

		JSONObject json_data; //creamos un objeto JSON
		try {
			json_data = jdata.getJSONObject(0); //leemos el primer segmento en nuestro caso el unico
			logstatus=json_data.getInt("getEventStatus");//accedemos al valor 
			Log.e("getEventStatus","getEventStatus= "+logstatus);//muestro por log que obtuvimos
		} catch (JSONException e) {
			// TODO Auto-generated catch block
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
		 			JSONArray events = json_data.getJSONArray("event");
		 			for (int i =0; i<events.length();i++){
		 				JSONObject currentEvent = events.getJSONObject(i);
		 				String name = (String)currentEvent.get("name");
		 				String descr = (String)currentEvent.get("description");
		 				int id = Integer.valueOf(currentEvent.get("id").toString());
		 				double latitude = Double.valueOf(currentEvent.get("latitude").toString());
		 				double longitude = Double.valueOf(currentEvent.get("longitude").toString());
		 				eventsMap.insertEvent(new Event(name, descr, id, latitude, longitude));
		 			}
		 		} catch (JSONException e) {
		 			// TODO Auto-generated catch block
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
	
}*/

}
