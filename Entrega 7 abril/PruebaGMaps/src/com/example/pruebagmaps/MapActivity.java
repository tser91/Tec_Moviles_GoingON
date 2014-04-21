package com.example.pruebagmaps;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends Activity {
	
	private GoogleMap map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
				setContentView(R.layout.activity_map);
		 map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
			        .getMap();
	    map.setMyLocationEnabled(true);
	    
	    
	    Evento evento1;
	    Evento evento2;
	    
        // Introduzco los datos para pruebas
        
        evento1 = new Evento(getResources().getDrawable(R.drawable.ic_concierto), "DJ Tiesto", "Costa Rica presenta al mejor DJ del mundo");
        evento2 = new Evento(getResources().getDrawable(R.drawable.ic_cafeteria), "Capuccinos 2x1", "Disfrute de un delicioso capuccino con la mejor compania");

        
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
	        .position(new LatLng(myLocation.getLatitude() + 1.0, myLocation.getLongitude() + 1.0))
	        .title("" + evento1.getNombre() ));
        	
        	map.addMarker(new MarkerOptions()
	        .position(new LatLng(myLocation.getLatitude() - 1.0, myLocation.getLongitude() - 1.0))
	        .title("" + evento2.getNombre() ));
        	
        	centerMapOnMyLocation(myLocation); 
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
	
	public void gotoProfileUser(MenuItem item){
		Intent i = new Intent(this, ProfileUser.class );
        startActivity(i);
	}

}
