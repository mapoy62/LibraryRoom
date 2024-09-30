package com.oym.libraryroom.application

import android.app.Application
import com.oym.libraryroom.data.BookRepository
import com.oym.libraryroom.data.db.BookDataBase

class LibraryRoomApp: Application() {

    private val database by lazy {
        BookDataBase.getDatabase(this@LibraryRoomApp)
    }

    val repository by lazy {
        BookRepository(database.bookDAO())
    }
}