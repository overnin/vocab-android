package com.example.vocab;

import com.example.vocab.model.Translation;
import com.example.vocab.model.TranslationDataSource;
import com.example.vocab.model.TryoutDataSource;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class EditTranslationFragment extends Fragment {
	TranslationDataSource translationDataSource;
	TryoutDataSource tryoutDataSource;
	long translationId;
	
	public EditTranslationFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(
				R.layout.fragment_edit_translation, container, false);
		Bundle b = getArguments();
		if (b != null) {
			translationId = b.getLong("translation_id");
		}
		Translation translation = null;
		translationDataSource = new TranslationDataSource(getActivity());
		tryoutDataSource = new TryoutDataSource(getActivity());
		
		if (translationId != 0) {
			translationDataSource.open();
			translation = translationDataSource.getTranslation(translationId);
			translationDataSource.close();
			EditText et = (EditText) rootView.findViewById(R.id.source_content);
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
		} else {
			sourceSpinner.setSelection(1);
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
}
