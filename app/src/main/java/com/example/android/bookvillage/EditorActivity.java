package com.example.android.bookvillage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bookvillage.data.AppDatabase;
import com.example.android.bookvillage.data.BookEntry;
/*import com.example.android.bookvillage.data.BookContract.BookEntry;*/

public class EditorActivity extends AppCompatActivity {

    // Extra for the book ID to be received in the intent
    public static final String EXTRA_BOOK_ID = "extraBookId";

    // Extra for the book ID to be received after rotation
    public static final String INSTANCE_BOOK_ID = "instanceBookId";

    // Constant for default task id to be used when not in update mode
    private static final int DEFAULT_BOOK_ID = -1;

    /**
     * EditText field to enter the book's name
     */
    private EditText mNameEditText;

    /**
     * EditText field to enter the book's price
     */
    private EditText mPriceEditText;

    /**
     * EditText field to enter the quantity of books
     */
    private TextView mQuantityTextView;

    /**
     * TextView to increase quantity of books
     */
    private TextView mIncreaseTextView;

    /**
     * TextView to decrease quantity of books
     */
    private TextView mDecreaseTextView;

    /**
     * TextView to order books
     */
    private TextView mOrderTextView;


    public int quantityLimit = -1;

    /**
     * EditText field to enter the supplier's name
     */
    private Spinner mSupplierSpinner;

    private int mBookId = DEFAULT_BOOK_ID;

    /**
     * EditText field to enter the supplier's phone
     */
    private EditText mPhoneEditText;

    /*  */
    /**
     * Supplier of the book. The possible valid values are in the BookContract.java file:
     * {@link BookEntry#SUPPLIER_UNKNOWN}, {@link BookEntry#SUPPLIER_ONE}, or
     * {@link BookEntry#SUPPLIER_TWO}.
     */
    private int mSupplier = BookEntry.SUPPLIER_UNKNOWN;

    /**
     * Boolean flag that keeps track of whether the book has been edited (true) or not (false)
     */

