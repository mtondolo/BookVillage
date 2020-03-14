package com.example.android.bookvillage;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.bookvillage.data.AppDatabase;
import com.example.android.bookvillage.data.BookEntry;

public class EditorViewModel extends ViewModel {
    private LiveData<BookEntry> book;

    public EditorViewModel(AppDatabase database, int bookId) {
        book = database.bookDao().loadBookById(bookId);
    }

    public LiveData<BookEntry> getBook() {
        return book;
    }
}
