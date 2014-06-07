package com.example.vocab;

import com.example.vocab.model.Translation;
import com.example.vocab.model.TranslationDataSource;
import com.example.vocab.model.Tryout;
import com.example.vocab.model.TryoutDataSource;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.os.Build;

public class TryoutActivity extends Activity {
	private TryoutDataSource tryoutDataSource;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tryout);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		//setup datasources
		tryoutDataSource = new TryoutDataSource(this);
		
		tryoutDataSource.open();
		long tryoutCounter = tryoutDataSource.countPending();
		tryoutDataSource.close();
		        
		if (tryoutCounter > 0) {
			DoTryoutFragment fragment = new DoTryoutFragment();
			getFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
		} else {
			AddTryoutFragment fragment = new AddTryoutFragment();
			getFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
		}
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tryout, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings: 
			return true;
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		//wordDictionary.open();
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		//wordDictionary.close();
		super.onPause();
	}
	
	public void onDestroy(){
		tryoutDataSource.close();
		super.onDestroy();
	}
	
}
