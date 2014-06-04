package com.example.vocab;

import com.example.vocab.model.Translation;
import com.example.vocab.model.TranslationColumns;
import com.example.vocab.model.TranslationDataSource;
import com.example.vocab.model.TryoutDataSource;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class EditTranslationFragment extends Fragment {
	TranslationDataSource translationDataSource;
	TryoutDataSource tryoutDataSource;
	long translationId = 0;
	private SimpleCursorAdapter dataAdapter;
	Handler actHandler;
	
	public EditTranslationFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(
				R.layout.fragment_edit_translation, container, false);
		
		Translation translation = null;
		translationDataSource = new TranslationDataSource(getActivity());
		//tryoutDataSource = new TryoutDataSource(getActivity());
		translationDataSource.open();
		Bundle b = getArguments();
		if (b != null) {
			translationId = b.getLong("translation_id");
			translation = translationDataSource.getTranslation(translationId);
		}
		Cursor sameSource = null;
		if (translation != null) {
			sameSource = translationDataSource.findSimilar(translationId, translation.getSourceContent());
		} else {
			sameSource = translationDataSource.findSimilar(translationId, "");
		}
		String[] columns = new String[] {
				TranslationColumns.COLUMN_DESTINATION_CONTENT
		};
		int[] to = new int[] {
				R.id.destination_content
		};
		dataAdapter= new SimpleCursorAdapter(
				getActivity(),
				R.layout.fragment_translation_similar,
				sameSource,
				columns,
				to,
				0);
		ListView listView = (ListView) rootView.findViewById(R.id.similar_list);
		listView.setAdapter(dataAdapter);
		
		if (sameSource != null && sameSource.getCount()!=0) {
			TextView tv = (TextView) rootView.findViewById(R.id.similar_status);
			tv.setText(R.string.similar_translation_present);
		}
		EditText et = (EditText) rootView.findViewById(R.id.source_content);
		et.setOnFocusChangeListener(new OnFocusChangeListener(){
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					EditText et = (EditText) v.findViewById(R.id.source_content);
					String content = et.getText().toString().trim();
					if (content != null && content != "") {
						dataAdapter.getFilter().filter(content);
					}
				}
			}
		});

		actHandler=new Handler(){
            public void handleMessage(android.os.Message msg)
            {
                //super.handleMessage(msg);
                TextView tv = (TextView) getView().findViewById(R.id.similar_status);
				//if (dataAdapter.getCount() != 0) {
                if (msg.arg1 != 0) {
					tv.setText(R.string.similar_translation_present);
				} else {
					tv.setText(R.string.no_similar_translation);
				}
            }
        };
		
		dataAdapter.setFilterQueryProvider(new FilterQueryProvider() {
			public Cursor runQuery(CharSequence s) {
				Cursor similars = translationDataSource.findSimilar(translationId, s.toString().trim());
				Message msg = actHandler.obtainMessage();
				msg.arg1 = similars.getCount();
				actHandler.sendMessage(msg);
				return similars;
			}
		});
		
		if (translationId != 0) {	
		//	EditText et = (EditText) rootView.findViewById(R.id.source_content);
			et.setText(translation.getSourceContent());
			et = (EditText) rootView.findViewById(R.id.destination_content);
			et.setText(translation.getDestinationContent());
		}
		
		
		// Supply the list of languages
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), 
				R.array.languages_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		Spinner sourceSpinner = (Spinner) rootView.findViewById(R.id.source_language);
		sourceSpinner.setAdapter(adapter);
		if (translation != null) {
			int pos = adapter.getPosition(translation.getSourceLanguage());
			sourceSpinner.setSelection(pos);
		} 
		sourceSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
				Spinner dl = (Spinner) getView().findViewById(R.id.destination_language);
				if (dl != null && position == dl.getSelectedItemPosition()) {
					int newPos = (position + 1) % 2;
					dl.setSelection(newPos);
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {				
			}
		});
		Spinner destinationSpinner = (Spinner) rootView.findViewById(R.id.destination_language);
		destinationSpinner.setAdapter(adapter);
		if (translation !=null) {
			int pos = adapter.getPosition(translation.getDestinationLanguage());
			destinationSpinner.setSelection(pos);
		} 
		destinationSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
				Spinner sl = (Spinner) getView().findViewById(R.id.source_language);
				if (sl != null && position == sl.getSelectedItemPosition()) {
					int newPos = (position + 1) % 2;
					sl.setSelection(newPos);
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {				
			}
		});
		
		Button addButton = (Button) rootView.findViewById(R.id.save_translation);
		addButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				saveTranslation();
			}
		});
		
		return rootView;
	}
	
	private void addSimilars(View rootView, String similarContent) {
		if (similarContent == null || similarContent == "") {
			//let's remove the adapter
		}
		Cursor sameSource = translationDataSource.findSimilar(translationId, similarContent);
		String[] columns = new String[] {
				TranslationColumns.COLUMN_DESTINATION_CONTENT
		};
		int[] to = new int[] {
				R.id.destination_content
		};
		dataAdapter= new SimpleCursorAdapter(
				getActivity(),
				R.layout.fragment_translation_similar,
				sameSource,
				columns,
				to,
				0);
		ListView listView = (ListView) rootView.findViewById(R.id.similar_list);
		listView.setAdapter(dataAdapter);
		
		if (sameSource.getCount()!=0) {
			TextView tv = (TextView) rootView.findViewById(R.id.similar_status);
			tv.setText(R.string.similar_translation_present);
		}
	}

	public void saveTranslation() {
		//get source
		Spinner sourceLanguageSpinner = (Spinner) getView().findViewById(R.id.source_language);
		String sourceLanguage = sourceLanguageSpinner.getSelectedItem().toString();
		EditText sourceContentEditText = (EditText) getView().findViewById(R.id.source_content);
		String sourceContent = sourceContentEditText.getText().toString().trim();
		//get destination
		Spinner destinationLanguageSpinner = (Spinner) getView().findViewById(R.id.destination_language);
		String destinationLanguage = destinationLanguageSpinner.getSelectedItem().toString();
		EditText destinationContentEditText = (EditText) getView().findViewById(R.id.destination_content);
		String destinationContent = destinationContentEditText.getText().toString().trim();
		//To store
		translationDataSource.open();
		if (translationId == 0) {
			translationId = translationDataSource.createTranslation(sourceLanguage, sourceContent, 
					destinationLanguage, destinationContent);
		} else {
			translationDataSource.updateTranslation(translationId, sourceLanguage, sourceContent, 
					destinationLanguage, destinationContent);
		}
		translationDataSource.close();
		tryoutDataSource.open();
		tryoutDataSource.createTryout(translationId, sourceLanguage);
		tryoutDataSource.createTryout(translationId, destinationLanguage);
		tryoutDataSource.close();
	  	Intent intent = new Intent(getActivity(), TranslationListActivity.class);
    	startActivity(intent);
	}

	@Override
	public void onDestroy() {
		translationDataSource.close();
		super.onDestroy();
	}
}
