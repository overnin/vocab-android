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
		TranslationColumns.COLUMN_SOURCE_LANGUAGE,
		TranslationColumns.COLUMN_SOURCE_CONTENT,
		TranslationColumns.COLUMN_DESTINATION_LANGUAGE,
		TranslationColumns.COLUMN_DESTINATION_CONTENT,
		TranslationColumns.COLUMN_CREATED_AT
		};
	
	public TranslationDataSource(Context context) {
		this.dbHelper = new TranslationDbHelper(context);
	}
	
	public void open() throws SQLException {
		this.database = this.dbHelper.getWritableDatabase();
	}
	
	public void close() {
		this.dbHelper.close();
	}
	
	public boolean createTranslation(String sourceLanguage, String sourceContent,
									String destinationLanguage, String destinationContent) {	
		ContentValues values = new ContentValues();
		values.put(TranslationColumns.COLUMN_SOURCE_LANGUAGE, sourceLanguage);
		values.put(TranslationColumns.COLUMN_SOURCE_CONTENT, sourceContent);
		values.put(TranslationColumns.COLUMN_DESTINATION_LANGUAGE, destinationLanguage);
		values.put(TranslationColumns.COLUMN_DESTINATION_CONTENT, destinationContent);
		long newTranslationId = database.insert(
				TranslationColumns.TABLE_NAME, null, values);
		return true;
	}
	
	public Translation getTranslation() {
		Cursor cursor = database.query(TranslationColumns.TABLE_NAME, 
				allColumns, null, null, null, null,null);
		cursor.moveToFirst();
		Translation translation = cursorToTranslation(cursor);
		cursor.close();
		return translation;
	}
	
	private Translation cursorToTranslation(Cursor cursor) {
		Translation translation = new Translation();
		translation.setSourceLanguage(cursor.getString(0));
		translation.setSourceContent(cursor.getString(1));
		translation.setDestinationLanguage(cursor.getString(2));
		translation.setDestinationContent(cursor.getString(3));
		return translation;
	}
	
	public class TranslationColumns implements BaseColumns {
		public static final String TABLE_NAME = "translations";
		
		public static final String COLUMN_SOURCE_LANGUAGE = "source_language";
		public static final String COLUMN_SOURCE_CONTENT = "source_content";
		public static final String COLUMN_DESTINATION_LANGUAGE = "destination_language";
		public static final String COLUMN_DESTINATION_CONTENT = "destination_content";
		public static final String COLUMN_CREATED_AT = "created_at";
	}
	
	public class TranslationDbHelper extends SQLiteOpenHelper {
	
		private static final String TEXT_TYPE = " TEXT";
		private static final String SQL_CREATE_TRANSLATION = 
				"CREATE TABLE IF NOT EXISTS " +
				TranslationColumns.TABLE_NAME + " (" +
				TranslationColumns._ID + " INTEGER PRIMARY KEY, " +
				TranslationColumns.COLUMN_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
				TranslationColumns.COLUMN_SOURCE_LANGUAGE + TEXT_TYPE + ", " +
				TranslationColumns.COLUMN_SOURCE_CONTENT + TEXT_TYPE + ", " +
				TranslationColumns.COLUMN_DESTINATION_LANGUAGE + TEXT_TYPE + ", " +
				TranslationColumns.COLUMN_DESTINATION_CONTENT + TEXT_TYPE +
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
