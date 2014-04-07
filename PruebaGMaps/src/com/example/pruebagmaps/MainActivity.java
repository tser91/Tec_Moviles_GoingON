package com.example.pruebagmaps;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;


public class MainActivity extends Activity {
  

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }
  
  public void gotologin(View view) {
	  Intent intent = new Intent(this, LoginActivity.class);

	// Verify it resolves
	  PackageManager packageManager = getPackageManager();
	  List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
	  boolean isIntentSafe = activities.size() > 0;

	  // Start an activity if it's safe
	  if (isIntentSafe) {
	      startActivity(intent);}
  }


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
