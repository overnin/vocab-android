package com.example.vocab.model;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class TranslationDataSource {

	private SQLiteDatabase database;
	private DigitalRubenDbHelper dbHelper;

	
	public static final String TABLE_NAME = "translations";
	public static final String[] allColumns = {
		TranslationColumns._ID,
		TranslationColumns.COLUMN_SOURCE_LANGUAGE,
		TranslationColumns.COLUMN_SOURCE_CONTENT,
		TranslationColumns.COLUMN_DESTINATION_LANGUAGE,
		TranslationColumns.COLUMN_DESTINATION_CONTENT,
		TranslationColumns.COLUMN_UPDATED_AT,
		TranslationColumns.COLUMN_CREATED_AT
		};
	
	public TranslationDataSource(Context context) {
		this.dbHelper = new DigitalRubenDbHelper(context);
	}
	
	public void open() throws SQLException {
		this.database = this.dbHelper.getWritableDatabase();
	}
	
	public void close() {
		this.dbHelper.close();
	}
	
	public long createTranslation(String sourceLanguage, String sourceContent,
									String destinationLanguage, String destinationContent) {	
		ContentValues values = new ContentValues();
		values.put(TranslationColumns.COLUMN_SOURCE_LANGUAGE, sourceLanguage);
		values.put(TranslationColumns.COLUMN_SOURCE_CONTENT, sourceContent);
		values.put(TranslationColumns.COLUMN_DESTINATION_LANGUAGE, destinationLanguage);
		values.put(TranslationColumns.COLUMN_DESTINATION_CONTENT, destinationContent);
		long newTranslationId = database.insert(
				TABLE_NAME, null, values);
		return newTranslationId;
	}
	
	public boolean updateTranslation(long id, String sourceLanguage, String sourceContent,
			String destinationLanguage, String destinationContent) {
		ContentValues values = new ContentValues();
		values.put(TranslationColumns.COLUMN_SOURCE_LANGUAGE, sourceLanguage);
		values.put(TranslationColumns.COLUMN_SOURCE_CONTENT, sourceContent);
		values.put(TranslationColumns.COLUMN_DESTINATION_LANGUAGE, destinationLanguage);
		values.put(TranslationColumns.COLUMN_DESTINATION_CONTENT, destinationContent);
		long newTranslationId = database.update(
				TABLE_NAME, values, TranslationColumns._ID + "=" + id, null);
		return true;
	}
	
	public long count() {
		return DatabaseUtils.longForQuery(database,
				"SELECT COUNT(*) FROM " + TABLE_NAME, null);	
	}
	
	public Translation getTranslation(long id) {
		String whereClause = null;
		if (id != 0) {
			whereClause = TranslationColumns._ID + "="+ id;
		}
		Cursor cursor = database.query(TABLE_NAME, 
				allColumns, whereClause, null, null, null, null);
		if (cursor == null) {
			return null;
		}
		cursor.moveToFirst();
		Translation translation = cursorToTranslation(cursor);
		cursor.close();
		return translation;
	}
	
	public Cursor getTranslations(String inputText) {
		String whereClause = null;
		if (inputText != null && inputText.length() != 0) {
			whereClause = TranslationColumns.COLUMN_DESTINATION_CONTENT + " like '%" + inputText + "%'" 
					+ " OR " + TranslationColumns.COLUMN_SOURCE_CONTENT + " like '%" + inputText + "%'";
		} 
		Cursor cursor = database.query(TABLE_NAME, allColumns, whereClause, null, null, null, 
				"datetime(" + TranslationColumns.COLUMN_UPDATED_AT+ ") DESC");
		if (cursor!=null) {
			cursor.moveToFirst();
		}
		return cursor;
	}
	
	public Translation getUnTryoutTranslation() {
		String whereClause = TranslationColumns._ID + " NOT IN " + 
				"(SELECT " +TryoutColumns.COLUMN_TRANSLATION_ID + 
				" FROM "+ TryoutDataSource.TABLE_NAME+")";
		String sortClause = "datetime(" + TranslationColumns.COLUMN_UPDATED_AT+ ") ASC";
		Cursor cursor = database.query(TABLE_NAME, allColumns, whereClause, 
				null, null, null, sortClause);
		if (cursor==null) {
			return null;
		}
		cursor.moveToFirst();
		Translation translation = cursorToTranslation(cursor);
		cursor.close();
		return translation;
	}
	
	public Translation cursorToTranslation(Cursor cursor) {
		Translation translation = new Translation();
		translation.setId(cursor.getLong(0));
		translation.setSourceLanguage(cursor.getString(1));
		translation.setSourceContent(cursor.getString(2));
		translation.setDestinationLanguage(cursor.getString(3));
		translation.setDestinationContent(cursor.getString(4));
		translation.setUpdatedAt(cursor.getString(5));
		translation.setCreatedAt(cursor.getString(6));
		return translation;
	}

}
