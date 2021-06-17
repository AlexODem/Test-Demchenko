package com.example.sparktestdemchenko.ui

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.sparktestdemchenko.R
import com.example.sparktestdemchenko.databinding.ActivityLaunchBinding
import com.example.sparktestdemchenko.ui.fragment.MessageDetailsFragment
import com.example.sparktestdemchenko.ui.fragment.MessageListFragment
import com.example.sparktestdemchenko.ui.util.screenSize


class LaunchActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityLaunchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLaunchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        setThemeConfiguration()

        if (resources.screenSize > Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            supportFragmentManager.beginTransaction().add(R.id.containerFragmentList, MessageListFragment()).commit()
            supportFragmentManager.beginTransaction().add(R.id.containerFragmentDetails, MessageDetailsFragment()).commit()
        } else {
            val navController = findNavController(R.id.nav_host_fragment_content_main)
            appBarConfiguration = AppBarConfiguration(navController.graph)
            setupActionBarWithNavController(navController, appBarConfiguration)
        }
    }


    override fun onResume() {
        super.onResume()
        setToolbarTitle(getString(R.string.inbox))
    }

    fun setToolbarTitle(title: String) {
        supportActionBar?.title = title
    }


    private fun setThemeConfiguration() {
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}