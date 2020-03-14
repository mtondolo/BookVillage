package com.example.android.bookvillage;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.bookvillage.data.AppDatabase;

public class EditorViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final AppDatabase mDb;
    private final int mBookId;

    public EditorViewModelFactory(AppDatabase db, int bookId) {
        mDb = db;
        mBookId = bookId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new EditorViewModel(mDb, mBookId);
    }
}
