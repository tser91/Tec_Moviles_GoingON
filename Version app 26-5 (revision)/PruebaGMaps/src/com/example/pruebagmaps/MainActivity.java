package com.example.pruebagmaps;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
//import com.microsoft.windowsazure.mobileservices.*;


public class MainActivity extends Activity {

	//private MobileServiceClient mClient;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
/*
			try {
				mClient = new MobileServiceClient( "https://goingonapp.azure-mobile.net/", "mNJvAeTOzWCaeRTvzOtkrCBCmeKVEq42", this );
			} catch (MalformedURLException e) {
				System.out.println("se cayo aca :O");
				e.printStackTrace();
			}
			*/

		//Remove title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		//Remove notification bar
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_main);
				//insertTest();
		new Timer().schedule(new TimerTask(){
			public void run() { 

				startActivity(new Intent(MainActivity.this, LoginUserActivity.class));
			}
		}, 1000); 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public void onBackPressed() {

	}
/*
	public void insertTest() {
		usuario item = new usuario();
		item.setCorreo("mi correo");
		item.setDescripcion("mi descripcion");
		item.setIdtipousuario(2);
		item.setNombre("sergio saborio");
		item.setIdImagen(1);
		item.setPassword("password");
		mClient.getTable(usuario.class).insert(item,
				new TableOperationCallback<usuario>() { public void onCompleted(
						usuario entity, Exception exception, ServiceFilterResponse response)
				{ if (exception == null) { // Insert succeeded 
					System.out.println("exito en insercion");
				} else { // Insert failed 
					System.out.println("fallo en insercion");
				}
				}
				});
	}
*/

}
