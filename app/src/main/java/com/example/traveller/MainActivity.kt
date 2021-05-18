package com.example.traveller

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.*
import com.example.traveller.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
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
//                supportFragmentManager
//                    .beginTransaction()
//                    .detach(SettingsFragment())
//                    .commit()
                supportFragmentManager.popBackStack()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun startSettingsTransaction(): Boolean {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.root_container, SettingsFragment())
            .addToBackStack(null)
            .commit()
        return true
    }

    // zrobić fragment jak w Paint z ćwiczeń (z ekranu fragment_first)

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
//                finish()
//                this.onBackPressed()
//                supportFragmentManager.removeOnBackStackChangedListener(FragmentManager.OnBackStackChangedListener { })
//                supportFragmentManager.commit {
//                    setReorderingAllowed(true)
//                    remove(SettingsFragment())
//                    supportFragmentManager.popBackStack()
                supportFragmentManager
                    .beginTransaction()
                    .detach(SettingsFragment())
                return true
                }
//                return true
            }
        return false
        }
//        return super.onContextItemSelected(item)
//    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}