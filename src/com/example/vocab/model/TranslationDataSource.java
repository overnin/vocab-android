package com.example.vocab.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class TranslationDataSource {

	private SQLiteDatabase database;
	private TranslationDbHelper dbHelper;
	public static final String[] allColumns = {
		TranslationColumns._ID,
		TranslationColumns.COLUMN_ORIGIN, 
		TranslationColumns.COLUMN_TRANSLATED};
	
	public TranslationDataSource(Context context) {
		this.dbHelper = new TranslationDbHelper(context);
	}
	
	public void open() throws SQLException {
		this.database = this.dbHelper.getWritableDatabase();
	}
	
	public void close() {
		this.dbHelper.close();
	}
	
	public boolean createWord(String origin, String translated) {	
		ContentValues values = new ContentValues();
		values.put(TranslationColumns.COLUMN_ORIGIN, origin);
		values.put(TranslationColumns.COLUMN_TRANSLATED, translated);
		
		long newTranslationId = database.insert(
				TranslationColumns.TABLE_NAME, null, values);
		return true;
	}
	
	public Translation getWord() {
		Cursor cursor = database.query(TranslationColumns.TABLE_NAME, 
				allColumns, null, null, null, null,null);
		cursor.moveToFirst();
		Translation translation = cursorToTranslation(cursor);
		cursor.close();
		return translation;
	}
	
	private Translation cursorToTranslation(Cursor cursor) {
		Translation translation = new Translation();
		translation.setOrigin(cursor.getString(0));
		translation.setTranslated(cursor.getString(1));
		return translation;
	}
	
	public class TranslationColumns implements BaseColumns {
		public static final String TABLE_NAME = "translations";
		public static final String COLUMN_ORIGIN = "origin";
		public static final String COLUMN_TRANSLATED = "translated";	
	}
	
	public class TranslationDbHelper extends SQLiteOpenHelper {
	
		private static final String TEXT_TYPE = " TEXT";
		private static final String SQL_CREATE_TRANSLATION = 
				"CREATE TABLE IF NOT EXISTS " +
				TranslationColumns.TABLE_NAME + " (" +
				TranslationColumns._ID +" INTEGER PRIMARY KEY, " +
				TranslationColumns.COLUMN_ORIGIN + TEXT_TYPE + ", " +
				TranslationColumns.COLUMN_TRANSLATED + TEXT_TYPE +
				");";
		
		public static final int DATABASE_VERSION = 1;
		public static final String DATABASE_NAME = "vocab.db";
		
		public TranslationDbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(SQL_CREATE_TRANSLATION);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			//TODO
		}
	}
	
}
