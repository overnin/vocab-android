package com.example.vocab.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class TryoutDataSource {

	private SQLiteDatabase database;
	private TryoutDbHelper dbHelper;
	public static final String TABLE_NAME = "results";
	
	public TryoutDataSource(Context context) {
		dbHelper = new TryoutDbHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public boolean createResult(long translationId, String result) {
		ContentValues values = new ContentValues();
		values.put(TryoutColumns.COLUMN_CREATED_DATE, "now");
		values.put(TryoutColumns.COLUMN_TRANSLATION_ID, translationId);
		values.put(TryoutColumns.COLUMN_RESULT, result);
		long newTryoutId = database.insert(
				TABLE_NAME, null, values);
		return true;
	}
	
	private class TryoutDbHelper extends SQLiteOpenHelper {
		
		
		private static final String TEXT_TYPE = " TEXT";
		private static final String SQL_CREATE_RESULT =
				"CREATE TABLE IF NOT EXISTS " +
			    TABLE_NAME + " (" +
				TryoutColumns._ID + " INTEGER PRIMARY KEY, " +
				TryoutColumns.COLUMN_CREATED_DATE + " DATE, " +
			    TryoutColumns.COLUMN_FROM_LANGUAGE + TEXT_TYPE + ", " +
			    TryoutColumns.COLUMN_ANSWER + TEXT_TYPE + ", " +
			    TryoutColumns.COLUMN_RESULT + TEXT_TYPE + ", " +
			    TryoutColumns.COLUMN_TRANSLATION_ID + " INTEGER, " +
			    " FOREIGN KEY (" + TryoutColumns.COLUMN_TRANSLATION_ID + ") REFERENCE " + 
			    TranslationDataSource.TABLE_NAME + " (" + TranslationColumns._ID + ")" +			    
			    ");";
	   public static final int DATABASE_VERSION = 1;
	   public static final String DATABASE_NAME = "vocab.db";
	   
	   public TryoutDbHelper(Context context) {
		   super(context, DATABASE_NAME, null, DATABASE_VERSION);
	   }
	   
	   @Override
	   public void onCreate(SQLiteDatabase db) {
		   db.execSQL(SQL_CREATE_RESULT);
	   }
	   
	   @Override
	   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		   // TODO Auto-generated method stub			
	   }	
	   
	}
	
}
