package com.example.vocab;

import java.util.List;

import com.example.vocab.model.Translation;
import com.example.vocab.model.TranslationColumns;
import com.example.vocab.model.TranslationDataSource;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.os.Build;

public class TranslationListActivity extends Activity {
	private TranslationDataSource translationDataSource;
	private SimpleCursorAdapter dataAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_translation_list);

		translationDataSource = new TranslationDataSource(this);
		
		translationDataSource.open();
		Cursor cursor = translationDataSource.getTranslations();
		
		String[] columns = new String[] {
				TranslationColumns.COLUMN_SOURCE_LANGUAGE,
				TranslationColumns.COLUMN_SOURCE_CONTENT,
				TranslationColumns.COLUMN_DESTINATION_LANGUAGE,
				TranslationColumns.COLUMN_DESTINATION_CONTENT,
		};
		
		int[] to = new int[] {
				R.id.source_language,
				R.id.source_content,
				R.id.destination_language,
				R.id.destination_content
		};
		dataAdapter = new SimpleCursorAdapter(this,
				R.layout.translation,
				cursor,
				columns,
				to,
				0);
		
		ListView listView = (ListView) findViewById(R.id.translation_list);
		listView.setAdapter(dataAdapter);
		
		translationDataSource.close();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.translation_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


}