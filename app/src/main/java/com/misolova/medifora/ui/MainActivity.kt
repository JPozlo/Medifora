package com.misolova.medifora.ui

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.misolova.medifora.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var listener: NavController.OnDestinationChangedListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.my_nav_host_fragment)

        appBarConfiguration = AppBarConfiguration.Builder(R.id.homeFragment, R.id.answerRequestFragment,
            R.id.profileFragment)
            .setOpenableLayout(drawerLayout)
            .build()
        setupActionBarWithNavController(navController, appBarConfiguration)

        navigation_view.setupWithNavController(navController)
        navigation_view.setNavigationItemSelectedListener(this)

        bottom_navigation_view.setupWithNavController(navController)
        bottom_navigation_view.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.homeMenuItem -> findNavController(
                    R.id.my_nav_host_fragment
                ).navigate(R.id.homeFragment)
                R.id.answerRequestsMenuItem -> findNavController(
                    R.id.my_nav_host_fragment
                ).navigate(R.id.answerRequestFragment)
                R.id.userProfileMenuItem -> findNavController(
                    R.id.my_nav_host_fragment
                ).navigate(R.id.profileFragment)
            }
            return@setOnNavigationItemSelectedListener true
        }

        listener = NavController.OnDestinationChangedListener{_, destination, _ ->
            when(destination.id){
                R.id.homeFragment, R.id.profileFragment, R.id.answerRequestFragment -> {
                    bottom_navigation_view.visibility = View.VISIBLE
                }
                else -> bottom_navigation_view.visibility = View.GONE
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.my_nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.homeMenuItem -> findNavController(
                R.id.my_nav_host_fragment
            ).navigate(R.id.homeFragment)
            R.id.settingsMenuItem -> findNavController(
                R.id.my_nav_host_fragment
            ).navigate(R.id.settingsFragment)
            R.id.accountMenuItem -> findNavController(
                R.id.my_nav_host_fragment
            ).navigate(R.id.accountFragment)
            R.id.logoutMenuItem -> findNavController(
                R.id.my_nav_host_fragment
            ).navigate(R.id.logoutFragment)
            R.id.answerRequestsMenuItem -> findNavController(
                R.id.my_nav_host_fragment
            ).navigate(R.id.answerRequestFragment)
            R.id.privacyMenuItem -> findNavController(
                R.id.my_nav_host_fragment
            ).navigate(R.id.privacyFragment)
            R.id.userProfileMenuItem -> findNavController(
                R.id.my_nav_host_fragment
            ).navigate(R.id.profileFragment)
        }
        drawerLayout.closeDrawer(GravityCompat.START, false)
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}