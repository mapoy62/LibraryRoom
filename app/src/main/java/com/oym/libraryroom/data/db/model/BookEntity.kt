package com.oym.libraryroom.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.oym.libraryroom.util.Constants

@Entity(tableName = Constants.DATABASE_BOOK_TABLE)
data class BookEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "book_id")
    var id: Long = 0,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "author")
    var author: String,

    @ColumnInfo(name = "genre")
    var genre: String,

    /*
    @ColumnInfo(name = "description")
    var description: String,

    @ColumnInfo(name = "numberPages")
    var numberPages: Int,
     */

    @ColumnInfo(name = "publishedYear")
    var publishedYear: Int
)