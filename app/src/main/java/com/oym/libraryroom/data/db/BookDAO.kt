package com.oym.libraryroom.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.oym.libraryroom.data.db.model.BookEntity
import com.oym.libraryroom.util.Constants

@Dao
interface BookDAO {
    @Query("SELECT * FROM ${Constants.DATABASE_BOOK_TABLE}")
    suspend fun getAllBooks(): MutableList<BookEntity>

    @Insert
    suspend fun addBook(book: BookEntity)

    @Insert
    suspend fun addBooks(books: MutableList<BookEntity>)

    @Update
    suspend fun updateBook(book: BookEntity)

    @Delete
    suspend fun deleteBook(book: BookEntity)

}