    // Member variable for the Database
    private AppDatabase mDb;
    private Intent mIntent;
    private String mNameString;
    private int mQuantityString;
    private double mPriceString;
    private int mPhoneString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Initialize member variable for the data base
        mDb = AppDatabase.getInstance(getApplicationContext());

        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_BOOK_ID)) {
            mBookId = savedInstanceState.getInt(INSTANCE_BOOK_ID, DEFAULT_BOOK_ID);
        }

        mIntent = getIntent();
        if (mIntent != null && mIntent.hasExtra(EXTRA_BOOK_ID)) {
            // This is an existing book, so change app bar to say "Edit Book"
            setTitle(getString(R.string.editor_activity_title_edit_book));
            if (mBookId == DEFAULT_BOOK_ID) {
                // populate the UI
                mBookId = mIntent.getIntExtra(EXTRA_BOOK_ID, DEFAULT_BOOK_ID);
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        // Use the loadBookById method to retrieve the book with id mBookId and
                        // assign its value to a final BookEntry variable
                        final BookEntry book = mDb.bookDao().loadBookById(mBookId);
                        // Call the populateUI method with the retrieve tasks
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                populateUI(book);
                            }
                        });
                    }
                });
            }
        } else {
            // Otherwise, this is a new book, so change the app bar to say "Add a Book"
            setTitle(getString(R.string.editor_activity_title_new_book));
            // Invalidate the options menu, so the "Delete" menu option can be hidden.
            // (It doesn't make sense to delete a book that hasn't been created yet.)
            invalidateOptionsMenu();
        }

        // Find all relevant views that we will need to read user input from
        initViews();

        //To increase book quantity
        mIncreaseTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mQuantity = mQuantityTextView.getText().toString();
                {
                    quantityLimit = Integer.parseInt(mQuantity);
                    mQuantityTextView.setText(String.valueOf(quantityLimit + 1));
                }
            }
        });

        //To decrease book quantity
        mDecreaseTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mQuantity = mQuantityTextView.getText().toString();
                {
                    quantityLimit = Integer.parseInt(mQuantity);
                    //To validate Quantity is greater than 0
                    if ((quantityLimit - 1) >= 0) {
                        mQuantityTextView.setText(String.valueOf(quantityLimit - 1));
                    } else {
                        Toast.makeText(EditorActivity.this, getString(R.string.no_stock), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
        });

        mOrderTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = mPhoneEditText.getText().toString();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNumber));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }

            }
        });

        setupSpinner();
    }

    private void initViews() {
        mNameEditText = (EditText) findViewById(R.id.edit_book_name);
        mQuantityTextView = (TextView) findViewById(R.id.edit_book_quantity);
        mIncreaseTextView = (TextView) findViewById(R.id.increase_text_view);
        mDecreaseTextView = (TextView) findViewById(R.id.decrease_text_view);
        mPriceEditText = (EditText) findViewById(R.id.edit_book_price);
        mSupplierSpinner = (Spinner) findViewById(R.id.spinner_supplier);
        mPhoneEditText = (EditText) findViewById(R.id.edit_supplier_phone);
        mOrderTextView = (TextView) findViewById(R.id.order_text_view);
    }

    /**
     * populateUI would be called to populate the UI when in update mode
     *
     * @param book the bookEntry to populate the UI
     */
    private void populateUI(BookEntry book) {
        // Return if the task is null
        if (book == null) {
            return;
        }
        // Use the variable book to populate the UI
        mNameEditText.setText(book.getBook_name());
        mQuantityTextView.setText(Integer.toString(book.getQuantity()));
        mPriceEditText.setText(Double.toString(book.getPrice()));
        mPhoneEditText.setText(Integer.toString(book.getSupplier_phone_number()));
        mSupplier = book.getSupplier_name();
        switch (mSupplier) {
            case BookEntry.SUPPLIER_ONE:
                mSupplierSpinner.setSelection(1);
                break;
            case BookEntry.SUPPLIER_TWO:
                mSupplierSpinner.setSelection(2);
                break;
            default:
                mSupplierSpinner.setSelection(0);
                break;
        }
        mSupplierSpinner.setSelection(mSupplier);
    }

    /**
     * Setup the dropdown spinner that allows the user to select the suppler of the book.
     */
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter supplierSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_supplier_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        supplierSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mSupplierSpinner.setAdapter(supplierSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mSupplierSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.supplier_one))) {
                        mSupplier = BookEntry.SUPPLIER_ONE;
                    } else if (selection.equals(getString(R.string.supplier_two))) {
                        mSupplier = BookEntry.SUPPLIER_TWO;
                    } else {
                        mSupplier = BookEntry.SUPPLIER_UNKNOWN;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSupplier = BookEntry.SUPPLIER_UNKNOWN;
            }
        });
    }


    /**
     * SaveData is called when the "save" options menu item is clicked.
     * It retrieves user input and inserts that new task data into the underlying database.
     */
    private void saveData() {
        readViews();
        final BookEntry bookEntry = new BookEntry(mNameString, mQuantityString, mPriceString, mSupplier, mPhoneString);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                // Insert the book only if mBookId matches DEFAULT_BOOK_ID
                // Otherwise update it
                // call finish in any case
                if (mBookId == DEFAULT_BOOK_ID) {
                    mDb.bookDao().insertBook(bookEntry);
                } else {
                    //update book
                    bookEntry.setId(mBookId);
                    mDb.bookDao().updateBook(bookEntry);
                }
                finish();
            }
        });
    }

    /**
     * Perform the deletion of the book in the database.
     */
    private void deleteBook() {
        readViews();
        final BookEntry bookEntry = new BookEntry(mNameString, mQuantityString, mPriceString, mSupplier, mPhoneString);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                // Delete the book only if mBookId matches DEFAULT_BOOK_ID
                if (mBookId != DEFAULT_BOOK_ID) {
                    bookEntry.setId(mBookId);
                    mDb.bookDao().deleteBook(bookEntry);
                }
                finish();
            }
        });

    }

    // Read from input fields
    // Use trim to eliminate leading or trailing white space
    private void readViews() {
        mNameString = mNameEditText.getText().toString().trim();
        mQuantityString = Integer.parseInt(mQuantityTextView.getText().toString().trim());
        mPriceString = Double.parseDouble(mPriceEditText.getText().toString().trim());
        mPhoneString = Integer.parseInt(mPhoneEditText.getText().toString().trim());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save book to database
                saveData();
                // Exit activity
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Pop up confirmation dialog for deletion
                showDeleteConfirmationDialog();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the book.
                deleteBook();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the book.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * This method is called when the back button is pressed.
     */
}
