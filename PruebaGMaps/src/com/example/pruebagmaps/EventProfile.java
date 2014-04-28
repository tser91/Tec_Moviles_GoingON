package com.example.pruebagmaps;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
/**
 * Activity which displays profile event, including name, description, price, category and
 * information about the amount of fans.
 */
public class EventProfile extends Activity {
	
	private String idEvent;
	private String mEventName;
	private String mEventDescr;
	private String mCategory;
	private String mEventPrice;
	private double mLatitude;
	private double mLongitude;
	private String mInitialDate;
	private String mInitialTime;
	private EventInfoTask infoTask;
	
	private ProgressDialog pDialog;

	HttpConnect post;
	
	String IP_Server="goingonapp.comuf.com";//IP DE NUESTRO SERVIDOR
    String URL_connect="http://"+IP_Server+"/getEventInfo.php";//Ruta en donde estan nuestros archivos
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile_event);
		
		post= new HttpConnect();
		
		//idEvent = getIntent().getExtras().getString("idEvent"); eventName
		idEvent = getIntent().getExtras().getString("eventName"); 
		
		findViewById(R.id.profile_event_getDirection).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						gotoWaze();
						
					}
				});
		
		infoTask = new EventInfoTask();
		infoTask.execute();
	}
	
	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class EventInfoTask extends AsyncTask<String, String, Boolean> {
		
		@Override
		protected void onPreExecute() {
        	//para el progress dialog
            pDialog = new ProgressDialog(EventProfile.this);
            pDialog.setMessage("Getting event info....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
				
		@Override
		protected Boolean doInBackground(String... params) {
	        return getInfo();
		}
	
		@Override
		protected void onPostExecute(final Boolean success) {
			infoTask = null;
			pDialog.dismiss();
		}
	
		@Override
		protected void onCancelled() {
			infoTask = null;
		}
		
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
		 		i.setImageBitmap(user.getImage());*/

		    }
		});
		
	}
	

/*Valida el estado del logueo solamente necesita como parametros el usuario y passw*/
public boolean getInfo() {
	
	/*Creamos un ArrayList del tipo nombre valor para agregar los datos recibidos por los parametros anteriores
	 * y enviarlo mediante POST a nuestro sistema para relizar la validacion*/ 
	ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
    System.out.println("User idEvent is: " + idEvent);
	postparameters2send.add(new BasicNameValuePair("EventName", idEvent));
	//realizamos una peticion y como respuesta obtenes un array JSON
  	JSONArray jdata=post.getserverdata(postparameters2send, URL_connect);
  	/*como estamos trabajando de manera local el ida y vuelta sera casi inmediato
  	* para darle un poco realismo decimos que el proceso se pare por unos segundos para poder
  	* observar el progressdialog
  	* la podemos eliminar si queremos
  	*/
	
  	SystemClock.sleep(950);
	    		
	//si lo que obtuvimos no es null
	if (jdata!=null && jdata.length() > 0){

		JSONObject json_data; //creamos un objeto JSON
	    	 try {
	 			json_data = jdata.getJSONObject(0); //leemos el primer segmento en nuestro caso el unico
	 			mEventName = json_data.getString("name");//accedemos al valor 
	 			mEventDescr = json_data.getString("description");//accedemos al valor 
	 			mCategory= json_data.getString("category");
	 			mEventPrice = json_data.getString("price");
	 			mLatitude = json_data.getDouble("latitude");
	 			mLongitude = json_data.getDouble("longitude");
	 			mInitialDate = json_data.getString("initialdate");
	 			mInitialTime = json_data.getString("initialtime");
	 		} catch (JSONException e) {
	 			// TODO Auto-generated catch block
	 			e.printStackTrace();
	 		}
	    	 updateUI();
	    	 return true;
	    
	    		 
	}else{	//json obtenido invalido verificar parte WEB.
		 Log.e("JSON  ", "ERROR");
		 return false;
	}
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