package com.example.pruebagmaps;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class AddEventActivity extends Activity {

	/**
	 * The default email to populate the email field with.
	 */
	public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private EventCreateTask mAuthTask = null;

	// Values for email and password at the time of the login attempt.
	private String mEventName;
	private String mEventDescr;
	private String mCategory;
	private String mEventPrice;
	private double mLatitude;
	private double mLongitude;
	private String mUserMail;
	
	
	HttpConnect post;
	
	String IP_Server="goingonapp.comuf.com";//IP DE NUESTRO SERVIDOR
    String URL_connect="http://"+IP_Server+"/addEvent.php";//Ruta en donde estan nuestros archivos

	// UI references.
	private EditText mEventNameView;
	private EditText mEventDescriptionView;
	private Spinner mEventCategorySpinner;
	private DatePicker mInitialDatePicker;
	private DatePicker mFinalDatePicker;
	private TimePicker mInitialTimePicker;
	private TimePicker mFinalTimePicker;
	private EditText mEventPriceView;
	
	private TextView mEventStatusMessageView;
	
	Button buttonCreateEvent;
	
	private ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_add_event);
		post= new HttpConnect();
		
		mUserMail = getIntent().getExtras().getString("userEmail");
				
		mLatitude = 0.0;
		mLongitude = 0.0;

		mEventPriceView = (EditText) findViewById(R.id.editText_event_price);		
		mEventNameView = (EditText) findViewById(R.id.editText_event_name);
		mEventDescriptionView = (EditText) findViewById(R.id.editText_event_descr);
		
		mEventCategorySpinner = (Spinner) findViewById(R.id.spinner_categories);
		mInitialDatePicker = (DatePicker) findViewById(R.id.datePicker_initial_date);
		mFinalDatePicker = (DatePicker) findViewById(R.id.datePicker_final_date);

		mInitialTimePicker = (TimePicker) findViewById(R.id.timePicker_initial_time);
		mFinalTimePicker = (TimePicker) findViewById(R.id.timePicker_final_time);
		
		mEventStatusMessageView = (TextView) findViewById(R.id.add_event_status_message);

		findViewById(R.id.button_create_event).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						if(getAddress()) {
							addEvent();
							}
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		//getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void addEvent() {
		if (mAuthTask != null) {
			return;
		}
		
		// Reset errors.
		mEventPriceView.setError(null);
		mEventNameView.setError(null);
		mEventDescriptionView.setError(null);
		
		// Store values at the time of the login attempt.
		mEventPrice = mEventPriceView.getText().toString();
		mEventName = mEventNameView.getText().toString();
		mEventDescr= mEventDescriptionView.getText().toString();	
		mCategory = mEventCategorySpinner.getSelectedItem().toString();

		
		boolean cancel = false;
		View focusView = null;
		
		// Check for a valid event name.
		if (TextUtils.isEmpty(mEventPrice)) {
			mEventPriceView.setError(getString(R.string.error_field_required));
			focusView = mEventPriceView;
			cancel = true;
		}

		// Check for a valid event name.
		if (TextUtils.isEmpty(mEventName)) {
			mEventNameView.setError(getString(R.string.error_field_required));
			focusView = mEventNameView;
			cancel = true;
		}
		
		// Check for a valid event description.
		if (TextUtils.isEmpty(mEventDescr)) {
			mEventDescriptionView.setError(getString(R.string.error_field_required));
			focusView = mEventDescriptionView;
			cancel = true;
		}
		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mEventStatusMessageView.setText(R.string.create_event_process);
			
			mAuthTask = new EventCreateTask();
			mAuthTask.execute(mEventName, mEventDescr, mEventPrice,mCategory, 
					mInitialDatePicker.getDayOfMonth() + "", mInitialDatePicker.getMonth() + "", mInitialDatePicker.getYear() + "",
					mInitialTimePicker.getCurrentHour()+"", mInitialTimePicker.getCurrentMinute()+"",
					mFinalDatePicker.getDayOfMonth() + "", mFinalDatePicker.getMonth() + "", mFinalDatePicker.getYear() + "",
					mFinalTimePicker.getCurrentHour()+"", mFinalTimePicker.getCurrentMinute()+"");
		}
	}
	
	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class EventCreateTask extends AsyncTask<String, String, Boolean> {
		
		String eventName,eventDescr, eventPrice, eventCategory,
			initDateDay, initDateMonth, initDateYear,
			initTimeHour, initTimeMinute,
			finalDateDay, finalDateMonth, finalDateYear,
			finalTimeHour, finalTimeMinute;
		
		@Override
		protected void onPreExecute() {
        	//para el progress dialog
            pDialog = new ProgressDialog(AddEventActivity.this);
            pDialog.setMessage("Creating Event....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
		
		@Override
		protected Boolean doInBackground(String... params) {
			//mInitialDate, mInitialTime, mFinalDate, mFinalTime
			//obtnemos usr y pass
			eventName=params[0];
			eventDescr=params[1];
			eventPrice=params[2];
			eventCategory=params[3];
			initDateDay=params[4];
			initDateMonth=params[5];
			initDateYear=params[6];
			initTimeHour=params[7];
			initTimeMinute=params[8];
			finalDateDay=params[9];
			finalDateMonth=params[10];
			finalDateYear=params[11];
			finalTimeHour=params[12];
			finalTimeMinute=params[13];
			
			//enviamos y recibimos y analizamos los datos en segundo plano.
    		if (createEventStatus(eventName,eventDescr, eventPrice, eventCategory,
    				initDateDay, initDateMonth, initDateYear,
    				initTimeHour, initTimeMinute,
    				finalDateDay, finalDateMonth, finalDateYear,
    				finalTimeHour, finalTimeMinute)==true){    		    		
    			return true; //login valido
    		}else{    		
    			return false; //login invalido     	          	  
    		}
		}
	

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;

			if (success) {
				finishedAuth();
				
			} else {
				pDialog.dismiss();
				mEventNameView
						.setError(getString(R.string.error_event_not_valid));
				mEventNameView.requestFocus();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
		}
	}
	
	//Valida el estado del logueo solamente necesita como parametros el usuario y passw
    public boolean createEventStatus(String eventName,String eventDescr, String eventPrice, String eventCategory,
    		String initDateDay, String initDateMonth, String initDateYear,
    		String initTimeHour, String initTimeMinute,
    		String finalDateDay, String finalDateMonth, String finalDateYear,
    		String finalTimeHour, String finalTimeMinute ) {
    	int logstatus=-1;
    	
    	/*Creamos un ArrayList del tipo nombre valor para agregar los datos recibidos por los parametros anteriores
    	 * y enviarlo mediante POST a nuestro sistema para relizar la validacion */
    	ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
     		
    	postparameters2send.add(new BasicNameValuePair("eventName",eventName));
		postparameters2send.add(new BasicNameValuePair("eventDescr",eventDescr));
		postparameters2send.add(new BasicNameValuePair("eventPrice",eventPrice));
		postparameters2send.add(new BasicNameValuePair("eventCategory",eventCategory));
		postparameters2send.add(new BasicNameValuePair("initDateDay",initDateDay));
		postparameters2send.add(new BasicNameValuePair("initDateMonth",initDateMonth));
		postparameters2send.add(new BasicNameValuePair("initDateYear",initDateYear));
		postparameters2send.add(new BasicNameValuePair("initTimeHour",initTimeHour));
		postparameters2send.add(new BasicNameValuePair("initTimeMinute",initTimeMinute));
		postparameters2send.add(new BasicNameValuePair("finalDateDay",finalDateDay));
		postparameters2send.add(new BasicNameValuePair("finalDateMonth",finalDateMonth));
		postparameters2send.add(new BasicNameValuePair("finalDateYear",finalDateYear));
		postparameters2send.add(new BasicNameValuePair("finalTimeHour",finalTimeHour));
		postparameters2send.add(new BasicNameValuePair("finalTimeMinute",finalTimeMinute));
		postparameters2send.add(new BasicNameValuePair("latitude",mLatitude+""));
		postparameters2send.add(new BasicNameValuePair("longitude",mLongitude+""));
		postparameters2send.add(new BasicNameValuePair("usermail",mUserMail));

		//realizamos una peticion y como respuesta obtenes un array JSON
      	JSONArray jdata=post.getserverdata(postparameters2send, URL_connect);

       /*	como estamos trabajando de manera local el ida y vuelta sera casi inmediato
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
				logstatus=json_data.getInt("logstatus");//accedemos al valor 
				Log.e("siginstatus","logstatus= "+logstatus);//muestro por log que obtuvimos
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		            
		    System.out.println("El valor del logstatus es " + logstatus);
			//validamos el valor obtenido
		    if (logstatus==0){// [{"logstatus":"0"}] 
		    	 Log.e("siginstatus ", "invalido");
		    	 return false;
		    }
		    else{// [{"logstatus":"1"}]
		    	 Log.e("siginstatus ", "valido");
		    	 return true;
		    }
		    		 
		}else{	//json obtenido invalido verificar parte WEB.
			 Log.e("JSON  ", "ERROR");
			 return false;
		}
    }
	
	public void finishedAuth() {
		Intent intent = new Intent(this, MapActivity.class);
		intent.putExtra("userMail", mUserMail);
		// Verify it resolves
		  PackageManager packageManager = getPackageManager();
		  List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
		  boolean isIntentSafe = activities.size() > 0;

		  // Start an activity if it's safe
		  if (isIntentSafe) {
		      startActivity(intent);}
	}

public boolean getAddress() {
        Geocoder geocoder= new Geocoder(this, Locale.ENGLISH);
        try {
               
        		EditText dir = (EditText)findViewById(R.id.editText_event_location);
              //Place your latitude and longitude
              List<Address> addresses = geocoder.getFromLocationName(dir.getText().toString(), 1);
              
              if(addresses.size() != 0) {
               
                  Address fetchedAddress = addresses.get(0);
                  mLatitude = fetchedAddress.getLatitude();
                  mLongitude = fetchedAddress.getLongitude();
                
                  Toast.makeText(getApplicationContext(),"Latitude: " +mLatitude+ ", Longitude: " +mLongitude,
                		  Toast.LENGTH_LONG).show();
                  return true;
              }
               
              else{
            	  dir.setError(getString(R.string.error_no_location_found));
            	  dir.requestFocus();
            	  return false;
              }          
        } 
        catch (IOException e) {
                 // TODO Auto-generated catch block
                 e.printStackTrace();
                 System.out.println(e);
                 Toast.makeText(getApplicationContext(),"Could not get address..!", Toast.LENGTH_LONG).show();
                 return false;
        }
    }
}
