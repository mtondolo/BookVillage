package com.example.android.bookvillage;

import android.content.Intent;

import com.example.android.bookvillage.data.AppDatabase;
import com.example.android.bookvillage.data.BookEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

/**
 * Displays list of books that were entered and stored in the app.
 */

public class CatalogActivity extends AppCompatActivity implements BookReyclerAdapter.
        BookReyclerAdapterOnClickHandler {

    private static String TAG = CatalogActivity.class.getSimpleName();

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
        setUpViewModel();
    }

    private void setUpViewModel() {
        CatalogViewModel viewModel = ViewModelProviders.of(this).get(CatalogViewModel.class);
        viewModel.getBooks().observe(this, new Observer<List<BookEntry>>() {
            @Override
            public void onChanged(List<BookEntry> bookEntries) {
                Log.d(TAG, "Updating list of tasks from LiveData in ViewModel");
                mBookReyclerAdapter.setBooks(bookEntries);
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

