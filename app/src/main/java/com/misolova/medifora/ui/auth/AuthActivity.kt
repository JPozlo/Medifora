package com.misolova.medifora.ui.auth

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.misolova.medifora.R
import com.misolova.medifora.ui.auth.viewmodel.AuthViewModel
import com.misolova.medifora.ui.home.viewmodel.MainViewModel
import com.misolova.medifora.util.BitmapConversion
import com.misolova.medifora.util.Constants.CAMERA_REQUEST_CODE
import com.misolova.medifora.util.Constants.GALLERY_REQUEST_CODE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import pub.devrel.easypermissions.AppSettingsDialog
import timber.log.Timber
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
@AndroidEntryPoint
@ExperimentalTime
class AuthActivity : AppCompatActivity() {

    companion object {
        const val TAG = "AUTH_ACTIVITY"
    }

    private val authViewModel: AuthViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        val navController = findNavController(R.id.my_auth_nav_host_fragment)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(Intent.ACTION_MAIN)
        intent.apply {
            addCategory(Intent.CATEGORY_HOME)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                var bmp = data?.extras?.get("data") as Bitmap
                val stringImn = BitmapConversion().encodeImage(bmp)
                authViewModel.setPhotoUrl(stringImn)
                mainViewModel.setPhotoUrl(stringImn)
                Timber.d("$TAG: Value of photo string $stringImn")
            }
            GALLERY_REQUEST_CODE -> {
                var bmp = data?.extras?.get("data") as Bitmap
                authViewModel.setPhotoUrl(BitmapConversion().encodeImage(bmp))
                mainViewModel.setPhotoUrl(BitmapConversion().encodeImage(bmp))
            }
            AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE -> {
                val yes = getString(R.string.yes)
                val no = getString(R.string.no)
                Toast.makeText(this, "Returned with camera settings", Toast.LENGTH_SHORT).show()
            }
        }
    }

}