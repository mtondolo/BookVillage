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

public class EditorActivity extends AppCompatActivity
        /*implements LoaderManager.LoaderCallbacks<Cursor>*/ {

    /* *//**
     * Identifier for the book data loader
     *//*
    private static final int EXISTING_BOOK_LOADER = 0;*/

    /* */
    /**
     * Content URI for the existing book (null if it's a new book)
     *//*
    private Uri mCurrentBookUri;*/

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
    /* private boolean mBookHasChanged = false;

     */
    /**
     * OnTouchListener that listens for any user touches on a View, implying that they are modifying
     * the view, and we change the mBookHasChanged boolean to true.
     *//*
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mBookHasChanged = true;
            return false;
        }
    };*/

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

        // Setup OnTouchListeners on all the input fields, so we can determine if the user
        // has touched or modified them. This will let us know if there are unsaved changes
        // or not, if the user tries to leave the editor without saving.
       /* mNameEditText.setOnTouchListener(mTouchListener);
        mQuantityTextView.setOnTouchListener(mTouchListener);
        mIncreaseTextView.setOnTouchListener(mTouchListener);
        mDecreaseTextView.setOnTouchListener(mTouchListener);
        mPriceEditText.setOnTouchListener(mTouchListener);
        mSupplierSpinner.setOnTouchListener(mTouchListener);
        mPhoneEditText.setOnTouchListener(mTouchListener);
        mOrderTextView.setOnTouchListener(mTouchListener);*/

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
       /* if (TextUtils.isEmpty(nameString)) {
            Toast.makeText(this, getString(R.string.insert_book_name_toast), Toast.LENGTH_LONG).show();
        } else {*/
            /*ContentValues values = new ContentValues();
            values.put(BookEntry.COLUMN_BOOK_NAME, nameString);
            values.put(BookEntry.COLUMN_BOOK_QUANTITY, quantityString);*/

           /* if (TextUtils.isEmpty(priceString)) {
                values.put(BookEntry.COLUMN_BOOK_PRICE, defaultPrice);
            } else {
                values.put(BookEntry.COLUMN_BOOK_PRICE, Double.parseDouble(priceString));
            }
            values.put(BookEntry.COLUMN_BOOK_SUPPLIER_NAME, mSupplier);
            if (TextUtils.isEmpty(phoneString)) {
                values.put(BookEntry.COLUMN_BOOK_SUPPLIER_PHONE, defaultSupplierPhone);
            } else {
                values.put(BookEntry.COLUMN_BOOK_SUPPLIER_PHONE, Integer.parseInt(phoneString));
            }*/




           /* // Determine if this is a new or existing book by checking if mCurrentBookUri is null or not
            if (mCurrentBookUri == null) {

                // This is a NEW book, so insert a new book into the provider,
                // returning the content URI for the new book.
                Uri newUri = getContentResolver().insert(BookEntry.CONTENT_URI, values);

                // Show a toast message depending on whether or not the insertion was successful.
                if (newUri == null) {
                    // If the new content URI is null, then there was an error with insertion.
                    Toast.makeText(this, getString(R.string.editor_insert_book_failed),
                            Toast.LENGTH_SHORT).show();
                } else {
                    // Otherwise, the insertion was successful and we can display a toast.
                    Toast.makeText(this, getString(R.string.editor_insert_book_successful),
                            Toast.LENGTH_SHORT).show();
                }*//*
            } else {
                // Otherwise this is an EXISTING book, so update the book with content URI: mCurrentBookUri
                // and pass in the new ContentValues. Pass in null for the selection and selection args
                // because mCurrentBookUri will already identify the correct row in the database that
                // we want to modify.
                int rowsAffected = getContentResolver().update(mCurrentBookUri, values, null, null);

                // Show a toast message depending on whether or not the update was successful.
                if (rowsAffected == 0) {
                    // If no rows were affected, then there was an error with the update.
                    Toast.makeText(this, getString(R.string.editor_update_book_failed),
                            Toast.LENGTH_SHORT).show();
                } else {
                    // Otherwise, the update was successful and we can display a toast.
                    Toast.makeText(this, getString(R.string.editor_update_book_successful),
                            Toast.LENGTH_SHORT).show();
                }
            }*//*
        }*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    /**
     * This method is called after invalidateOptionsMenu(), so that the
     * menu can be updated (some menu items can be hidden or made visible).
     */
  /*  @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new book, hide the "Delete" menu item.
        if (mCurrentBookUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }*/
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
        // Respond to a click on the "Up" arrow button in the app bar
           /* case android.R.id.home:
                // If the book hasn't changed, continue with navigating up to parent activity
                // which is the {@link CatalogActivity}.
                if (!mBookHasChanged) {
                    NavUtils.navigateUpFromSameTask(this);
                    return true;
                }*/

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that
        // changes should be discarded.
            /*    DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }*/

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
   /* @Override
    public void onBackPressed() {
        // If the book hasn't changed, continue with handling back button press
        if (!mBookHasChanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }*/

 /*   @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        // Since the editor shows all book attributes, define a projection that contains
        // all columns from the book table
        String[] projection = {
                BookEntry._ID,
                BookEntry.COLUMN_BOOK_NAME,
                BookEntry.COLUMN_BOOK_QUANTITY,
                BookEntry.COLUMN_BOOK_PRICE,
                BookEntry.COLUMN_BOOK_SUPPLIER_NAME,
                BookEntry.COLUMN_BOOK_SUPPLIER_PHONE};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mCurrentBookUri,         // Query the content URI for the current book
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }
*/
   /* @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if (cursor.moveToFirst()) {

            // Find the columns of book attributes that we're interested in
            int nameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_NAME);
            int quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_QUANTITY);
            int priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_PRICE);
            int supplierNameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_SUPPLIER_NAME);
            int supplierPhoneColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_SUPPLIER_PHONE);

            // Extract out the value from the Cursor for the given column index
            String bookName = cursor.getString(nameColumnIndex);
            int bookQuantity = cursor.getInt(quantityColumnIndex);
            double bookPrice = cursor.getDouble(priceColumnIndex);
            int bookSupplierName = cursor.getInt(supplierNameColumnIndex);
            String bookQSupplierPhone = cursor.getString((supplierPhoneColumnIndex));

            // Update the TextViews with the attributes for the current book
            mNameEditText.setText(bookName);
            mPhoneEditText.setText(bookQSupplierPhone);
            mQuantityTextView.setText(String.valueOf(bookQuantity));
            mPriceEditText.setText(String.valueOf(bookPrice));

            // Gender is a dropdown spinner, so map the constant value from the database
            // into one of the dropdown options (0 is Unknown, 1 is Supplier One, Supplier Two).
            // Then call setSelection() so that option is displayed on screen as the current selection.
            switch (bookSupplierName) {
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

        }

    }*/

   /* @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        // If the loader is invalidated, clear out all the data from the input fields.
        mNameEditText.setText("");
        mQuantityTextView.setText("");
        mPriceEditText.setText("");
        mSupplierSpinner.setSelection(0); // Select "Unknown" supplier
        mPhoneEditText.setText("");
    }*/

    /**
     * Show a dialog that warns the user there are unsaved changes that will be lost
     * if they continue leaving the editor.
     *
     * @param discardButtonClickListener is the click listener for what to do when
     *                                   the user confirms they want to discard their changes
     */
 /*   private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the book.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }*/
}
