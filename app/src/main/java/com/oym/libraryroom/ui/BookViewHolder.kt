package com.oym.libraryroom.ui

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.oym.libraryroom.R
import com.oym.libraryroom.data.db.model.BookEntity
import com.oym.libraryroom.databinding.BookElementBinding

class BookViewHolder(
    private val binding: BookElementBinding
): RecyclerView.ViewHolder(binding.root) {

    //Mappeo de imágenes por género
    private val genreImageMap = mapOf(
        "Drama" to R.drawable.ic_drama,
        "Fiction" to R.drawable.ic_fiction,
        "Non-fiction" to R.drawable.ic_non_fiction,
        "Poetry" to R.drawable.ic_poetry,
        "Essay" to R.drawable.ic_essay,
        "Journalism" to R.drawable.ic_journalism
    )

    fun bind(book: BookEntity){
        binding.apply {
            tvTitle.text = book.title
            tvAuthor.text = book.author
            tvGenre.text = book.genre
            tvPublishedYear.text = book.publishedYear.toString()

            // Obtener el recurso de imagen basado en el género
            val imageResId = genreImageMap[book.genre] ?: R.drawable.book

            // Cargar la imagen usando Glide
            Glide.with(itemView.context)
                .load(imageResId)
                .into(ivIcon)
        }
    }
}