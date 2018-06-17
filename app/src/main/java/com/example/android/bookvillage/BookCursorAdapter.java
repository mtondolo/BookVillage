package com.example.android.bookvillage;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bookvillage.data.BookContract.BookEntry;

import java.text.NumberFormat;

import static com.example.android.bookvillage.data.BookContract.BookEntry.COLUMN_BOOK_QUANTITY;

/**
 * {@link BookCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of book data as its data source. This adapter knows
 * how to create list items for each row of book data in the {@link Cursor}.
 */

public class BookCursorAdapter extends CursorAdapter {


    /**
     * Constructs a new {@link BookCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public BookCursorAdapter(Context context, Cursor c) {
        super ( context, c, 0 /* flags */ );
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from ( context ).inflate ( R.layout.list_item, parent, false );
    }

    /**
     * This method binds the book data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current book can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        // Find individual views that we want to modify in the list item layout
        TextView nameTextView = (TextView) view.findViewById ( R.id.name );
        TextView quantityTextView = (TextView) view.findViewById ( R.id.quantity );
        TextView priceTextView = (TextView) view.findViewById ( R.id.price );

        // Find the columns of book attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex ( BookEntry.COLUMN_BOOK_NAME );
        int quantityColumnIndex = cursor.getColumnIndex ( COLUMN_BOOK_QUANTITY );
        int priceColumnIndex = cursor.getColumnIndex ( BookEntry.COLUMN_BOOK_PRICE );

        // Read the book attributes from the Cursor for the current book
        String bookName = cursor.getString ( nameColumnIndex );
        final int bookQuantity = cursor.getInt ( quantityColumnIndex );
        double bookPrice = cursor.getDouble ( priceColumnIndex );
        final int productId = cursor.getInt ( cursor.getColumnIndex ( BookEntry._ID ) );

        // Update the TextViews with the attributes for the current book
        nameTextView.setText ( bookName );
        quantityTextView.setText ( String.valueOf ( bookQuantity ) );
        String formattedPrice = NumberFormat.getCurrencyInstance ().format ( bookPrice );
        priceTextView.setText ( String.valueOf ( formattedPrice ) );

        TextView saleTextView = (TextView) view.findViewById ( R.id.sell );
        saleTextView.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                if (bookQuantity > 0) {
                    int newQuantity = bookQuantity - 1;
                    Uri productUri = ContentUris.withAppendedId ( BookEntry.CONTENT_URI, productId );

                    ContentValues values = new ContentValues ();
                    values.put ( COLUMN_BOOK_QUANTITY, newQuantity );
                    context.getContentResolver ().update ( productUri, values, null, null );
                    Toast.makeText ( context, context.getString ( R.string.sell_success ), Toast.LENGTH_SHORT ).show ();
                } else {
                    Toast.makeText ( context, context.getString ( R.string.no_stock ), Toast.LENGTH_SHORT ).show ();
                }
            }
        } );
    }

}

