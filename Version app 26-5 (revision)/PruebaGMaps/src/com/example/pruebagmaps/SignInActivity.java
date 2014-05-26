package com.example.pruebagmaps;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class SignInActivity extends Activity  {
	
	private int signinstatus;

	/**
	 * The default email to populate the email field with.
	 */
	public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;

	// Values for email and password at the time of the login attempt.
	private String mUserName;
	private String mEmail;
	private String mPassword;
	private String mPasswordConf;
	private String mUserDescr;
	private double mLatitude;
	private double mLongitude;
	private String mUserType;
	
	HttpConnect post;
	
	String IP_Server="goingonapp.comuf.com";//IP DE NUESTRO SERVIDOR
    String URL_connect="http://"+IP_Server+"/adduser.php";//Ruta en donde estan nuestros archivos

	// UI references.
	private EditText mUserNameView;
	private EditText mUserDescriptionView;
	private EditText mUserMailView;
	private EditText mPasswordView;
	private EditText mPasswordConfirmView;
	private TextView mLoginStatusMessageView;
	
	Button bsigin;
	
	private ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_signup);
		post= new HttpConnect();
		
		// Set up the login form.
		
		mLatitude = 0.0;
		mLongitude = 0.0;
		mUserType = "mobile";
		
		mUserNameView = (EditText) findViewById(R.id.userName);
		mUserDescriptionView = (EditText) findViewById(R.id.userDescription);
		
		mEmail = getIntent().getStringExtra(EXTRA_EMAIL);
		mUserMailView = (EditText) findViewById(R.id.email);
		mUserMailView.setText(mEmail);

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordConfirmView = (EditText) findViewById(R.id.passwordConf);


		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptSigin();
					}
				});
		
		findViewById(R.id.button_cancel_sign_up).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						finish();
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptSigin() {
		if (mAuthTask != null) {
			return;
		}
		
		// Reset errors.
		mUserMailView.setError(null);
		mPasswordView.setError(null);
		mPasswordConfirmView.setError(null);
		mUserDescriptionView.setError(null);
		mUserNameView.setError(null);
		
		// Store values at the time of the login attempt.
		mEmail = mUserMailView.getText().toString();
		mPassword = mPasswordView.getText().toString();
		mPasswordConf = mPasswordConfirmView.getText().toString();
		mUserDescr = mUserDescriptionView.getText().toString();
		mUserName = mUserNameView.getText().toString();
		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 4) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		} else if (TextUtils.isEmpty(mPasswordConf)) {
			mPasswordConfirmView.setError(getString(R.string.error_field_required));
			focusView = mPasswordConfirmView;
			cancel = true;
		} else if (!mPasswordConf.equals(mPassword)) {
			mPasswordConfirmView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordConfirmView;
			cancel = true;
		} 

		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail)) {
			mUserMailView.setError(getString(R.string.error_field_required));
			focusView = mUserMailView;
			cancel = true;
		} else if (!mEmail.contains("@")) {
			mUserMailView.setError(getString(R.string.error_invalid_email));
			focusView = mUserMailView;
			cancel = true;
		}
		// Check for a valid user name.
		if (TextUtils.isEmpty(mUserName)) {
			mUserNameView.setError(getString(R.string.error_field_required));
			focusView = mUserNameView;
			cancel = true;
		}
		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mAuthTask = new UserLoginTask();
			mAuthTask.execute(mUserName, mEmail, mPassword, mUserDescr);
		}
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<String, String, Boolean> {
		
		String user,mail,pass,desc;
		
		@Override
		protected void onPreExecute() {
        	//para el progress dialog
            pDialog = new ProgressDialog(SignInActivity.this);
            pDialog.setMessage("Autenticando....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
		
		@Override
		protected Boolean doInBackground(String... params) {
			
			//obtnemos usr y pass
			user=params[0];
			mail=params[1];
			pass=params[2];
			desc=params[3];
			//enviamos y recibimos y analizamos los datos en segundo plano.
    		if (siginstatus(user,mail,pass,desc)==true){    		    		
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
				if (signinstatus==0){
					mPasswordView
					.setError(getString(R.string.error_incorrect_password));
					mPasswordView.requestFocus();
				}
				else if (signinstatus==2){
					Toast.makeText(getApplicationContext(), R.string.error_user_already_exists, Toast.LENGTH_LONG).show();
				}
			}
			
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
		}
		
	}
	
	//Valida el estado del logueo solamente necesita como parametros el usuario y passw
    public boolean siginstatus(String userName, String mail ,String password, String description ) {
    	signinstatus=-1;
    	
    	/*Creamos un ArrayList del tipo nombre valor para agregar los datos recibidos por los parametros anteriores
    	 * y enviarlo mediante POST a nuestro sistema para relizar la validacion */
    	ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
     		
    	postparameters2send.add(new BasicNameValuePair("usuario",userName));
		postparameters2send.add(new BasicNameValuePair("email",mail));
		postparameters2send.add(new BasicNameValuePair("password",password));
		postparameters2send.add(new BasicNameValuePair("descripcion",description));
		postparameters2send.add(new BasicNameValuePair("latitude",""+mLatitude));
		postparameters2send.add(new BasicNameValuePair("longitude",""+mLongitude));
		postparameters2send.add(new BasicNameValuePair("usertype",mUserType));

		//realizamos una peticion y como respuesta obtenes un array JSON
      	JSONArray jdata=post.getserverdata(postparameters2send, URL_connect);

      /*	como estamos trabajando de manera local el ida y vuelta sera casi inmediato
      	* para darle un poco realismo decimos que el proceso se pare por unos segundos para poder
      	* observar el progressdialog
      	* la podemos eliminar si queremos
      	*/
		
      	SystemClock.sleep(950);
		    		
      	System.out.println("El jdata devuelto en sign in es " +jdata);
		//si lo que obtuvimos no es null
		if (jdata!=null && jdata.length() > 0){

			JSONObject json_data; //creamos un objeto JSON
			try {
				json_data = jdata.getJSONObject(0); //leemos el primer segmento en nuestro caso el unico
				signinstatus=json_data.getInt("signinstatus");//accedemos al valor 
				Log.e("signinstatus","signinstatus= "+signinstatus);//muestro por log que obtuvimos
			} catch (JSONException e) {
				e.printStackTrace();
			}		            
			//validamos el valor obtenido
		    if (signinstatus==0){// [{"logstatus":"0"}] 
		    	 Log.e("signinstatus ", "invalido");
		    	 return false;
		    }
		    else if (signinstatus==2){// [{"logstatus":"2"}] 
		    	 Log.e("signinstatus ", "Ya existe usuario");
		    	 return false;
		    }
		    else{// [{"logstatus":"1"}]
		    	 Log.e("signinstatus ", "valido");
		    	 return true;
		    }
		    		 
		}else{	//json obtenido invalido verificar parte WEB.
			 Log.e("JSON  ", "ERROR");
			 return false;
		}
    }
	
	public void finishedAuth() {
		Intent intent = new Intent(this, MapActivity.class);
		System.out.println("userMail es " + mEmail);
		intent.putExtra("userEmail", mEmail);
		intent.putExtra("fbEventsInfo", "null");
		
		// Verify it resolves
		  PackageManager packageManager = getPackageManager();
		  List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
		  boolean isIntentSafe = activities.size() > 0;

		  // Start an activity if it's safe
		  if (isIntentSafe) {
		      startActivity(intent);}
	}
	
	public void onRadioButtonClicked(View view) {
	    // Is the button now checked?
	    boolean checked = ((RadioButton) view).isChecked();
	    
	    // Check which radio button was clicked
	    switch(view.getId()) {
	        case R.id.radioButton_mobile_user:
	            if (checked) {
	            	mLatitude = 0.0;
                    mLongitude = 0.0;
                    mUserType = "mobile";
	            break;}
	        case R.id.radioButton_local_user:
	            if (checked){
	            	mUserType = "local";
	            	GPSTracker gps = new GPSTracker(this);
	            	if(gps.canGetLocation()){ // gps enabled} // return boolean true/false
	            		mLatitude = gps.getLatitude();
	                    mLongitude = gps.getLongitude();
	                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + mLatitude + "\nLong: " + mLongitude, Toast.LENGTH_LONG).show();    
	                }else{
	                    // can't get location
	                    // GPS or Network is not enabled
	                    // Ask user to enable GPS/network in settings
	                    gps.showSettingsAlert();
	                } 	
	            break;}
	    }
	}
}
