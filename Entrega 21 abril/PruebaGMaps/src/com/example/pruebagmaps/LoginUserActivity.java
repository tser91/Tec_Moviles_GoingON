package com.example.pruebagmaps;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginUserActivity extends FragmentActivity  {
	
	private FbLoginFragment fbLoginFragment;
	
	private int logstatus;
	
	/**
	 * The default email to populate the email field with.
	 */
	public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";
	public static final String GO_LOGIN = "gologin";

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;

	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private String mPassword;
	
	HttpConnect post;
	
	String IP_Server="goingonapp.comuf.com";//IP DE NUESTRO SERVIDOR
    String URL_connect="http://"+IP_Server+"/acces.php";//Ruta en donde estan nuestros archivos

	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;
	private TextView mLoginStatusMessageView;
	
	private ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);
				
		if (savedInstanceState == null) {
	        // Add the fragment on initial activity setup
			fbLoginFragment = new FbLoginFragment();
	        getSupportFragmentManager()
	        .beginTransaction()
	        .add(android.R.id.content, fbLoginFragment)
	        .commit(); 
	    } else {
	        // Or set the fragment from restored state info
	    	fbLoginFragment = (FbLoginFragment) getSupportFragmentManager()
	        .findFragmentById(android.R.id.content);
	    }
		
		post= new HttpConnect();
		
		// Set up the login form.
		mEmail = getIntent().getStringExtra(EXTRA_EMAIL);
		mEmailView = (EditText) findViewById(R.id.email);
		mEmailView.setText(mEmail);

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});
		
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		findViewById(R.id.log_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();
					}
				});
		
		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						gotoSignIn();
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
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();

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
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		} else if (!mEmail.contains("@")) {
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			executeTaskFb(mEmail, mPassword, GO_LOGIN);
		}
	}

	/**
	 * Starts the authenticaton process
	 * @param mMail
	 * @param mPass
	 * @param typeLogin
	 */
	public void executeTaskFb(String mMail, String mPass, String typeLogin) {
		mEmail = mMail;
		mAuthTask  = new UserLoginTask();
		mAuthTask.execute(mMail, mPass, typeLogin);
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<String, String, Boolean> {
		
		String mail,pass,typeLogin;
		
		@Override
		protected void onPreExecute() {
        	//para el progress dialog
            pDialog = new ProgressDialog(LoginUserActivity.this);
            pDialog.setMessage("Autenticando....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
		
		@Override
		protected Boolean doInBackground(String... params) {

			//obtnemos usr y pass
			mail=params[0];
			pass=params[1];
			typeLogin=params[2];
            
			//enviamos y recibimos y analizamos los datos en segundo plano.
    		if (loginstatus(mail,pass,typeLogin)==true){    		    		
    			return true; //login valido
    		}else{    		
    			return false; //login invalido     	          	  
    		}
		}
	

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			pDialog.dismiss();
			if (success) {
				finishedAuth();
			} else {
				if (logstatus==0){
					mPasswordView
					.setError(getString(R.string.error_incorrect_password));
					mPasswordView.requestFocus();
				}
				else if (logstatus==2){
					Toast.makeText(getApplicationContext(), R.string.error_user_doesnt_exist, Toast.LENGTH_LONG).show();
				}
			}
			
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
		}
		
	}
	
	/*Valida el estado del logueo solamente necesita como parametros el usuario y passw*/
    public boolean loginstatus(String mail ,String password, String typeLogin) {
    	
    	logstatus=-1;
    	
    	/*Creamos un ArrayList del tipo nombre valor para agregar los datos recibidos por los parametros anteriores
    	 * y enviarlo mediante POST a nuestro sistema para relizar la validacion*/ 
    	ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();

		postparameters2send.add(new BasicNameValuePair("email",mail));
		postparameters2send.add(new BasicNameValuePair("password",password));
		postparameters2send.add(new BasicNameValuePair("typeLogin",typeLogin));

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
				logstatus=json_data.getInt("logstatus");//accedemos al valor 
				Log.e("loginstatus","logstatus= "+logstatus);//muestro por log que obtuvimos
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		            
		             
			//validamos el valor obtenido
		    if (logstatus==0){// [{"logstatus":"0"}] 
		    	 Log.e("loginstatus ", "invalido");
		    	 return false;
		    }
		    else if (logstatus==2){
		    	 Log.e("loginstatus ", "doesntExist");
		    	return false;
		    }
		    else{// [{"logstatus":"1"}]
		    	 Log.e("loginstatus ", "valido");
		    	 return true;
		    }
		    		 
		}else{	//json obtenido invalido verificar parte WEB.
			 Log.e("JSON  ", "ERROR");
			 return false;
		}
    }
	
	public void finishedAuth() {
		Intent intent = new Intent(this, MapActivity.class);
		
		intent.putExtra("userEmail", mEmail);
		
		// Verify it resolves
		  PackageManager packageManager = getPackageManager();
		  List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
		  boolean isIntentSafe = activities.size() > 0;

		  // Start an activity if it's safe
		  if (isIntentSafe) {
		      startActivity(intent);}
	}
	
	public void gotoSignIn(){
		Intent intent = new Intent(this, SignInActivity.class );
		
		// Verify it resolves
		  PackageManager packageManager = getPackageManager();
		  List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
		  boolean isIntentSafe = activities.size() > 0;

		  // Start an activity if it's safe
		  if (isIntentSafe) {
		      startActivity(intent);}
	}
}
