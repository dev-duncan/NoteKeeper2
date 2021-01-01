package com.example.notekeeperk

import android.os.Bundle
import androidx.lifecycle.ViewModel

class ItemsActivityViewModel: ViewModel() {
    var isFirstCreated = true

    var navDrawerDisplaySelectionName = "com.example.notekeeperk.ItemsActivityViewModel. mNavDrawerDisplaySelection"
    var mNavDrawerDisplaySelection = R.id.nav_notes

    fun saveState(outState: Bundle) {
        outState.putInt(navDrawerDisplaySelectionName,mNavDrawerDisplaySelection)
    }

    fun restoreState(savedInstanceState: Bundle?) {
        savedInstanceState?.getInt(navDrawerDisplaySelectionName )

    }

}