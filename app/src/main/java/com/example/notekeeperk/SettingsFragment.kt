package com.example.notekeeperk

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

class SettingsFragment: PreferenceFragmentCompat(), Preference.OnPreferenceClickListener
{
    private val TAG = "SettingsFragment"
    var items:Items? = null
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.root_preferences)

        //set preference change listener
        val accountPreference : Preference? = findPreference("key_account_settings")
        accountPreference?.setOnPreferenceClickListener { onPreferenceClick(it) }
    }

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        view?.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.white))

        items!!.showSettingsAppBar()

        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(activity.findViewById(R.id.settings_toolbar)as Toolbar)
        val actionBar = activity.supportActionBar!!
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.title = activity.getText(R.string.action_settings)
        setHasOptionsMenu(true)
        return view

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            items = (context as Items)
        }catch (e: Exception){
            printToLog(e.message)

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            items!!.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
       val menuItem :MenuItem = menu.findItem(R.id.action_settings)
        menuItem.isVisible = false

    }

    private fun printToLog(message: String?) {
        message?.let { Log.d(TAG, it) }

    }

    override fun onPreferenceClick(preference: Preference?): Boolean {
        if (preference!!.key.equals("key_account_settings")){
            items!!.inflateAccountFragment()
        }
        return true
    }
}