package com.example.android.bookvillage;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.android.bookvillage.data.AppDatabase;
import com.example.android.bookvillage.data.BookEntry;

import java.util.List;

public class CatalogViewModel extends AndroidViewModel {

    private static String TAG = CatalogViewModel.class.getSimpleName();

    private LiveData<List<BookEntry>> books;

    public CatalogViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the books from the Database");
        books = database.bookDao().loadAllBooks();
    }

    public LiveData<List<BookEntry>> getBooks() {
        return books;
    }
}
