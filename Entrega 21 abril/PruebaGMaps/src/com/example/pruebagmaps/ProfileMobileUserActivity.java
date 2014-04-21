package com.example.pruebagmaps;


import java.util.ArrayList;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity which displays profile user, including name, description, events, picture and
 * information about the amount of friends and checkins.
 */
public class ProfileMobileUserActivity extends Activity {
	
	private UserLoginTask mAuthTask = null;
	
	private ProgressDialog pDialog;

	/**
	 * Mobile User instance
	 */	
	private MobileUser user;
	private int logstatus;
	
	HttpConnect post;
	
	String IP_Server="goingonapp.comuf.com";//IP DE NUESTRO SERVIDOR
    String URL_connect="http://"+IP_Server+"/getMobileUserInfo.php";//Ruta en donde estan nuestros archivos
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile_user_mobile);
		
		post= new HttpConnect();
		
		String userMail = getIntent().getExtras().getString("userEmail");
		
		user = new MobileUser(userMail);
		
		ListView lista = (ListView) findViewById(R.id.listaeventos);
        ArrayList<Event> arraydir = new ArrayList<Event>();
        Event evento;
 
        // Introduzco los datos
        
        evento = new Event(getResources().getDrawable(R.drawable.ic_concierto), "DJ Tiesto", "Costa Rica presenta al mejor DJ del mundo");
        arraydir.add(evento);
        evento = new Event(getResources().getDrawable(R.drawable.ic_cafeteria), "Capuccinos 2x1", "Disfrute de un delicioso capuccino con la mejor compania");
        arraydir.add(evento);
        evento = new Event(getResources().getDrawable(R.drawable.ic_discoteca), "Black Night", "Venga y viva la mejor noche vistiendo de negro");
        arraydir.add(evento);

 
        // Creo el adapter personalizado
        AdapterEventos adapter = new AdapterEventos(this, arraydir);
 
        // Lo aplico
        lista.setAdapter(adapter);
        
        mAuthTask  = new UserLoginTask();
		mAuthTask.execute();
        
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

		    	 TextView t = (TextView)findViewById(R.id.textView_name_user);
		 		t.setText(user.getName());
		 		
		 		t = (TextView)findViewById(R.id.textView_description_user);
		 		t.setText(user.getDescription());
		 		
		 		t =(TextView)findViewById(R.id.textView_amount_friends);
		 		t.setText("+" + user.getUser_count_F() + " friends");
		 		/*
		 		ImageView i = (ImageView)findViewById(R.id.imageView_picture_user);
		 		i.setImageBitmap(user.getImage());*/

		    }
		});
		
	}
	

/*Valida el estado del logueo solamente necesita como parametros el usuario y passw*/
public boolean getInfo() {
	
	logstatus=-1;
	
	/*Creamos un ArrayList del tipo nombre valor para agregar los datos recibidos por los parametros anteriores
	 * y enviarlo mediante POST a nuestro sistema para relizar la validacion*/ 
	ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
    System.out.println("User mail is: " + user.getEmail());
	postparameters2send.add(new BasicNameValuePair("email",user.getEmail()));
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
			logstatus=json_data.getInt("getstatus");//accedemos al valor 
			Log.e("mUserInfoStatus","logstatus= "+logstatus);//muestro por log que obtuvimos
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		            
	             
		//validamos el valor obtenido
	    if (logstatus==0){// [{"logstatus":"0"}] 
	    	 Log.e("getstatus ", "invalido");
			 Toast.makeText(getApplicationContext(), R.string.error_user_info_unreachable, Toast.LENGTH_LONG).show();
	    	 return false;
	    }
	    else{// [{"logstatus":"1"}]
	    	 Log.e("getstatus ", "valido");
	    	 try {
	 			json_data = jdata.getJSONObject(0); //leemos el primer segmento en nuestro caso el unico
	 			user.setName(json_data.getString("name"));//accedemos al valor 
	 			user.setDescription(json_data.getString("description"));//accedemos al valor 
	 			user.setUser_count_F(json_data.getInt("countfriends"));
	 			//user.setImage(json_data.getString("description"));//accedemos al valor 
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

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<String, String, Boolean> {
		
		@Override
		protected void onPreExecute() {
        	//para el progress dialog
            pDialog = new ProgressDialog(ProfileMobileUserActivity.this);
            pDialog.setMessage("Getting user info....");
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
			mAuthTask = null;
			pDialog.dismiss();
		}
	
		@Override
		protected void onCancelled() {
			mAuthTask = null;
		}
		
	}
	
	public void gotomap(MenuItem menu) {
		Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra("userEmail", user.getEmail());
    	startActivity(intent);
	}
	
}
