package com.example.pruebagmaps;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

/**
 * Activity which displays profile event, including name, description, price, category and
 * information about the amount of fans.
 */
public class EventProfile extends Activity {

	private Event eventObj;
	private String mEventName;
	private String mEventDescr;
	private String mCategory;
	private String mEventPrice;
	private double mLatitude;
	private double mLongitude;
	private String mInitialDate;
	private String mInitialTime;

	HttpConnect post;

	String IP_Server="goingonapp.comuf.com";//IP DE NUESTRO SERVIDOR
	String URL_connect="http://"+IP_Server+"/getEventInfo.php";//Ruta en donde estan nuestros archivos

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile_event);

		post= new HttpConnect();

		eventObj = (Event) getIntent().getSerializableExtra("eventInfo");

		findViewById(R.id.profile_event_getDirection).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						gotoWaze();
					}
				});
		getInfo();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user, menu);
		return true;
	}

	private void updateUI() {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {

				TextView t = (TextView)findViewById(R.id.profile_event_name);
				t.setText(mEventName);

				t = (TextView)findViewById(R.id.profile_event_description);
				t.setText(mEventDescr);

				t =(TextView)findViewById(R.id.profile_event_price);
				t.setText(mEventPrice);

				t =(TextView)findViewById(R.id.profile_event_category);
				t.setText(mCategory);

				t =(TextView)findViewById(R.id.profile_event_date);
				t.setText(mInitialDate);

				t =(TextView)findViewById(R.id.profile_event_time);
				t.setText(mInitialTime);

				/*
		 		ImageView i = (ImageView)findViewById(R.id.imageView_picture_user);
		 		i.setImageBitmap(user.getImage());
		 		*/
			}
		});
	}

	/*Valida el estado del logueo solamente necesita como parametros el usuario y passw*/
	public boolean getInfo() {
		mEventName = eventObj.getNombre();
		mEventDescr = eventObj.getDescripcion();
		mCategory = eventObj.getCategory();
		mEventPrice = eventObj.getPrice();
		mLatitude = eventObj.getLatitude();
		mLongitude = eventObj.getLongitude();
		mInitialDate = eventObj.getInitialDate();
		mInitialTime = eventObj.getFinalDate();
		updateUI();
		return true;
	}

	public void gotoWaze(){
		try
		{
			String url = "waze://?q=" + mLatitude + "," + mLongitude;
			Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( url ) );
			startActivity( intent );
		}
		catch ( ActivityNotFoundException ex  )
		{
			Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( "market://details?id=com.waze" ) );
			startActivity(intent);
		}		
	}

	public void gotomap(MenuItem menu) {
		/*Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra("userEmail", user.getEmail());
    	startActivity(intent);*/
		finish();
	}

}