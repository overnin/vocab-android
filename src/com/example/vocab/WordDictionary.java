package com.example.vocab;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public final class WordDictionary {

	private SQLiteDatabase database;
	private DictionaryDbHelper dbHelper;
	
	
	public WordDictionary(Context context) {
		this.dbHelper = new DictionaryDbHelper(context);
	}
	
	public void open() throws SQLException {
		this.database = this.dbHelper.getWritableDatabase();
	}
	
	public void close() {
		this.dbHelper.close();
	}
	
	public boolean createWord(String english_word, String swahili_word) {	
		ContentValues values = new ContentValues();
		values.put(WordEntry.COLUMN_NAME_ENGLISH, english_word);
		values.put(WordEntry.COLUMN_NAME_SWAHILI, swahili_word);
		
		long newWordId = database.insert(WordEntry.TABLE_NAME, null, values);
		return true;
	}
	
	public WordEntry getWord() {
		Cursor cursor = database.query(WordEntry.TABLE_NAME, 
				WordEntry.allColumns, null, null, null, null,null);
		cursor.moveToFirst();
		WordEntry wordEntry = cursorToWordEntry(cursor);
		cursor.close();
		return wordEntry;
	}
	
	private WordEntry cursorToWordEntry(Cursor cursor) {
		WordEntry wordEntry = new WordEntry();
		wordEntry.setEnglish(cursor.getString(0));
		wordEntry.setSwahili(cursor.getString(1));
		return wordEntry;
	}
	
	public class WordEntry implements BaseColumns {
		private String english;
		private String swahili;
		
		public String getEnglish() {
			return english;
		}
		
		public void setEnglish(String english) {
			this.english = english;
		}
		
		public String getSwahili() {
			return swahili;
		}
		
		public void setSwahili(String swahili) {
			this.swahili = swahili;
		}

	}
	
	public class DictionaryDbHelper extends SQLiteOpenHelper {
	
		public static final String TABLE_NAME = "word_dictionary";
		public static final String COLUMN_NAME_ENGLISH = "english";
		public static final String COLUMN_NAME_SWAHILI = "swahili";
		public static final String[] allColumns = {COLUMN_NAME_ENGLISH, COLUMN_NAME_ENGLISH};

		private static final String TEXT_TYPE = " TEXT";
		private static final String SQL_CREATE_DICTIONARY = 
				"CREATE TABLE IF NOT EXISTS " +
				TABLE_NAME + " (" +
				WordEntry._ID +" INTEGER PRIMARY KEY, " +
				WordEntry.COLUMN_NAME_ENGLISH + TEXT_TYPE + ", " +
				WordEntry.COLUMN_NAME_SWAHILI + TEXT_TYPE +
				");";
		
		public static final int DATABASE_VERSION = 1;
		public static final String DATABASE_NAME = "dictionary.db";
		
		public DictionaryDbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(SQL_CREATE_DICTIONARY);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			//TODO
		}
	}
}