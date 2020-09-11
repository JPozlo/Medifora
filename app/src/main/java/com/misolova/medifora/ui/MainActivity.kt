package com.misolova.medifora.ui

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.misolova.medifora.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerAppBarConfiguration: AppBarConfiguration
    private lateinit var listener: NavController.OnDestinationChangedListener



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.my_nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.homeFragment,
            R.id.answerRequestFragment,
            R.id.profileFragment
        ))
        drawerAppBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)


        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout,
            R.string.start,
            R.string.close
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        navigation_view.setupWithNavController(navController)
        navigation_view.setNavigationItemSelectedListener(this)


        bottom_navigation_view.setupWithNavController(navController)
        bottom_navigation_view.setOnNavigationItemSelectedListener(this)


        listener = NavController.OnDestinationChangedListener{controller, destination, arguments ->
            when(destination.id){
                R.id.homeFragment -> supportActionBar?.setBackgroundDrawable(ColorDrawable(getColor(
                    R.color.colorPrimaryDark
                )))
                R.id.profileFragment -> supportActionBar?.setBackgroundDrawable(ColorDrawable(getColor(
                    R.color.colorPrimary
                )))
                R.id.answerRequestFragment -> supportActionBar?.setBackgroundDrawable(ColorDrawable(getColor(
                    R.color.colorAccent
                )))
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.my_nav_host_fragment)
        return navController.navigateUp(drawerAppBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true
        }
        return true
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
        return super.onSupportNavigateUp()
    }
}