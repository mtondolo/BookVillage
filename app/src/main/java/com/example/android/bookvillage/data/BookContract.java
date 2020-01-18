package com.example.android.bookvillage.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * API Contract for the Book Village app.
 */
/*
public final class BookContract {

    // To prevent someone from accidentally instantiating the contract class.
    private BookContract() {
    }

    */
/**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     *//*

    public static final String CONTENT_AUTHORITY = "com.example.android.bookvillage";

    */
/**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     *//*

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    */
/**
     * Possible path (appended to base content URI for possible URI's)
     * For instance, content://com.example.android.pets/pets/ is a valid path for
     * looking at pet data. content://com.example.android.pets/staff/ will fail,
     * as the ContentProvider hasn't been given any information on what to do with "staff".
     *//*

    public static final String PATH_BOOKS = "books";

    */
/**
     * Inner class that defines constant values for the books database table.
     * Each entry in the table represents a single book.
     *//*

    public static final class BookEntry implements BaseColumns {

        */
/**
         * The MIME type of the {@link #CONTENT_URI} for a list of books.
         *//*

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;

        */
/**
         * The MIME type of the {@link #CONTENT_URI} for a single book.
         *//*

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;

        */
/**
         * The content URI to access the book data in the provider
         *//*

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_BOOKS);

        */
/**
         * Name of database table for books
         *//*

        public final static String TABLE_NAME = "books";

        */
/**
         * Unique ID number for the book (only for use in the database table).
         * <p>
         * Type: INTEGER
         *//*

        public final static String _ID = BaseColumns._ID;

        */
/**
         * Name of the book.
         * <p>
         * Type: TEXT
         *//*

        public final static String COLUMN_BOOK_NAME = "book_name";

        */
/**
         * Quantity of the books.
         * <p>
         * Type: INTEGER
         *//*

        public final static String COLUMN_BOOK_QUANTITY = "quantity";

        */
/**
         * Price of the book.
         * <p>
         * Type: INTEGER
         *//*

        public final static String COLUMN_BOOK_PRICE = "price";

        */
/**
         * Name of the supplier
         * <p>
         * The only possible values are {@link #SUPPLIER_UNKNOWN}, {@link #SUPPLIER_ONE},
         * or {@link #SUPPLIER_TWO}.
         * <p>
         * Type: INTEGER
         *//*

        public final static String COLUMN_BOOK_SUPPLIER_NAME = "supplier_name";

        */
/**
         * Phone number of the supplier.
         * <p>
         * Type: INTEGER
         *//*

        public final static String COLUMN_BOOK_SUPPLIER_PHONE = "supplier_phone_number";

        */
/**
         * Possible names for the suppliers of the book.
         *//*

        public static final int SUPPLIER_UNKNOWN = 0;
        public static final int SUPPLIER_ONE = 1;
        public static final int SUPPLIER_TWO = 2;

        public static Uri buildBookUriWithId(int _id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(String.valueOf(_id))
                    .build();
        }
    }
}
*/
