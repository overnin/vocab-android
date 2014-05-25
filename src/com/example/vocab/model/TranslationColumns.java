package com.example.vocab.model;

import android.provider.BaseColumns;

public class TranslationColumns implements BaseColumns {		
	public static final String COLUMN_SOURCE_LANGUAGE = "source_language";
	public static final String COLUMN_SOURCE_CONTENT = "source_content";
	public static final String COLUMN_DESTINATION_LANGUAGE = "destination_language";
	public static final String COLUMN_DESTINATION_CONTENT = "destination_content";
	public static final String COLUMN_CREATED_AT = "created_at";
}
