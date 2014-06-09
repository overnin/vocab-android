package com.example.vocab;

import com.example.vocab.model.TranslationDataSource;
import com.example.vocab.model.TryoutDataSource;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class AddTryoutFragment extends Fragment {
	TryoutDataSource tryoutDataSource;
	TranslationDataSource translationDataSource;

	public AddTryoutFragment() {
		setHasOptionsMenu(true);
	}
	
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle saveInstanceState) {
		View rootView = inflater.inflate(
				R.layout.fragment_tryout_add, container, false);
		tryoutDataSource = new TryoutDataSource(getActivity());
		translationDataSource = new TranslationDataSource(getActivity());
		translationDataSource.open();
		OnItemSelectedListener listener = new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
					updateCreateTryoutStatus(null);
				}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}	
		};
		
		ArrayAdapter<CharSequence> adapterTF = ArrayAdapter.createFromResource(getActivity(), 
				R.array.translate_from_array, android.R.layout.simple_spinner_item);
		adapterTF.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner spinner = (Spinner) rootView.findViewById(R.id.language_from_option);
		spinner.setAdapter(adapterTF);
		spinner.setOnItemSelectedListener(listener);
		
		ArrayAdapter<CharSequence> adapterT = ArrayAdapter.createFromResource(getActivity(), 
				R.array.translate_array, android.R.layout.simple_spinner_item);
		adapterT.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner = (Spinner) rootView.findViewById(R.id.translate_option);
		spinner.setAdapter(adapterT);
		spinner.setOnItemSelectedListener(listener);
		
		//updateCreateTryoutStatus(rootView);
		
		return rootView;
	}
	
	public void updateCreateTryoutStatus(View rootView) {
		if (rootView == null) {
			rootView = getView();
		}
		Spinner languageFromSpinner = (Spinner) rootView.findViewById(R.id.language_from_option);
		String languageFrom = languageFromSpinner.getSelectedItem().toString();
		
		Spinner translateSpinner = (Spinner) rootView.findViewById(R.id.translate_option);
		String translate = translateSpinner.getSelectedItem().toString();
		
		long total = translationDataSource.count();
		if (languageFrom.compareTo("Both English and Swahili")==0) {
			total = total * 2;
		}
		
		Resources res = getResources();
		String createTryoutStatus = String.format(
				res.getString(R.string.create_tryout_status),
				total);
		TextView createTryoutStatusText = (TextView) rootView.findViewById(R.id.create_tryout_status);
		createTryoutStatusText.setText(createTryoutStatus);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.tryout_add, menu);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.create:
			createTryouts();
			openDoTryoutFragment();
			break;
		}
		return true;
	}

	public void openDoTryoutFragment(){
		Intent intent = new Intent(getActivity(), TryoutActivity.class);
		startActivity(intent);
		getActivity().finish();
	}
	
	public void createTryouts() {
		Spinner languageFromSpinner = (Spinner) getView().findViewById(R.id.language_from_option);
		String languageFrom = languageFromSpinner.getSelectedItem().toString();
		
		Spinner translateSpinner = (Spinner) getView().findViewById(R.id.translate_option);
		String translate = translateSpinner.getSelectedItem().toString();
		
		Resources res = getResources();
		translationDataSource.open();
		tryoutDataSource.open();
		Cursor cursor = translationDataSource.getTranslations(null);
		do {
			long translationId = cursor.getLong(0);
			if (languageFrom.compareTo("Both English and Swahili")==0) {
				tryoutDataSource.createTryout(translationId, res.getString(R.string.english));
				tryoutDataSource.createTryout(translationId, res.getString(R.string.swahili));
			} else if (languageFrom.compareTo("Only English")==0) {
				tryoutDataSource.createTryout(translationId, res.getString(R.string.english));
			} else {
				tryoutDataSource.createTryout(translationId, res.getString(R.string.swahili));
			}
		} while (cursor.moveToNext()); 
	}

	public void onDestroy() {
		tryoutDataSource.close();
		translationDataSource.close();
		super.onDestroy();
	}
}
