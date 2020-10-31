package com.misolova.medifora.ui.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.misolova.medifora.R
import com.misolova.medifora.ui.auth.AuthActivity
import com.misolova.medifora.ui.auth.viewmodel.AuthViewModel
import com.misolova.medifora.ui.home.viewmodel.MainViewModel
import com.misolova.medifora.util.Constants.KEY_USER_STATUS
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber
import javax.inject.Inject
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
@AndroidEntryPoint
@ExperimentalTime
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    BottomNavigationView.OnNavigationItemSelectedListener {

    companion object {
        private const val TAG = "MAIN_ACTIVITY"
    }

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private val authViewModel: AuthViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onStart() {
        super.onStart()

        val navController = findNavController(R.id.my_nav_host_fragment)
        val userState = sharedPreferences.getBoolean(KEY_USER_STATUS, false)

        Timber.d("$TAG: The user state before check is -> $userState")

        if (!userState) {
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
        }

        Timber.d("$TAG: The user state after check is -> $userState")

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_main)


        val navController = findNavController(R.id.my_nav_host_fragment)

//        imageView.setImageBitmap(BitmapConversion().decodeString(mainViewModel.getPhotoUrl()))

//        GlobalScope.launch {
//            checkConnection()
//        }

        appBarConfiguration = AppBarConfiguration.Builder(
            R.id.homeFragment, R.id.answerRequestFragment,
            R.id.profileFragment, R.id.auth_nav
        )
            .setOpenableLayout(drawerLayout)
            .build()
        setupActionBarWithNavController(navController, appBarConfiguration)

        navigation_view.setupWithNavController(navController)
        navigation_view.setNavigationItemSelectedListener(this)

        bottom_navigation_view.setupWithNavController(navController)
        bottom_navigation_view.setOnNavigationItemSelectedListener {
            when (it.itemId) {
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
        NavController.OnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment, R.id.profileFragment, R.id.answerRequestFragment -> {
                    bottom_navigation_view.visibility = View.VISIBLE
                }
                else -> bottom_navigation_view.visibility = View.GONE
            }
        }

    }

//    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
//        navDrawerHeaderName?.text = authViewModel.user?.email.toString()
//        return super.onCreateView(name, context, attrs)
//    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.my_nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
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
    private fun checkConnection() {
        val result = hasOnlineConnection(applicationContext)
        if (!result) {
            openOfflineFragment()
        } else return
    }

    private fun openOfflineFragment() {
        val navController = findNavController(R.id.my_nav_host_fragment)
        navController.navigate(R.id.offlineFragment)
    }

    private fun hasOnlineConnection(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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
        return false
    }
}