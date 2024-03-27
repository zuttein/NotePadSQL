package com.example.notepadsql

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.notepadsql.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    //MVC

    lateinit var binding: ActivityMainBinding
    lateinit var databaseHandler: DatabaseHandler

    var selectedItemPosition = -1
    var currentNoteItem: Note? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHandler = DatabaseHandler(this)

        binding.btnAdd.setOnClickListener{
            addNote()
        }
        showNotes()

        binding.lvNotes.onItemLongClickListener = AdapterView.OnItemLongClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position) as Note
            deleteNote(selectedItem)
        }

        binding.lvNotes.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            if (selectedItemPosition != -1){
                val previousSelectedItem = parent.getChildAt(selectedItemPosition)
                previousSelectedItem.setBackgroundColor(Color.TRANSPARENT)

            }

            selectedItemPosition = position
            currentNoteItem = parent.getItemAtPosition(position) as Note
            view.setBackgroundColor(Color.GREEN)
        }

        binding.btnUpdate.setOnClickListener {

            if (currentNoteItem != null){
                updateUser()
            }
        }



    }
    fun addNote(){
        try {
            val title = binding.title.text.toString()
            val text = binding.breadtext.text.toString()

            val note = Note(-1, title, text)

            databaseHandler.addNote(note)

            binding.title.text.clear()
            binding.breadtext.text.clear()
            showNotes()
        }
        catch (e: Exception){
            println(e.stackTrace)
            Toast.makeText(this, "Error Creating Note", Toast.LENGTH_SHORT).show()
        }
    }

    fun showNotes(){
        val noteList = databaseHandler.getAllNotes()

        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,noteList)
        binding.lvNotes.adapter = arrayAdapter
    }
    fun deleteNote(note: Note): Boolean{
        val result = databaseHandler.deleteNote(note)
        showNotes()
        return result
    }

    fun updateUser(){
        try{
            val title = binding.title.text.toString()
            val breadtext = binding.breadtext.text.toString()


            val newNote = Note(currentNoteItem!!.id,title, breadtext )

            databaseHandler.updateNote(newNote)

            binding.title.text.clear()
            binding.breadtext.text.clear()
            showNotes()
            currentNoteItem = null

        }catch (e : Exception){
            println(e.stackTrace)
            Toast.makeText(this,"Error updating user", Toast.LENGTH_SHORT).show()
        }
    }

}