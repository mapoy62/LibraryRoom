package com.oym.libraryroom.data

import com.oym.libraryroom.data.db.BookDAO
import com.oym.libraryroom.data.db.model.BookEntity

class BookRepository(
    private val bookDao: BookDAO
) {
    suspend fun getAllBooks(): MutableList<BookEntity> = bookDao.getAllBooks()

    suspend fun addBook(book: BookEntity){
        bookDao.addBook(book)
    }

    suspend fun updateBook(book: BookEntity){
        bookDao.updateBook(book)
    }

    suspend fun  deleteBook(book: BookEntity){
        bookDao.deleteBook(book)
    }

}