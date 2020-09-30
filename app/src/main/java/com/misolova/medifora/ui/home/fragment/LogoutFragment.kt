package com.misolova.medifora.ui.home.fragment

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.misolova.medifora.R
import com.misolova.medifora.ui.auth.AuthActivity
import com.misolova.medifora.ui.home.viewmodel.MainViewModel
import com.misolova.medifora.util.Constants.KEY_USER_STATUS
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.time.ExperimentalTime

@ExperimentalTime
@AndroidEntryPoint
class LogoutFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance() =
            LogoutFragment().apply {
                LogoutFragment()
            }

        private const val TAG = "LOGOUT FRAGMENT"
    }

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_logout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val confirmLogoutButton = view.findViewById(R.id.btnConfirmLogout) as MaterialButton?
        val cancelLogoutButton = view.findViewById(R.id.btnCancelLogout) as MaterialButton?

        confirmLogoutButton?.setOnClickListener {
            sharedPreferences.edit().putBoolean(KEY_USER_STATUS, false).apply()
            viewModel.logout
            val intent = Intent(requireActivity(), AuthActivity::class.java)
            startActivity(intent)
        }

        cancelLogoutButton?.setOnClickListener {
            findNavController().navigate(R.id.action_logoutFragment_to_homeFragment)
        }
    }

}
