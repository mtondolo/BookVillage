package com.example.android.bookvillage;

import android.content.Intent;

import com.example.android.bookvillage.data.AppDatabase;
import com.example.android.bookvillage.data.BookEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

/*import com.example.android.bookvillage.data.BookContract.BookEntry;*/

/**
 * Displays list of books that were entered and stored in the app.
 */

public class CatalogActivity extends AppCompatActivity implements BookReyclerAdapter.
        BookReyclerAdapterOnClickHandler {


/*public static final String[] MAIN_CATALOG_PROJECTION = {
            BookEntry._ID,
            BookEntry.COLUMN_BOOK_QUANTITY,
            BookEntry.COLUMN_BOOK_NAME,
            BookEntry.COLUMN_BOOK_PRICE,
    };*/



    /*public static final int INDEX_ID = 0;
    public static final int INDEX_QUANTITY = 1;
    public static final int INDEX_NAME = 2;
    public static final int INDEX_PRICE = 3;*/


    //private static final int BOOK_LOADER = 0;


    /*Cursor mCursor;*/
    private BookReyclerAdapter mBookReyclerAdapter;
    private RecyclerView mRecyclerBooks;
    /*private View mEmptyView;
    private int mPosition = RecyclerView.NO_POSITION;*/
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        mRecyclerBooks = findViewById(R.id.list_books);
        //mEmptyView = findViewById(R.id.empty_view);

        LinearLayoutManager booksLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerBooks.setLayoutManager(booksLinearLayoutManager);

        mBookReyclerAdapter = new BookReyclerAdapter(this, this);
        mRecyclerBooks.setAdapter(mBookReyclerAdapter);


        /*LoaderManager.getInstance(this).initLoader(BOOK_LOADER, null, this);*/
        // Initialize member variable for the data base
        mDb = AppDatabase.getInstance(getApplicationContext());
    }

    /**
     * This method is called after this activity has been paused or restarted.
     * Often, this is after new data has been inserted through an EditorActivity,
     * so this re-queries the database data for any changes.
     */
    @Override
    protected void onResume() {
        super.onResume();
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final List<BookEntry> books = mDb.bookDao().loadAllBooks();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mBookReyclerAdapter.setBooks(books);
                    }
                });
            }
        });
    }
/* private void showRecyclerBooks() {
        mEmptyView.setVisibility(View.INVISIBLE);
        mRecyclerBooks.setVisibility(View.VISIBLE);
    }*/

    /**
     * Helper method to delete all books in the database.
     */


 /* private void deleteAllBooks() {
        int rowsDeleted = getContentResolver().delete(BookEntry.CONTENT_URI, null, null);
        Log.v("CatalogActivity", rowsDeleted + " rows deleted from book database");
    }*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                //deleteAllBooks();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(int id) {
        Intent editorActivityIntent = new Intent(CatalogActivity.this, EditorActivity.class);
        editorActivityIntent.putExtra(EditorActivity.EXTRA_BOOK_ID, id);
        startActivity(editorActivityIntent);
    }


/*@Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,
                BookEntry.CONTENT_URI,   // The table to query
                MAIN_CATALOG_PROJECTION,            // The columns to return
                null,         // The columns for the WHERE clause
                null,      // The values for the WHERE clause
                null);
    }*/



  /*@Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Update {@link BookCursorAdapter} with this new cursor containing updated book data
        mBookReyclerAdapter.swapCursor(data);
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
        mRecyclerBooks.smoothScrollToPosition(mPosition);
        if (data.getCount() != 0) showRecyclerBooks();
    }*/



 /*@Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Callback called when the data needs to be deleted
        mBookReyclerAdapter.swapCursor(null);
    }*/


  /*  @Override
    public void onClick(int _id) {
        Intent editorActivityIntent = new Intent(CatalogActivity.this, EditorActivity.class);
        Uri uriForBookClicked = BookEntry.buildBookUriWithId(_id);
        editorActivityIntent.setData(uriForBookClicked);
        startActivity(editorActivityIntent);
    }*/

}

