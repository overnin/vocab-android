package com.example.vocab.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class TryoutDataSource {

	private SQLiteDatabase database;
	private DigitalRubenDbHelper dbHelper;
	
	public static final String DATABASE_NAME = "vocab.db";
	public static final String TABLE_NAME = "tryouts";
	public static String[] allColumns = {
		TryoutColumns._ID,
		TryoutColumns.COLUMN_TRANSLATION_ID,
		TryoutColumns.COLUMN_FROM_LANGUAGE,
		TryoutColumns.COLUMN_ANSWER,
		TryoutColumns.COLUMN_ANSWERED,
		TryoutColumns.COLUMN_ANSWERED_AT,
		TryoutColumns.COLUMN_ANSWER_CORRECT,
		TryoutColumns.COLUMN_SKIPED,
		TryoutColumns.COLUMN_SKIPED_AT,
	};
	
	public TryoutDataSource(Context context) {
		dbHelper = new DigitalRubenDbHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public boolean createTryout(long translationId, String fromLanguage) {
		ContentValues values = new ContentValues();
		values.put(TryoutColumns.COLUMN_TRANSLATION_ID, translationId);
		values.put(TryoutColumns.COLUMN_FROM_LANGUAGE, fromLanguage);
		long newTryoutId = database.insert(
				TABLE_NAME, null, values);
		return true;
	}
	
	public long countPending() {
		return DatabaseUtils.longForQuery(database,
				"SELECT COUNT(*) FROM " + TABLE_NAME, null);	
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
		cursor.moveToFirst();
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
	
	public Tryout cursorToTryout(Cursor cursor) 
	{
		return new Tryout();
	}
}
