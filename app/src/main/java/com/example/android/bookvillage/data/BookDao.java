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

    // A Query method that receives an int id and returns a BookEntry Object
    // The query for this method gets all the data for that id in the book table
    @Query("SELECT * FROM books WHERE id = :id")
    BookEntry loadBookById(int id);
}
