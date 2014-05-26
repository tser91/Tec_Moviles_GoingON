package com.example.pruebagmaps;


import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
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
	
	private contextEventsMap eventsUser = null;

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
		
		ImageView im = (ImageView) findViewById(R.id.imageView_picture_user);
		Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_user_picture);
		im.setImageBitmap(getRoundedShape(bm));
		
		post= new HttpConnect();
		
		String userMail = getIntent().getExtras().getString("userEmail");
		
		eventsUser = (contextEventsMap) getIntent().getSerializableExtra("eventsInfo");
		
		user = new MobileUser(userMail);
		
		ListView lista = (ListView) findViewById(R.id.listaeventos);
      
		ArrayList<Event> arraydir = (ArrayList<Event>)eventsUser.getListEvents();

		System.out.println("Ev size "+arraydir.size());
		for (int i = 0; i<arraydir.size();i++){
			arraydir.get(i).event_Image = getResources().getDrawable(R.drawable.ic_concierto);
			System.out.println("Ev "+i+", is "+arraydir.get(i).event_Name);
		}
 
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
		 		t.setText(user.getUser_count_F()+"");
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
	
	public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
	    int targetWidth = 50;
	    int targetHeight = 50;
	    Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, 
	                        targetHeight,Bitmap.Config.ARGB_8888);

	    Canvas canvas = new Canvas(targetBitmap);
	    Path path = new Path();
	    path.addCircle(((float) targetWidth - 1) / 2,
	        ((float) targetHeight - 1) / 2,
	        (Math.min(((float) targetWidth), 
	        ((float) targetHeight)) / 2),
	        Path.Direction.CCW);

	    canvas.clipPath(path);
	    Bitmap sourceBitmap = scaleBitmapImage;
	    canvas.drawBitmap(sourceBitmap, 
	        new Rect(0, 0, sourceBitmap.getWidth(),
	        sourceBitmap.getHeight()), 
	        new Rect(0, 0, targetWidth, targetHeight), null);
	    return targetBitmap;
	}
	
	public void gotomap(MenuItem menu) {
		/*Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra("userEmail", user.getEmail());
    	startActivity(intent);*/
		finish();
	}
	
}
