package com.example.vocab;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

public class EditTranslationActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_translation);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Bundle b = getIntent().getExtras();
		b.putString("action", "edit");
				
		if (savedInstanceState == null) {
			EditTranslationFragment fragment = new EditTranslationFragment();
			fragment.setArguments(b);
			getFragmentManager().beginTransaction()
					.add(R.id.container, fragment).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.action_settings: 
			return true;
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
