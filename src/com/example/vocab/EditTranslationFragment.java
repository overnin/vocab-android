package com.example.vocab;

import com.example.vocab.model.Translation;
import com.example.vocab.model.TranslationDataSource;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class EditTranslationFragment extends Fragment {
	TranslationDataSource translationDataSource;
	long translation_id;
	
	public EditTranslationFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(
				R.layout.fragment_edit_translation, container, false);
		Bundle b = getArguments();
		if (b != null) {
			translation_id = b.getLong("translation_id");
		}
		Translation translation = null;
		translationDataSource = new TranslationDataSource(getActivity());
		
		if (translation_id != 0) {
			translationDataSource.open();
			translation = translationDataSource.getTranslation(translation_id);
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
		}
		Spinner destinationSpinner = (Spinner) rootView.findViewById(R.id.destination_language);
		destinationSpinner.setAdapter(adapter);
		if (translation !=null) {
			int pos = adapter.getPosition(translation.getDestinationLanguage());
			destinationSpinner.setSelection(pos);
		}
		
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
		String sourceContent = sourceContentEditText.getText().toString();
		//get destination
		Spinner destinationLanguageSpinner = (Spinner) getView().findViewById(R.id.destination_language);
		String destinationLanguage = destinationLanguageSpinner.getSelectedItem().toString();
		EditText destinationContentEditText = (EditText) getView().findViewById(R.id.destination_content);
		String destinationContent = destinationContentEditText.getText().toString();
		//To store
		translationDataSource.open();
		if (translation_id == 0) {
			translationDataSource.createTranslation(sourceLanguage, sourceContent, 
					destinationLanguage, destinationContent);
		} else {
			translationDataSource.updateTranslation(translation_id, sourceLanguage, sourceContent, 
					destinationLanguage, destinationContent);
		}
		translationDataSource.close();
	  	Intent intent = new Intent(getActivity(), TranslationListActivity.class);
    	startActivity(intent);
	}
}
