package com.example.evav3

import android.os.Bundle
import android.view.Menu
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.evav3.databinding.ActivityMainBinding // Import ViewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint // For Hilt dependency injection
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding // Declare ViewBinding variable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate layout using ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the Toolbar - Use the correct ID from activity_main.xml
        setSupportActionBar(binding.toolbar) // FIX: Access toolbar directly

        val drawerLayout: DrawerLayout = binding.drawerLayout // Access DrawerLayout via binding
        val navView: NavigationView = binding.navView // Access NavigationView via binding
        // Find the NavController - Use the correct ID from activity_main.xml
        val navController = findNavController(R.id.nav_host_fragment) // FIX: Use correct NavHostFragment ID

        // Define top-level destinations. These IDs should match your menu item IDs
        // in activity_main_drawer.xml and fragment IDs in mobile_navigation.xml
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_chat, R.id.navigation_call, R.id.navigation_memories, R.id.navigation_settings
            ), drawerLayout
        )

        // Connect the ActionBar (Toolbar) with the NavController and AppBarConfiguration
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Connect the NavigationView with the NavController
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        // Use the correct ID from activity_main.xml
        val navController = findNavController(R.id.nav_host_fragment) // FIX: Use correct NavHostFragment ID
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}