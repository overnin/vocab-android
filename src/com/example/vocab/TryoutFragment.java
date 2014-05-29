package com.example.vocab;

import com.example.vocab.model.Translation;
import com.example.vocab.model.Tryout;
import com.example.vocab.model.TryoutDataSource;

import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TryoutFragment extends Fragment {
	TryoutDataSource tryoutDataSource;
	Tryout tryout;
	
	public TryoutFragment() {
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, 
			ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(
				R.layout.fragment_tryout, container, false);
		tryoutDataSource = new TryoutDataSource(getActivity());
		tryoutDataSource.open();
		tryout = tryoutDataSource.getPendingTryout();
		tryoutDataSource.close();
		
		String contentToAsk;
		Translation translation = tryout.getTranslation(); 
		if (tryout.getFromLanguage() == translation.getSourceLanguage()) {
			contentToAsk = translation.getSourceContent();
		} else {
			contentToAsk = translation.getDestinationContent();
		}
		
		Resources res = getResources();
		String questionText = String.format(
				res.getString(R.string.tryout_question),
				tryout.getFromLanguage(),
				contentToAsk);
		TextView tryoutFromText = (TextView) rootView.findViewById(R.id.tryout_question);
		tryoutFromText.setText(questionText);
		return rootView;
	}
	
}
