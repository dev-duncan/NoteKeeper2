package com.example.notekeeperk

import android.content.Context
import android.content.SharedPreferences
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.preference.PreferenceManager
import android.text.Editable

object preferenceHelper{
    fun defaultPrefs(context:Context):SharedPreferences{
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun customPrefs(context: Context, name: String): SharedPreferences{
        return context.getSharedPreferences(name, Context.MODE_PRIVATE)
    }

        inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) ->Unit){
            val editor: SharedPreferences.Editor = this.edit()
            operation(editor)
            editor.apply()

    }

    operator fun SharedPreferences.set(key: String, value: Any){
        when (value){
            is Editable ->{
                if (value.toString() != ("")){
                    edit({it.putString(key, value.toString())})
                }
            }
            is String ->{
                if (value != ("")){
                    edit {it.putString(key, value)}
                }
            }
            is Int -> edit {it.putInt(key, value)}
            is Boolean -> edit {it.putBoolean(key, value)}
            is Float -> edit({it.putFloat(key, value)})
            is Long -> edit({it.putLong(key, value)})
            else -> throw UnsupportedOperationException("Unsupported operation")
        }

    }

    inline operator fun <reified T> SharedPreferences.get(key: String, defaultValue: T? = null): T? {
        return when (T::class) {
            String::class -> getString(key, defaultValue as? String) as T?
            Int::class -> getInt(key, defaultValue as? Int ?: -1) as T?
            Boolean::class -> getBoolean(key, defaultValue as? Boolean ?: false) as T?
            Float::class -> getFloat(key, defaultValue as? Float ?: -1f) as T?
            Long::class -> getLong(key, defaultValue as? Long ?: -1) as T?
            else -> throw UnsupportedOperationException("Unsupported Operation")
        }
    }
}