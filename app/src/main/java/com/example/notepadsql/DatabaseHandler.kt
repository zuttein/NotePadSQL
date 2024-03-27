package com.example.notepadsql

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler (context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object{
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "NotesDatabase"
        const val TABLE_NOTES = "notes"
        const val KEY_ID = "id"
        const val KEY_TITLE = "title"
        const val KEY_TEXT = "text"


    }
    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE $TABLE_NOTES ($KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT, $KEY_TITLE TEXT, $KEY_TEXT TEXT )"

        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {

    }

    fun addNote(noteObject: Note): Boolean{
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(KEY_TITLE, noteObject.title)
        values.put(KEY_TEXT, noteObject.text)

        val insert = db.insert(TABLE_NOTES, null, values)

        db.close()

        if (insert == -1L){
            return false
        } else return true
    }
    fun updateNote(note: Note){
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_TITLE, note.title)
            put(KEY_TEXT, note.text)

        }

        db.update(TABLE_NOTES, values, "$KEY_ID = ?", arrayOf(note.id.toString()))

        db.close()
    }


    fun getAllNotes(): List <Note>{
        val notes = ArrayList <Note>()
        val query = "SELECT * FROM notes"
        val db = this.readableDatabase
        val cursor : Cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()){
            do {
                val noteId = cursor.getInt(0)
                val noteTitle = cursor.getString(1)
                val noteText = cursor.getString(2)

                val note = Note(noteId, noteTitle, noteText)
                notes.add(note)

            }while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return notes
    }

    fun deleteNote(note: Note): Boolean{
        val db = this.writableDatabase
        val query = "DELETE FROM $TABLE_NOTES WHERE $KEY_ID = ${note.id}"

        val cursor : Cursor = db.rawQuery(query, null)

        val result = cursor.moveToFirst()
        cursor.close()

        return result
    }


}