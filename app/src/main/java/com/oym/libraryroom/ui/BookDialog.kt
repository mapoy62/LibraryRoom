package com.oym.libraryroom.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import com.oym.libraryroom.R
import com.oym.libraryroom.application.LibraryRoomApp
import com.oym.libraryroom.data.BookRepository
import com.oym.libraryroom.data.db.model.BookEntity
import com.oym.libraryroom.databinding.BookDialogBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class BookDialog(
    private val newBook: Boolean = true,
    private var book: BookEntity = BookEntity(
        title = "",
        author = "",
        genre = "",
        publishedYear = 0
    ),
    private val updateUI: () -> Unit,
    private val message: (String) -> Unit
): DialogFragment() {
    private var _binding: BookDialogBinding? = null
    private val binding get() = _binding!!

    private lateinit var builder: AlertDialog.Builder
    private lateinit var dialog: Dialog

    private var saveButton: Button? = null

    private lateinit var repository: BookRepository

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = BookDialogBinding.inflate(requireActivity().layoutInflater)

        repository = (requireContext().applicationContext as LibraryRoomApp).repository
        builder = AlertDialog.Builder(requireContext())

        binding.apply {
            tietTitle.setText(book.title)
            tietAuthor.setText(book.author)
            tietPublishedYear.setText(book.publishedYear.toString())

            //Configuración de spinner
            val genres = arrayOf("Select a genre","Drama", "Fiction", "Non-fiction", "Poetry", "Essay", "Journalism")
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, genres)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerGenre.adapter = adapter

            val genrePosition = genres.indexOf(book.genre)
            if (genrePosition >= 0) {
                spinnerGenre.setSelection(genrePosition)
            }

            spinnerGenre.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    // Llama a validateFields cada vez que se selecciona un nuevo elemento
                    saveButton?.isEnabled = validateFields()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // No se necesita acción aquí
                }
            })

        }

        dialog = if (newBook)
            buildDialog("Save", "Cancel",{
                binding.apply {
                    book.apply {
                        title = tietTitle.text.toString()
                        author = tietAuthor.text.toString()
                        genre = spinnerGenre.selectedItem.toString()
                        publishedYear = tietPublishedYear.text.toString().toIntOrNull() ?: 0
                    }
                }

                try{
                    lifecycleScope.launch(Dispatchers.IO) {
                        val result = async {
                            repository.addBook(book)
                        }
                        result.await()

                        withContext(Dispatchers.Main){
                            message("Book saved succesfully!")

                            updateUI()
                        }
                    }
                }catch (e: IOException){
                    message("Error saving the book :c")
                }
        },{
            //Action to cancel
            })
        else
            buildDialog("Update", "Delete", {
            binding.apply {
                book.apply {
                    title = tietTitle.text.toString()
                    author = tietAuthor.text.toString()
                    genre = spinnerGenre.selectedItem.toString()
                    publishedYear = tietPublishedYear.text.toString().toIntOrNull() ?: 0
                }
            }
            try {
                lifecycleScope.launch (Dispatchers.IO){
                    val result = async {
                        repository.updateBook(book)
                    }
                    result.await()

                    withContext(Dispatchers.Main){
                        message("Book updated successfully!")
                        updateUI()
                    }
                }
            }catch (e: IOException) {
                message("Error updating the book :c")
            }
        },{
            val context = requireContext()
            AlertDialog.Builder(requireContext())
                .setTitle("Confirmation")
                .setMessage("Do you really want to delete the book {book.title}")
                .setPositiveButton("Accept"){_,_ ->
                    try {
                        lifecycleScope.launch(Dispatchers.IO) {
                            val result = async {
                                repository.deleteBook(book)
                            }
                            result.await()
                            withContext(Dispatchers.Main){
                                message("Book deleted successfully!")
                                updateUI
                            }
                        }
                    }catch (e: IOException){
                        message("There was an error deleting the book")
                    }
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .create().show()
            })
        return dialog
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        val alertDialog = dialog as AlertDialog

        saveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)

        saveButton?.isEnabled = false

        binding.apply {
            setupTextWatcher()
        }

        binding.apply {
            setupTextWatcher(
                tietTitle,
                tietAuthor,
                tietPublishedYear
            )
        }
    }

    private fun validateFields(): Boolean
        = binding.tietTitle.text.toString().isNotEmpty() &&
            binding.tietAuthor.text.toString().isNotEmpty() &&
            binding.tietPublishedYear.text.toString().isNotEmpty() &&
            binding.spinnerGenre.selectedItemPosition != 0

    private fun setupTextWatcher(vararg textFields: TextInputEditText){
        val textWatcher = object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }
        }

        textFields.forEach { textField ->
            textField.addTextChangedListener(textWatcher)
        }
    }

    private fun buildDialog(
        btn1Text: String,
        btn2Text: String,
        positiveButton: () -> Unit,
        negativeButton: () -> Unit
    ): Dialog =
        builder.setView(binding.root)
            .setTitle("Book")
            .setPositiveButton(btn1Text){_, _ ->
                positiveButton()
            }.setNegativeButton(btn2Text){_, _ ->
                negativeButton()
            }
            .create()
}