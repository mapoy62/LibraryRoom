package com.oym.libraryroom.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oym.libraryroom.data.db.model.BookEntity
import com.oym.libraryroom.databinding.BookElementBinding

class BookAdapter(
    private val onBookClicked: (BookEntity) -> Unit
) : RecyclerView.Adapter<BookViewHolder>() {

    private var books: MutableList<BookEntity> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = BookElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookViewHolder(binding)
    }

    override fun getItemCount(): Int = books.size

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]

        holder.bind(book)

        //Manejando el click del elemento
        holder.itemView.setOnClickListener{
            onBookClicked(book)
        }
    }

    fun updateList(list: MutableList<BookEntity>){
        books = list
        println("Se agregóooooo LIBROUSSS!!!!")
        notifyDataSetChanged()
    }

}