package com.misolova.medifora.ui

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.annotation.UiThread
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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var listener: NavController.OnDestinationChangedListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        GlobalScope.launch {
//            checkConnection()
//        }

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

    @UiThread
    private fun checkConnection(){
        val result = hasOnlineConnection(applicationContext)
        if(!result){
            openOfflineFragment()
        } else return
    }

    private fun openOfflineFragment(){
        val navController = findNavController(R.id.my_nav_host_fragment)
        navController.navigate(R.id.offlineFragment)
    }

    private fun hasOnlineConnection(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        Timber.d("Internet: NetworkCapabilities.TRANSPORT_CELLULAR")
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        Timber.d("Internet: NetworkCapabilities.TRANSPORT_WIFI")
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        Timber.d("Internet: NetworkCapabilities.TRANSPORT_ETHERNET")
                        return true
                    }
                }
            }
        }
        return false
    }
}