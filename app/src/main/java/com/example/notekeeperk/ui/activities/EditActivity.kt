package com.example.notekeeperk.ui.activities


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.notekeeperk.NOTE_POSITION
import com.example.notekeeperk.NoteGetTogetherHelper
import com.example.notekeeperk.POSITION_NOT_SET
import com.example.notekeeperk.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import com.example.data.CourseInfo as NotekeeperkCourseInfo

class NoteActivity : AppCompatActivity() {

    private var notePosition = POSITION_NOT_SET

    val noteGetTogetherHelper = NoteGetTogetherHelper(this,lifecycle)
    private val tag = this::class.simpleName
     var mCancelling: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        initSpinner()
        getIncomingIntent(savedInstanceState)

    }



    private fun getIncomingIntent(savedInstanceState: Bundle?) {
        //this will run in case its recreation
        notePosition = savedInstanceState?.getInt(NOTE_POSITION, POSITION_NOT_SET)?:
        //this will run in case its a new note
        intent.getIntExtra(NOTE_POSITION, POSITION_NOT_SET)
        if (notePosition != POSITION_NOT_SET)
           displayNotes()
        else{
            com.example.data.DataManager.notes.add(com.example.data.NoteInfo())
            notePosition = com.example.data.DataManager.notes.lastIndex

        }
    }

    private fun displayNotes() {
        if(notePosition > com.example.data.DataManager.notes.lastIndex) {
            Log.d(tag, "note position $notePosition")
            showMessage("Note not found")
            return
        }

            val note = com.example.data.DataManager.notes[notePosition]
            textNoteTitle.setText(note.title).toString()
            textNoteText.setText(note.text)

            val coursePosition = com.example.data.DataManager.courses.values.indexOf(note.course)
            spinnercourses.setSelection(coursePosition)

    }

    private fun  initSpinner() {
        val adapterCourses = ArrayAdapter(this, android.R.layout.simple_spinner_item,
            com.example.data.DataManager.courses.values.toList())
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnercourses.adapter = adapterCourses

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
         when (item.itemId) {
            R.id.action_cancel ->{
                 mCancelling = true
                finish()

            }

            R.id.action_send_email ->{
                sendEmail()

            }

            R.id.action_next -> {
                if (notePosition< com.example.data.DataManager.notes.lastIndex){
                moveNext()
            }else {
                    val message = "NO More Notes To Display"
                    showMessage(message)
            }

        }
            R.id.action_cancel ->{
                if (notePosition< com.example.data.DataManager.notes.size){
                movePrevious()
                }
            }
             R.id.action_meet ->{

             }


            else -> super.onOptionsItemSelected(item)


        }
        return true
    }

    private fun sendEmail() {
        val course = spinnercourses.selectedItem as NotekeeperkCourseInfo
        val subject = textNoteTitle.text.toString()
        val text = "check out what i learned in the pluralSight course \"" +
                course + "\"n" + textNoteText.text

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "message/rfc2822"
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(Intent.EXTRA_TEXT, text)
        startActivity(intent)

    }

    private fun showMessage(message: String) {
        Snackbar.make(textNoteTitle, message, Snackbar.LENGTH_LONG)
            .show()
    }

    private fun moveNext() {
        ++notePosition
        displayNotes()
        invalidateOptionsMenu()
    }

    private fun movePrevious (){
      --notePosition
        displayNotes()
        invalidateOptionsMenu()
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if(notePosition >= com.example.data.DataManager.notes.lastIndex) {
            val menuItem = menu?.findItem(R.id.action_next)
            if (menuItem !=null ){
                menuItem.isEnabled = false
            }

        }
        return super.onPrepareOptionsMenu(menu)

    }

    override fun onPause() {
        super.onPause()

        saveNote()
    }

    private fun saveNote() {
        val note = com.example.data.DataManager.notes[notePosition]
        note.title = textNoteTitle.text.toString()
        note.text = textNoteText.text.toString()
        note.course = spinnercourses.selectedItem  as NotekeeperkCourseInfo
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(NOTE_POSITION, notePosition)
    }
}
