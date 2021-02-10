package com.example.notekeeperk.ui.activities

import androidx.recyclerview.widget.RecyclerView
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Rule
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
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import com.example.data.NoteInfo
import com.example.notekeeperk.Adapter.NoteRecycleAdapter


@RunWith(AndroidJUnit4::class)
class NextThroughNoteTest{
    @Rule @JvmField
    val listActivity = ActivityScenarioRule(ListActivity::class.java)

    @Test
    fun nextThroughNotes(){


        onView(withId(R.id.listItems))
            .perform(RecyclerViewActions
                .actionOnItemAtPosition<NoteRecycleAdapter.ViewHolder>(0, click()))
        
        for (index in 0..DataManager.notes.lastIndex){
            val note = DataManager.notes[index]

            if (index != DataManager.notes.lastIndex)
                onView(allOf(withId(R.id.action_next),isEnabled())).perform(click())


        }

        onView(withId(R.id.action_next)).check(matches(isClickable()))



    }

    @Test
    fun clickOnRecycleview_toGoToTheNextActivity(){

//        onView(R.id.li)

        onView(withId(R.id.action_settings))
            .perform(click())

       pressBack()
    }
}