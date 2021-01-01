package com.example.notekeeperk.ui.activities



import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.AppBarConfiguration
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.data.NoteInfo
import com.example.notekeeperk.*
import com.example.notekeeperk.Adapter.NoteRecycleAdapter
import com.example.notekeeperk.databinding.ActivityListBinding
import com.example.notekeeperk.dublicate.FIRST_TIME_LOGIN
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.jwhh.notekeeper.CourseRecyclerAdapter
import kotlinx.android.synthetic.main.activity_list.*
import kotlinx.android.synthetic.main.content_list.*
import kotlinx.android.synthetic.main.layout_settings_toolbar.*


class ListActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener, Items,
    ChangePhotoDialog.OnPhotoReceivedListener
{
    private val maxRecentlyViewNotes =5
    val recentlyViewedNotes = ArrayList<NoteInfo>(maxRecentlyViewNotes)


    private var settingsFragment: SettingsFragment? = null
    private var accountFragment: AccountFragment? = null

    val TAG: String = "ListActivity"

    private val noteLayoutManager by lazy { LinearLayoutManager(this) }
    private val noteRecycleAdapter by lazy {NoteRecycleAdapter(this, com.example.data.DataManager.notes) }

    private val courseLayoutManager by lazy { GridLayoutManager(this, 2) }
    private val courseRecycleAdapter by lazy {CourseRecyclerAdapter(this, com.example.data.DataManager.courses.values.toList()) }
    private val mViewModel by lazy {ViewModelProvider(this).get(ItemsActivityViewModel::class.java)  }


    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityListBinding = DataBindingUtil.setContentView(this,R.layout.activity_list)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        isFirstTimeLogin()
        initFab()

        //restore activity durable persistence state in case of system shut down
        if (mViewModel.isFirstCreated  && savedInstanceState != null)
        mViewModel.restoreState(savedInstanceState)
        mViewModel.isFirstCreated = false

         handleDisplaySelection(mViewModel.mNavDrawerDisplaySelection)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        //  val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_notes, R.id.nav_courses, R.id.nav_slideshow
            ), drawerLayout
        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener (this)

    }

    override fun onStart() {
        super.onStart()

    }

    private fun initFab() {
        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->

            startActivity(Intent(this, NoteActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        }
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)
        if (outState != null)
            mViewModel.saveState(outState)

    }

    private fun isFirstTimeLogin() {
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val isFirstLogin = pref.getBoolean(FIRST_TIME_LOGIN, true)

        if (isFirstLogin){
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setMessage(R.string.first_time_user_message)
            alertDialogBuilder.setPositiveButton("ok") { dialogInterface: DialogInterface, i: Int ->
                val editor =pref.edit()
                editor.putBoolean(FIRST_TIME_LOGIN, false)
                editor.apply()
                dialogInterface.dismiss()
            }

            alertDialogBuilder.setIcon(R.drawable.ic_menu_camera)
            alertDialogBuilder.setTitle("")
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }
    }


    private fun initRecycleView() {
        listItems.layoutManager = noteLayoutManager
        listItems.adapter = noteRecycleAdapter
//       val db: SQLiteDatabase? = mDBopenHelper?.readableDatabase
        nav_view.menu.findItem(R.id.nav_notes).isChecked = true
        nav_view.menu.findItem(R.id.nav_courses).isChecked = false
    }

    private fun displayCourses() {
        listItems.layoutManager = courseLayoutManager
        listItems.adapter = courseRecycleAdapter
        nav_view.menu.findItem(R.id.nav_courses).isChecked = true
        nav_view.menu.findItem(R.id.nav_notes).isChecked = false

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
        correctSettingsToolBarVisibility()
    }


    private fun correctSettingsToolBarVisibility(){
        if(settingsFragment!=null){
            if(settingsFragment!!.isVisible){
            showSettingsAppBar()
        }else{
            hideSettingsAppBar()
        }
        return
    }
            hideSettingsAppBar()

    }

    override fun onResume() {
        super.onResume()
        listItems.adapter?.notifyDataSetChanged()
        upDateNavHeader()
        openDrawer()
    }

    private fun openDrawer() {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
            drawer.openDrawer(GravityCompat.START)
        }, 1000)
    }

    private fun upDateNavHeader() {
        val navHeader = findViewById<NavigationView>(R.id.nav_view)
        val headerView = navHeader.getHeaderView(0)

        val textEmailAddress = headerView.findViewById<TextView>(R.id.text_email_address)
        val textUserName = headerView.findViewById<TextView>(R.id.text_user_name)
        val pref = PreferenceManager.getDefaultSharedPreferences(this)

        textEmailAddress.text = pref.getString("user_email_address", "")
        textUserName.text = pref.getString("user_display_name", "")

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.list, menu)
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_notes -> {
                handleDisplaySelection(R.id.nav_notes)
                mViewModel.mNavDrawerDisplaySelection = R.id.nav_notes
                drawer_layout.closeDrawer(GravityCompat.START)
            }
            R.id.nav_courses ->{
                handleDisplaySelection(R.id.nav_courses)
                mViewModel.mNavDrawerDisplaySelection = R.id.nav_courses
                drawer_layout.closeDrawer(GravityCompat.START)
            }
            R.id.nav_recent ->{


            }
            R.id.send -> {
                handleSelection("send")
            }
        }
        return false
    }
    private fun handleDisplaySelection(itemId : Int){
        when(itemId){
            R.id.nav_notes ->{
                initRecycleView()
            }
            R.id.nav_courses ->{
                displayCourses()

            }
        }

    }

    private fun handleSelection(message: String) {
        Snackbar.make(listItems,message,Snackbar.LENGTH_LONG).show()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_settings -> {
                //temporary method
              //  inflateAccountSetting()
               inflateSettingsFragment()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun inflateAccountFragment() {
        printToLog("inflating account setting fragment")
        if (accountFragment == null){
            accountFragment = AccountFragment()
        }
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.account_container, accountFragment!!, FRAGMENT_ACCOUNT )
        transaction.addToBackStack(FRAGMENT_ACCOUNT)
        printToLog("inflating account setting fragment2")

        transaction.commit()
    }

    private fun inflateSettingsFragment(){
        Log.d(TAG, "inflating settings fragment")
        if (settingsFragment == null){
            settingsFragment = SettingsFragment()
        }
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.settings_container,settingsFragment!!, FRAGMENT_SETTINGS)
        transaction.addToBackStack(FRAGMENT_ACCOUNT)
        transaction.commit()


    }

    override fun showSettingsAppBar() {
        settings_app_bar.visibility = View.VISIBLE
    }

    override fun hideSettingsAppBar() {
        settings_app_bar.visibility = View.GONE
    }

    override fun setImageUri(imageUri: Uri?) {
        accountFragment!!.setImageUri(imageUri)
    }

    private fun printToLog(message: String?){
        Log.d(TAG, message)
    }

    override fun onDestroy() {

        super.onDestroy()
    }

}
