package com.example.vocab;

import com.example.vocab.model.TranslationDataSource;
import com.example.vocab.model.TryoutDataSource;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends Activity {
	private TranslationDataSource translationDataSource;
	private TryoutDataSource tryoutDataSource;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //setup the datasource
        translationDataSource = new TranslationDataSource(this);
        tryoutDataSource = new TryoutDataSource(this);

        //get the translation stats
        translationDataSource.open();
        long tanslationCounter = translationDataSource.count();
        translationDataSource.close();
    
        Resources res = getResources();
        String translationCounterText = String.format(
        		res.getString(R.string.translation_counter),
        		tanslationCounter);

        TextView counterElt = (TextView) findViewById(R.id.translation_counter);
        counterElt.setText(translationCounterText);
        
        //get the tryout stats
        tryoutDataSource.open();
        long tryoutCounter = tryoutDataSource.countPending();
        tryoutDataSource.close();
        String tryoutStatusText = String.format(
        		res.getString(R.string.tryout_status),
        		tryoutCounter);
        TextView tryoutStatusTextView = (TextView) findViewById(R.id.tryout_status);
        tryoutStatusTextView.setText(tryoutStatusText);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case R.id.action_add_word:
    		openAddWord();
    		return true;
    	case R.id.action_tryout:
    		openTryout();
    		return true;
    	case R.id.action_translation_list:
    		openTranslationList();
    		return true;
    	case R.id.action_settings:
    		openSettings();
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }
    
    private void openTranslationList() {
    	Intent intent = new Intent(this, TranslationListActivity.class);
    	startActivity(intent);
    	finish();
	}

	public void openAddWord() {
    	Intent intent = new Intent(this, AddTranslationActivity.class);
    	startActivity(intent);
    	finish();
    }
    
    public void openTryout() {
    	Intent intent = new Intent(this, TryoutActivity.class);
    	startActivity(intent);
    	finish();
    }
    
    public void openSettings() {
    	
    }
    
}
