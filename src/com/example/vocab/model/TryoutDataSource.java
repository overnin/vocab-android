package com.example.vocab.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.text.format.Time;

public class TryoutDataSource {

	private SQLiteDatabase database;
	private DigitalRubenDbHelper dbHelper;
	
	public static final String DATABASE_NAME = "vocab.db";
	public static final String TABLE_NAME = "tryouts";
	public static String[] allColumns = {
		TryoutColumns._ID,
		TryoutColumns.COLUMN_TRANSLATION_ID,
		TryoutColumns.COLUMN_FROM_LANGUAGE,
		TryoutColumns.COLUMN_ANSWERED,
		TryoutColumns.COLUMN_ANSWERED_AT,
		TryoutColumns.COLUMN_ANSWER,
		TryoutColumns.COLUMN_ANSWER_CORRECT,
	};
	
	public TryoutDataSource(Context context) {
		dbHelper = new DigitalRubenDbHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.open();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public boolean createTryout(long translationId, String fromLanguage) {
		ContentValues values = new ContentValues();
		values.put(TryoutColumns.COLUMN_TRANSLATION_ID, translationId);
		values.put(TryoutColumns.COLUMN_FROM_LANGUAGE, fromLanguage);
		database.insert(TABLE_NAME, null, values);
		return true;
	}
	
	public long countPending() {
		return DatabaseUtils.longForQuery(database,
				"SELECT COUNT(*) FROM " + TABLE_NAME +
				" WHERE 1!="+TryoutColumns.COLUMN_ANSWERED, null);	
	}
	
	public long countTodaySuccess() {
		return countDayAnswers(1, getToday());
	}
	
	public long countTodayFailure() {
		return countDayAnswers(0, getToday());
	}
	
	public long countDayAnswers(int result, Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, 1);
		Date nextDate = c.getTime();
		String whereClause = "1=" + TryoutColumns.COLUMN_ANSWERED +
				" AND '" + fromDateToString(date) + "'<datetime(" + TryoutColumns.COLUMN_ANSWERED_AT + ",'localtime')" +
				" AND '" + fromDateToString(nextDate) + "'>datetime(" + TryoutColumns.COLUMN_ANSWERED_AT + ",'localtime')" +
				" AND " + result + "=" + TryoutColumns.COLUMN_ANSWER_CORRECT;
		return DatabaseUtils.longForQuery(database,
				"SELECT COUNT(*) FROM " + TABLE_NAME +
				" WHERE " + whereClause , null);			
	}

	
	private Date getToday() {
		return Calendar.getInstance().getTime();
	}
	
	private String fromDateToString(Date date) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		return df.format(date);
	}
	
	private Date fromStringToDate(String date) {
		try {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public long countSuccess(Date date) {
		return countDayAnswers(1, date);
	}
	public long countFailure(Date date) {
		return countDayAnswers(0, date);
	}
	/*
	public void getAll() {
		String[] column = {"datetime("+TryoutColumns.COLUMN_ANSWERED_AT +",'localtime')"};
		Cursor cursor = database.query(TABLE_NAME,
				column, null, null, null, null, null);
		if (cursor == null || cursor.getCount()==0) {
			return;
		}
		cursor.moveToFirst();
		do {
			String date = cursor.getString(0);
			date.charAt(0);
		} while (cursor.moveToNext());
	}*/
	
	public Date getLastPracticeDate(Date fromDate) {
		String whereClause = TryoutColumns.COLUMN_ANSWERED_AT + "<'" + fromDateToString(fromDate) +"'";
		String sortClause = TryoutColumns.COLUMN_ANSWERED_AT + " DESC";
		String[] column = {TryoutColumns.COLUMN_ANSWERED_AT};
		Cursor cursor = database.query(TABLE_NAME,
				column, whereClause, null, null, null, sortClause);
		if (cursor == null || cursor.getCount()==0) {
			return null;
		}
		cursor.moveToFirst();
		String date = cursor.getString(0);
		return fromStringToDate(date);
	}
	
	public Tryout getPendingTryout() {
		String fields = 
				"a."+TryoutColumns._ID +
				",a."+TryoutColumns.COLUMN_FROM_LANGUAGE +
				",a."+TryoutColumns.COLUMN_TRANSLATION_ID +
				",b."+TranslationColumns.COLUMN_SOURCE_CONTENT +
				",b."+TranslationColumns.COLUMN_SOURCE_LANGUAGE +
				",b."+TranslationColumns.COLUMN_DESTINATION_CONTENT +
				",b."+TranslationColumns.COLUMN_DESTINATION_LANGUAGE;
		String query = "SELECT " + fields + 
					 " FROM "+TryoutDataSource.TABLE_NAME+" a" + 
					 " INNER JOIN " + TranslationDataSource.TABLE_NAME + " b" +
					 " ON a."+TryoutColumns.COLUMN_TRANSLATION_ID+"=b."+TranslationColumns._ID +
					 " WHERE a." + TryoutColumns.COLUMN_ANSWERED + "=0;"; 
		Cursor cursor = database.rawQuery(query, null);
		if (cursor == null) {
			return null;
		}
		long totalPending = cursor.getCount();
		Random rand = new Random();
		int draw = rand.nextInt((int) totalPending) + 1;
		cursor.move(draw);
		Tryout tryout = new Tryout();
		tryout.setId(cursor.getLong(0));
		tryout.setFromLanguage(cursor.getString(1));
		tryout.setTranslationId(cursor.getLong(2));
		Translation translation = new Translation();
		translation.setSourceContent(cursor.getString(3));
		translation.setSourceLanguage(cursor.getString(4));
		translation.setDestinationContent(cursor.getString(5));
		translation.setDestinationLanguage(cursor.getString(6));
		tryout.setTranslation(translation);
		return tryout;
	}
	
	public void updateTryoutResult(long id, String answer, long answeredCorrect) {
		ContentValues values = new ContentValues();		
		values.put(TryoutColumns.COLUMN_ANSWERED, 1);
		values.put(TryoutColumns.COLUMN_ANSWER, answer);
		values.put(TryoutColumns.COLUMN_ANSWER_CORRECT , answeredCorrect);
		database.update(TABLE_NAME, values, id+"="+TryoutColumns._ID, null);
	}
	
	
	public Tryout cursorToTryout(Cursor cursor) 
	{
		return new Tryout();
	}



}
