package com.example.traveller

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.room.CoroutinesRoom
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.traveller.adapter.EntryAdapter
import com.example.traveller.camera.CameraActivity
//import com.example.traveller.camera.localisation.LocalisationServicesClient
import com.example.traveller.database.AppDatabase
import com.example.traveller.database.Entry
import com.example.traveller.databinding.ActivityMainBinding
import com.example.traveller.service.BoundedService
import com.example.traveller.service.ForegroundService
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.Executor

const val NOTIFICATION_CHANNEL_DEFAULT = "com.example.service.DEFAULT"

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val settingsFragment: SettingsFragment by lazy { SettingsFragment() }
    private val view by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val firstFragment: FirstFragment = FirstFragment()

//        private val fragment = findViewById<View>(R.id.FirstFragment)
    private lateinit var database: AppDatabase

    val serviceConnection = object :
        ServiceConnection { // podpiecie do serwisu (a konkretnie do interfejsu ServiceConnection)
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            this@MainActivity.service = (service as BoundedService.Bound).service
        }

        override fun onServiceDisconnected(name: ComponentName?) {

        }
    }

    var service: BoundedService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        database = Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "local-database"
        ).build()
//        firstFragment = FirstFragment()
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        registerChannel()
        getDataFromDb()

        binding.fab.setOnClickListener { view ->
            startActivity(Intent(this, CameraActivity::class.java))
        }
    }

    private fun getDataFromDb() {
        lifecycleScope.launch(Dispatchers.IO) {
            val entryDao = database.entryDao()
            val listOfEntries: List<Entry> = entryDao.getAll()
            withContext(Dispatchers.Main) {
                firstFragment.setAdapter(listOfEntries, baseContext)
                supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_first_entry_list_recyclerView, firstFragment).commit()
            }
        }
    }

    override fun onResume() {
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        super.onResume()
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
        return when (item.itemId) {
            R.id.action_settings -> startSettingsTransaction()
            android.R.id.home -> {
                supportFragmentManager
                    .beginTransaction()
                    .detach(settingsFragment)
                    .commit()
//                supportFragmentManager.popBackStack()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun startSettingsTransaction(): Boolean {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.root_container, settingsFragment)
            .addToBackStack(null)
            .commit()
        return true
    }

    // zrobić fragment jak w Paint z ćwiczeń (z ekranu fragment_first)

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
//                supportFragmentManager.commit {
//                    setReorderingAllowed(true)
//                    remove(SettingsFragment())
//                    supportFragmentManager.popBackStack()
                supportFragmentManager
                    .beginTransaction()
                    .detach(SettingsFragment())
                return true
            }
        }
        return false
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    fun hasPermissions(): Boolean {
//        if (applicationContext != null ){
//            ActivityCompat.checkSelfPermission(this, it)
//        }
        return false
    }

    fun startForegroundService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(Intent(this, ForegroundService::class.java))
        } else {
            startService(Intent(this, ForegroundService::class.java))
        }
    }

    fun registerChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_DEFAULT,
                "General", // nazwa widoczna dla uzytkownika
                importance
            )
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

//    private fun onEditAction(model: Entry) {
//
//    }
//
//    private fun onRemoveAction(model: Entry) : Boolean {
//        return false
//    }
}