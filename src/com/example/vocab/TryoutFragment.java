package com.example.vocab;

import com.example.vocab.model.Translation;
import com.example.vocab.model.Tryout;
import com.example.vocab.model.TryoutDataSource;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;

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
		String translateLanguage = tryout.getTryoutLanguage();
		String contentToAsk = tryout.getTryoutContent();
		
		Resources res = getResources();
		String questionText = String.format(
				res.getString(R.string.tryout_question),
				translateLanguage,
				contentToAsk);
		TextView tryoutFromText = (TextView) rootView.findViewById(R.id.tryout_question);
		tryoutFromText.setText(questionText);
		
		Button verifyingButton = (Button) rootView.findViewById(R.id.verifying);
		verifyingButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText et = (EditText) getView().findViewById(R.id.tryout_answer);
				String submittedAnswer = et.getText().toString().trim();

				TextView tv = (TextView) getView().findViewById(R.id.tryout_status);
				long success;
				tryoutDataSource.open();
				if (tryout.isSubmitCorrect(submittedAnswer)) {
					//success
					tv.setText("Correct! :)");
					success = 1;
				} else {
					//fail  => display correct one
					tv.setText("Failed :(");
					TextView correctAnswerText = (TextView) getView().findViewById(R.id.correct_answer);
					correctAnswerText.setText(tryout.getCorrectAnswer());
					success = 0;
					tryoutDataSource.createTryout(tryout.getTranslationId(), tryout.getFromLanguage());
				}
				tryoutDataSource.updateTryoutResult(tryout.getId(), submittedAnswer, success);
				
				long pendingTryout = tryoutDataSource.countPending();
				tryoutDataSource.close();
				Button verifyingButton = (Button) v;
				verifyingButton.setVisibility(v.GONE);
				if (pendingTryout==0) {
					Button nextButton = (Button) getView().findViewById(R.id.next);
					nextButton.setVisibility(v.VISIBLE);
					nextButton.setText(R.string.finish);
					nextButton.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(getActivity(), MainActivity.class);
							startActivity(intent);
						}
					});
				} else {
					Button nextButton = (Button) getView().findViewById(R.id.next);
					nextButton.setVisibility(v.VISIBLE);
					nextButton.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(getActivity(), TryoutActivity.class);
							getActivity().startActivity(intent);
						}
					});
				}
			}
		});
		
		
		return rootView;
	}
	
}
