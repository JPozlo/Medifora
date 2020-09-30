package com.misolova.medifora.ui.auth.fragment

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.misolova.medifora.R
import com.misolova.medifora.ui.auth.viewmodel.AuthViewModel
import com.misolova.medifora.ui.home.MainActivity
import com.misolova.medifora.util.Constants
import com.misolova.medifora.util.Constants.KEY_USER_ID
import com.misolova.medifora.util.Constants.USER_DATA_BUNDLE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber
import javax.inject.Inject
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
@AndroidEntryPoint
@ExperimentalTime
class LoginFragment : Fragment() {
    companion object {
        @JvmStatic
        fun newInstance() =
            LoginFragment().apply {
                LoginFragment()
            }
        private const val TAG = "LOGIN_FRAGMENT"
    }

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private val authViewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val emailEditText = view.findViewById(R.id.etEmailLogin) as EditText?
        val passwordEditText = view.findViewById(R.id.etPasswordLogin) as EditText?
        val loginUserButton = view.findViewById(R.id.btnLogin) as MaterialButton?
        val switchScreenButton = view.findViewById(R.id.btnSwitchToRegister) as MaterialButton?

        loginUserButton?.setOnClickListener {
            val email = emailEditText?.text.toString()
            val password = passwordEditText?.text.toString()
            if(!authViewModel.validateSignInFields(email = email, password = password)){
                Snackbar.make(view, "Please enter valid input", Snackbar.LENGTH_LONG).show()
                Timber.e("Invalid input")
            } else {
                login(email, password)
            }

        }

        switchScreenButton?.setOnClickListener {
            goToSignup()
        }

    }

    private fun login(email: String, password: String) {
        authViewModel.loginFunction(email, password).addOnSuccessListener {
            Timber.d("Successfully saved data -> ${it.user}")
            val user = it.user
            Timber.d("$TAG: The user id is -> ${user?.uid!!}")
            sharedPreferences.edit().putString(KEY_USER_ID, user.uid)
            sharedPreferences.edit().putBoolean(Constants.KEY_USER_STATUS, true).apply()
            val intent = Intent(requireActivity(), MainActivity::class.java)
            intent.apply {
                this.putExtra(USER_DATA_BUNDLE, user)
            }
            startActivity(intent)
        }.addOnFailureListener {
            Timber.e("$TAG: Error due to -> ${it.message}")
            }

    }

    private fun goToSignup() {
        findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
    }

}