package com.example.notekeeperk

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.rule.ActivityTestRule
import com.example.notekeeperk.ui.activities.ListActivity
import org.junit.Rule




@RunWith(AndroidJUnit4::class)
class CreateNewNoteTest{

    @Rule @JvmField
    val listActivity = ActivityTestRule(ListActivity::class.java)

    @Test
    fun createNewNote(){
        val  noteTitle = "Test note title"
        val  noteText = "This is the body of our test note"

        onView(withId(R.id.fab)).perform(click())

        onView(withId(R.id.textNoteTitle)).perform(typeText(noteTitle))
        onView(withId(R.id.textNoteText)).perform(typeText(noteText))

    }
}