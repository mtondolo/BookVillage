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

/**
 * Displays list of books that were entered and stored in the app.
 */

public class CatalogActivity extends AppCompatActivity implements BookReyclerAdapter.
        BookReyclerAdapterOnClickHandler {

    /*Cursor mCursor;*/
    private BookReyclerAdapter mBookReyclerAdapter;
    private RecyclerView mRecyclerBooks;
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

        LinearLayoutManager booksLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerBooks.setLayoutManager(booksLinearLayoutManager);

        mBookReyclerAdapter = new BookReyclerAdapter(this, this);
        mRecyclerBooks.setAdapter(mBookReyclerAdapter);

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

    /**
     * Helper method to delete all books in the database.
     */

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
}

