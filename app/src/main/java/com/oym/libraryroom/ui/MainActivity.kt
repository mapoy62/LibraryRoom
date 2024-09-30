package com.oym.libraryroom.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.oym.libraryroom.R
import com.oym.libraryroom.application.LibraryRoomApp
import com.oym.libraryroom.data.BookRepository
import com.oym.libraryroom.data.db.model.BookEntity
import com.oym.libraryroom.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var books: MutableList<BookEntity> = mutableListOf()
    private lateinit var repository: BookRepository

    private lateinit var bookAdapter: BookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        repository = (application as LibraryRoomApp).repository
        bookAdapter = BookAdapter { selectedBook ->
            //Click al registro de cada libro
            val dialog = BookDialog(newBook = false, book = selectedBook, updateUI = {
                updateUI()
            }, message = { text ->
                message(text)
            })

            dialog.show(supportFragmentManager, "dialog2")
        }

        binding.rvBooks.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = bookAdapter
        }

        updateUI()
    }

    fun click(view: View){
        val dialog = BookDialog(updateUI = {
            updateUI()
        }, message = { text ->
            message(text)
        })

        dialog.show(supportFragmentManager, "dialog1")
    }

    private fun message(text: String){
        Snackbar.make(
            binding.cl,
            text,
            Snackbar.LENGTH_SHORT
        )
            .setTextColor(getColor(R.color.white))
            .setBackgroundTint(getColor(R.color.snackbar))
            .show()
    }

    private fun updateUI(){
        lifecycleScope.launch {
            books = repository.getAllBooks()

            binding.tvNoRegisters.visibility =
                if(books.isNotEmpty()) View.INVISIBLE else View.VISIBLE

            bookAdapter.updateList(books)
        }
    }
}