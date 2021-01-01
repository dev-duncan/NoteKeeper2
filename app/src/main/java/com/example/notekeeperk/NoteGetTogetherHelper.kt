package com.example.notekeeperk

import android.content.Context
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class NoteGetTogetherHelper(val context: Context, val lifecycle: Lifecycle): LifecycleObserver {
    init {
        lifecycle.addObserver(this)
    }
    val tag = this::class.simpleName
    var currentLat = 0.0
    var currentLon = 0.0

    private val locManager = PseudoLocationManager(context){ lat, lon ->
        currentLat = lat
        currentLon = lon

        Log.d(tag, "Location callback lat:$currentLat lon:$currentLon")

    }
     @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun startHandler(){
         Log.d(tag, "startHandler")
        locManager.start()
       
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stopHandler(){
        Log.d(tag, "stopHandler")

        locManager.stop()
    }
}