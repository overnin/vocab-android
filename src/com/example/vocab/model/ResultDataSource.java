package com.example.vocab.model;

import java.sql.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class ResultDataSource {

	private SQLiteDatabase database;
	private ResultDbHelper dbHelper;
	
	public ResultDataSource(Context context) {
		dbHelper = new ResultDbHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public boolean createResult(long translationId, String result) {
		ContentValues values = new ContentValues();
		values.put(ResultColumns.COLUMN_DATE, "now");
		values.put(ResultColumns.COLUMN_TRANSLATION_ID, translationId);
		values.put(ResultColumns.COLUMN_RESULT, result);
		long newResultId = database.insert(
				ResultColumns.TABLE_NAME, null, values);
		return true;
	}
	
	public
	
	
	private abstract class ResultColumns implements BaseColumns {
		public static final String TABLE_NAME = "results";
		public static final String COLUMN_DATE = "date";
		public static final String COLUMN_TRANSLATION_ID = "translation_id";
		public static final String COLUMN_RESULT = "result";
	}
	
	private class ResultDbHelper extends SQLiteOpenHelper {
		
		private static final String TEXT_TYPE = " TEXT";
		private static final String SQL_CREATE_RESULT =
				"CREATE TABLE IF NOT EXISTS " +
			    ResultColumns.TABLE_NAME + " (" +
				ResultColumns._ID + " INTEGER PRIMARY KEY, " +
			    ResultColumns.COLUMN_DATE + " DATE, " +
			    ResultColumns.COLUMN_TRANSLATION_ID + TEXT_TYPE + ", " +
			    ResultColumns.COLUMN_RESULT + TEXT_TYPE + ", " +
			    ");";
	   public static final int DATABASE_VERSION = 1;
	   public static final String DATABASE_NAME = "vocab.db";
	   
	   public ResultDbHelper(Context context) {
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
