package com.oym.libraryroom.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.oym.libraryroom.data.db.model.BookEntity
import com.oym.libraryroom.util.Constants

@Database(
    entities = [BookEntity::class],
    version = 1,
    exportSchema = true
)
abstract class BookDataBase : RoomDatabase() {

    abstract fun bookDAO(): BookDAO

    companion object{

        @Volatile
        private var INSTANCE: BookDataBase? = null

        fun getDatabase(context: Context): BookDataBase{
            //Utilizando SINGLETON para instanciar
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BookDataBase::class.java,
                    Constants.DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance

                instance
            }
        }
    }
}