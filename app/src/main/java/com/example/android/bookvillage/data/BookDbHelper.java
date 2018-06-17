package com.example.android.bookvillage.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.bookvillage.data.BookContract.BookEntry;

/**
 * Database helper for Book Village app. Manages database creation and version management.
 */

public class BookDbHelper extends SQLiteOpenHelper {

    /**
     * Name of the database file
     */
    private static final String DATABASE_NAME = "book_village.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of {@link BookDbHelper
     * }.
     *
     * @param context of the app
     */
    public BookDbHelper(Context context) {
        super ( context, DATABASE_NAME, null, DATABASE_VERSION );
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the books table
        String SQL_CREATE_BOOKS_TABLE = "CREATE TABLE " + BookEntry.TABLE_NAME + " ("
                + BookEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BookEntry.COLUMN_BOOK_NAME + " TEXT NOT NULL, "
                + BookEntry.COLUMN_BOOK_QUANTITY + " INTEGER NOT NULL, "
                + BookEntry.COLUMN_BOOK_PRICE + " REAL, "
                + BookEntry.COLUMN_BOOK_SUPPLIER_NAME + " INTEGER, "
                + BookEntry.COLUMN_BOOK_SUPPLIER_PHONE + " TEXT); ";

        // Execute the SQL statement
        db.execSQL ( SQL_CREATE_BOOKS_TABLE );
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
    }
}


