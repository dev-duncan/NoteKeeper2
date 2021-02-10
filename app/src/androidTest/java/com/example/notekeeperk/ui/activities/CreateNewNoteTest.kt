package com.example.notekeeperk.ui.activities

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.Matchers.*
import androidx.test.espresso.action.ViewActions.*
import com.example.data.CourseInfo
import com.example.data.DataManager
import com.example.notekeeperk.R
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import org.junit.Rule


@RunWith(AndroidJUnit4::class )
class CreateNewNoteTest{
    @Rule @JvmField
 val listActivity = ActivityScenarioRule(ListActivity::class.java)

         @Test
        fun createNewNote(){

            val course = DataManager.courses["android_async"]
            val noteTitle = "Test Note Title"
            val noteText = "This is the body of our test note"


            onView(withId(R.id.fab)).perform(click())

            onView(withId(R.id.spinnercourses)).perform(click())
            onData(allOf(instanceOf(CourseInfo::class.java), equalTo(course))).perform(click())

            onView(withId(R.id.textNoteTitle)).perform(typeText(noteTitle))
            onView(withId(R.id.textNoteText)).perform(typeText(noteText), closeSoftKeyboard())

             pressBack()

            val newNote = DataManager.notes.last()
            assertEquals(course, newNote.course)
            assertEquals(noteTitle, newNote.title)
            assertEquals(noteText, newNote.text)
    }

}