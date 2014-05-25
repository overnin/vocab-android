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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.support.v4.app.NavUtils;

import com.example.vocab.model.TranslationDataSource;

public class AddWordActivity extends Activity {
	private TranslationDataSource translactionDataSource;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_word);
		// setup the datasource
		translactionDataSource = new TranslationDataSource(this);
		
		// Supply the list of languages
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, 
				R.array.languages_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		Spinner sourceSpinner = (Spinner) findViewById(R.id.source_language);
		sourceSpinner.setAdapter(adapter);
		Spinner destinationSpinner = (Spinner) findViewById(R.id.destination_language);
		destinationSpinner.setAdapter(adapter);
		
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
		//get source
		Spinner sourceLanguageSpinner = (Spinner) findViewById(R.id.source_language);
		String sourceLanguage = sourceLanguageSpinner.getSelectedItem().toString();
		EditText sourceContentEditText = (EditText) findViewById(R.id.source_content);
		String sourceContent = sourceContentEditText.getText().toString();
		//get destination
		Spinner destinationLanguageSpinner = (Spinner) findViewById(R.id.destination_language);
		String destinationLanguage = destinationLanguageSpinner.getSelectedItem().toString();
		EditText destinationContentEditText = (EditText) findViewById(R.id.destination_content);
		String destinationContent = destinationContentEditText.getText().toString();
		//To store
		translactionDataSource.open();
		translactionDataSource.createTranslation(sourceLanguage, sourceContent, 
				destinationLanguage, destinationContent);
		translactionDataSource.close();
		sourceContentEditText.setText("");
		destinationContentEditText.setText("");
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
