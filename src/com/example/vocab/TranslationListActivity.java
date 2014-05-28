package com.example.vocab;

import java.util.List;

import com.example.vocab.model.Translation;
import com.example.vocab.model.TranslationColumns;
import com.example.vocab.model.TranslationDataSource;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
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

		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		translationDataSource = new TranslationDataSource(this);
		translationDataSource.open();
		Cursor cursor = translationDataSource.getTranslations(null);
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
				R.layout.fragment_translation,
				cursor,
				columns,
				to,
				0);
		
		ListView listView = (ListView) findViewById(R.id.translation_list);
		listView.setAdapter(dataAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
				Translation t = translationDataSource.cursorToTranslation((Cursor) adapter.getItemAtPosition(position));
				openEditTranslation(t);
			}
		});
		
		EditText filter = (EditText) findViewById(R.id.translation_filter);
		filter.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				dataAdapter.getFilter().filter(s.toString());
			}
		});
		
		dataAdapter.setFilterQueryProvider(new FilterQueryProvider() {
			public Cursor runQuery(CharSequence constraint) {
				return translationDataSource.getTranslations(constraint.toString());
			}
		});
		//translationDataSource.close();
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
		switch(item.getItemId()) {
		case R.id.action_add_word:
    		openAddWord();
    		return true;
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void openAddWord() {
    	Intent intent = new Intent(this, AddTranslationActivity.class);
    	startActivity(intent);
    }
	
	public void openEditTranslation(Translation t) {
		Intent intent = new Intent(this, EditTranslationActivity.class);
		Bundle b = new Bundle();
		b.putLong("translation_id", t.getId());
		intent.putExtras(b);
		startActivity(intent);
	}


}
