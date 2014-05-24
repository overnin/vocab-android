package com.example.vocab;

import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.support.v4.app.NavUtils;

public class AddWordActivity extends Activity {
	private WordDictionary wordDictionary;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_word);
		// setup the datasource
		wordDictionary = new WordDictionary(this);
		
		// Show the Up button in the action bar.
		setupActionBar();
		Button addButton = (Button) findViewById(R.id.add_word);
		addButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				addWord();
			}
		});
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_word, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void addWord() {
		EditText englishWordEditText = (EditText) findViewById(R.id.english_word);
		String englishWord = englishWordEditText.getText().toString();
		EditText swahiliWordEditText = (EditText) findViewById(R.id.swahili_word);
		String swahiliWord = swahiliWordEditText.getText().toString();
		//To store
		wordDictionary.open();
		wordDictionary.createWord(englishWord, swahiliWord);
		wordDictionary.close();
		englishWordEditText.setText("");
		swahiliWordEditText.setText("");
		//To add a flash message
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
	
}
