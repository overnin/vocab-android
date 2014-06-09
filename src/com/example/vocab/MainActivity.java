package com.example.vocab;

import java.util.Calendar;
import java.util.Date;

import com.example.vocab.model.TranslationDataSource;
import com.example.vocab.model.TryoutDataSource;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
        translationDataSource.open();
        tryoutDataSource.open();
        
        Resources res = getResources();
        //today stats
        long todayS = tryoutDataSource.countTodaySuccess();
        long todayF = tryoutDataSource.countTodayFailure();
        //long todayT = todayS + todayF;
        String todaySuccessStatsText = String.format(res.getString(R.string.success_stats),
        		todayS);
        TextView todaySuccessStatsElt = (TextView) findViewById(R.id.today_success_stats);
        todaySuccessStatsElt.setText(todaySuccessStatsText);
        
        String todayFailureStatsText = String.format(res.getString(R.string.failure_stats),
        		todayF);
        TextView todayFailureStatsElt = (TextView) findViewById(R.id.today_failure_stats);
        todayFailureStatsElt.setText(todayFailureStatsText);
        
        //last practice stats
        Date lastPracticeDate = tryoutDataSource.getLastPracticeDate(Calendar.getInstance().getTime());
        setLastSummary(lastPracticeDate, R.id.last_practice, R.id.last_practice_success_stats, R.id.last_practice_failure_stats);
        if (lastPracticeDate!=null) {
        	Date lastLastPracticeDate = tryoutDataSource.getLastPracticeDate(lastPracticeDate);
            setLastSummary(lastLastPracticeDate, R.id.last_last_practice, R.id.last_last_practice_success_stats, R.id.last_last_practice_failure_stats);
        }
        
        //general stats
        long tanslationCount = translationDataSource.count();
        long tryoutCount = tryoutDataSource.countPending();
        String generalStatsText = String.format(
        		res.getString(R.string.general_stats),
        		tanslationCount,
        		tryoutCount);
        TextView counterElt = (TextView) findViewById(R.id.general_stats);
        counterElt.setText(generalStatsText);
        
    }

    private void setLastSummary(Date lastPracticeDate, int eltId, int eltSuccessStatsId, int eltFailureStatsId) {
    	if (lastPracticeDate != null) {
    		Resources res = getResources();
	        long lastPracticeS = tryoutDataSource.countSuccess(lastPracticeDate);
	        long lastPracticeF = tryoutDataSource.countFailure(lastPracticeDate);
	        String lastPracticeSuccessStatsText = String.format(res.getString(R.string.success_stats),
	        		lastPracticeS);
	        TextView lastPraticeStatsElt = (TextView) findViewById(eltSuccessStatsId);
	        lastPraticeStatsElt.setText(lastPracticeSuccessStatsText);
	        String lastPracticeFailureStatsText = String.format(res.getString(R.string.failure_stats),
	        		lastPracticeF);
	        TextView lastPraticeFailureStatsElt = (TextView) findViewById(eltFailureStatsId);
	        lastPraticeFailureStatsElt.setText(lastPracticeFailureStatsText);	        
	        
	        
	        String lastPractice = DateUtils.getRelativeTimeSpanString(this, lastPracticeDate.getTime(), true).toString();
	        TextView lastPracticeElt = (TextView) findViewById(eltId);
        	lastPracticeElt.setText(lastPractice);
        } else {
        	TextView lastPracticeFailureStatsElt = (TextView) findViewById(eltFailureStatsId);
	        lastPracticeFailureStatsElt.setVisibility(View.GONE);
        	TextView lastPracticeStatsElt = (TextView) findViewById(eltSuccessStatsId);
	        lastPracticeStatsElt.setVisibility(View.GONE);
        	TextView lastPracticeElt = (TextView) findViewById(eltId);
        	lastPracticeElt.setVisibility(View.GONE);
        }
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
    
    @Override
    public void onDestroy() {
    	translationDataSource.close();
    	tryoutDataSource.close();
    	super.onDestroy();
    }
    
}
