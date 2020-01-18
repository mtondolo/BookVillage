package com.example.android.bookvillage.data;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


@Dao
public interface BookDao {

    @Query("SELECT * FROM books ORDER BY id")
    List<BookEntry> loadAllBooks();

    @Insert
    void insertBook(BookEntry bookEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateBook(BookEntry bookEntry);

    @Delete
    void deleteBook(BookEntry bookEntry);
}
