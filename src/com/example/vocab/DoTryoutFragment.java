package com.example.vocab;

import com.example.vocab.model.TranslationDataSource;
import com.example.vocab.model.Tryout;
import com.example.vocab.model.TryoutDataSource;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class DoTryoutFragment extends Fragment {
	TryoutDataSource tryoutDataSource;
	TranslationDataSource translationDataSource;
	Tryout tryout;
	boolean answered = false;
	boolean finished = false;
	long pendingTryoutCount;
	
	final int SIMILAR = 2;
	final int SUCCESS = 1;
	final int FAIL = 0;
	
	public DoTryoutFragment() {
		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, 
			ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(
				R.layout.fragment_tryout_do, container, false);
		tryoutDataSource = new TryoutDataSource(getActivity());
		translationDataSource = new TranslationDataSource(getActivity());
		tryoutDataSource.open();
		pendingTryoutCount = tryoutDataSource.countPending();
		initializeTryout(rootView);
		return rootView;
	}
	
	public void initializeTryout(View rootView) {
		answered = false;
		finished = false;
		tryout = tryoutDataSource.getPendingTryout();
		String translateLanguage = tryout.getTryoutLanguage();
		String contentToAsk = tryout.getTryoutContent();
		
		Resources res = getResources();
        String tryoutStatusText = String.format(
        		res.getString(R.string.tryout_status),
        		pendingTryoutCount);
        TextView tryoutStatusTextView = (TextView) rootView.findViewById(R.id.tryout_status);
        tryoutStatusTextView.setText(tryoutStatusText);
		
		String questionText = String.format(
				res.getString(R.string.tryout_question),
				translateLanguage,
				contentToAsk);
		TextView tryoutFromText = (TextView) rootView.findViewById(R.id.tryout_question);
		tryoutFromText.setText(questionText);
		EditText tryoutAnswer = (EditText) rootView.findViewById(R.id.tryout_answer);
		tryoutAnswer.setText("");
		TextView tryoutStatus = (TextView) rootView.findViewById(R.id.tryout_result);
		tryoutStatus.setText("");
	}

	private int getResult(String answer) {
		if (tryout.isSubmitCorrect(answer)) {
			return SUCCESS;
		}
		translationDataSource.open();
		if (translationDataSource.existsSimilar(tryout.getTryoutContent(), answer)) {
			return SIMILAR;
		}
		return FAIL;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		if (!answered) {
			getActivity().getMenuInflater().inflate(R.menu.tryout_do, menu);
		}
	}
	
	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		if (answered && !finished) {
			getActivity().getMenuInflater().inflate(R.menu.tryout_next, menu);
		} else if (answered && finished) {
			getActivity().getMenuInflater().inflate(R.menu.tryout_finish, menu);
		}
	     
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
    	case R.id.verifying:
    		verifying();
    		return true;
    	case R.id.next:
    		openNextTryoutActivity();
    		return true;
    	case R.id.finish:
    		openMainActivity();
    		return true;
		} 
		return true;
	}
	
	public void openMainActivity() {
		Intent intent = new Intent(getActivity(), MainActivity.class);
		startActivity(intent);
		getActivity().finish();
	}
	
	public void openNextTryoutActivity() {
		initializeTryout(getView());
		getActivity().invalidateOptionsMenu();
	}
	
	public void verifying() {
		EditText et = (EditText) getView().findViewById(R.id.tryout_answer);
		String submittedAnswer = et.getText().toString().trim();

		TextView tv = (TextView) getView().findViewById(R.id.tryout_result);				
		int result =getResult(submittedAnswer);
		Resources res = getResources();
		switch (result) {
			case SUCCESS:
				tv.setText(res.getString(R.string.tryout_success));
				break;
			case SIMILAR:
				tv.setText(String.format(res.getString(R.string.tryout_similar), submittedAnswer));
				return;
			default:
				tv.setText(String.format(res.getString(R.string.tryout_fail), tryout.getCorrectAnswer()));
				tryoutDataSource.createTryout(tryout.getTranslationId(), tryout.getFromLanguage());
				break;
		}
		answered = true;
		tryoutDataSource.updateTryoutResult(tryout.getId(), submittedAnswer, result);
		pendingTryoutCount = tryoutDataSource.countPending();
		if (pendingTryoutCount==0) {
			finished = true;
		}
		getActivity().invalidateOptionsMenu();
	}
	
	public void onDestroy(){
		translationDataSource.close();
		tryoutDataSource.close();
		super.onDestroy();
	}
}
