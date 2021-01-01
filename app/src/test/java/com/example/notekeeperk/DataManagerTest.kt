package com.example.notekeeperk

import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class DataManagerTest {

    @Before
    fun setUp() {
        com.example.data.DataManager.notes.clear()
        com.example.data.DataManager.initializeNotes()
    }

    @Test
    fun addNote() {
        val course = com.example.data.DataManager.courses.get("android_async")!!
        val noteTitle = "This is a test note"
        val noteText = "This is the body of my  test note"

        val index = com.example.data.DataManager.addNote(course,noteTitle,noteText)
        val note = com.example.data.DataManager.notes[index]

        assertEquals(course,note.course)
        assertEquals(note.title,note.title)
        assertEquals(noteText,note.text)
    }

    @Test
    fun findSimilarNotes(){
        val course = com.example.data.DataManager.courses["android_async"]
        val noteTitle = "This is a note test"
        val noteText1 = "This is the body the of my test note"
        val noteText2 = "Ths is theX body of my second test note"

        val index1 = com.example.data.DataManager.addNote(course!!,noteTitle,noteText1)
        val index2 = com.example.data.DataManager.addNote(course,noteTitle,noteText2)

        val note1 : com.example.data.NoteInfo? = com.example.data.DataManager.findNote(course, noteTitle, noteText1)
        val foundIndex1 :Int = com.example.data.DataManager.notes.indexOf(noteText1)
        assertEquals(index1, foundIndex1)

        val note2 : com.example.data.NoteInfo? = com.example.data.DataManager.findNote(course, noteTitle, noteText1)
        val foundIndex2: Int = com.example.data.DataManager.notes.indexOf(noteText2)
        assertEquals(index2, foundIndex2)


    }

}