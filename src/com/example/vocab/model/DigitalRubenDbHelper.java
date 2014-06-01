package com.example.vocab.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DigitalRubenDbHelper extends SQLiteOpenHelper{

	public static final String DATABASE_NAME = "vocab.db";
	public static final int DATABASE_VERSION = 1;

	//Translation Table
	private static final String TEXT_TYPE = " TEXT";
	private static final String SQL_CREATE_TRANSLATION = 
			"CREATE TABLE IF NOT EXISTS " +
		    TranslationDataSource.TABLE_NAME + " (" +
			TranslationColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
			TranslationColumns.COLUMN_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
			TranslationColumns.COLUMN_UPDATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
			TranslationColumns.COLUMN_SOURCE_LANGUAGE + TEXT_TYPE + ", " +
			TranslationColumns.COLUMN_SOURCE_CONTENT + TEXT_TYPE + ", " +
			TranslationColumns.COLUMN_DESTINATION_LANGUAGE + TEXT_TYPE + ", " +
			TranslationColumns.COLUMN_DESTINATION_CONTENT + TEXT_TYPE +
			");";
	private static final String SQL_CREATE_TRANSLATION_TRIGGER =
			"CREATE TRIGGER update_at_date_at_update AFTER UPDATE ON " + TranslationDataSource.TABLE_NAME +
			" BEGIN" +
					" update " + TranslationDataSource.TABLE_NAME +
					" SET " + TranslationColumns.COLUMN_UPDATED_AT + "=datetime('now')" +
					" WHERE " + TranslationColumns._ID + "=NEW." + TranslationColumns._ID + ";" +
			" END;";
	//Tryout table
	private static final String SQL_CREATE_TRYOUT =
			"CREATE TABLE IF NOT EXISTS " +
		    TryoutDataSource.TABLE_NAME + " (" +
			TryoutColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
			TryoutColumns.COLUMN_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
		    TryoutColumns.COLUMN_TRANSLATION_ID + " INTEGER, " +
		    TryoutColumns.COLUMN_FROM_LANGUAGE + TEXT_TYPE + " NOT NULL, " +
		    TryoutColumns.COLUMN_ANSWER + TEXT_TYPE + " DEFAULT NULL, " +
		    TryoutColumns.COLUMN_ANSWERED + " INTEGER DEFAULT 0, " +
		    TryoutColumns.COLUMN_ANSWERED_AT + " DATETIME DEFAULT NULL, " +
		    TryoutColumns.COLUMN_ANSWER_CORRECT + " INTEGER DEFAULT 0, " +
		    " FOREIGN KEY (" + TryoutColumns.COLUMN_TRANSLATION_ID + ") REFERENCES " + 
		    TranslationDataSource.TABLE_NAME + " (" + TranslationColumns._ID + ")" +			    
		    ");";
	private static final String SQL_CREATE_TRYOUT_TRIGGER =
			"CREATE TRIGGER update_at_date_at_answer AFTER UPDATE ON " + TryoutDataSource.TABLE_NAME +
			" BEGIN" +
					" update " + TryoutDataSource.TABLE_NAME +
					" SET " + TryoutColumns.COLUMN_ANSWERED_AT + "=datetime('now')" +
					" WHERE " + TryoutColumns._ID + "=NEW." + TryoutColumns._ID + ";" +
			" END;";
	
	public DigitalRubenDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_TRANSLATION);
		db.execSQL(SQL_CREATE_TRANSLATION_TRIGGER);
		db.execSQL(SQL_CREATE_TRYOUT);
		db.execSQL(SQL_CREATE_TRYOUT_TRIGGER);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//TODO
	}
}
