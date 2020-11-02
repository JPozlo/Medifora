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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.misolova.medifora.R
import com.misolova.medifora.ui.auth.viewmodel.AuthViewModel
import com.misolova.medifora.ui.home.MainActivity
import com.misolova.medifora.ui.home.viewmodel.MainViewModel
import com.misolova.medifora.util.Constants.KEY_USER_ID
import com.misolova.medifora.util.Constants.KEY_USER_STATUS
import com.misolova.medifora.util.showSingleActionSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_login.*
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
    private val sharedViewModel: MainViewModel by viewModels()

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
            if (!authViewModel.validateSignInFields(email = email, password = password)) {
                notifyUser("Please enter valid input")
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
        progressBarLogin?.visibility = View.VISIBLE
        authViewModel.loginFunction(email, password)
            .addOnSuccessListener {
                Timber.d("Successfully saved data -> ${it.user}")
                val user = it.user
                Timber.d("$TAG: The user id is -> ${user?.uid!!}")

                sharedPreferences.edit().putString(KEY_USER_ID, user.uid).apply()
                sharedPreferences.edit().putBoolean(KEY_USER_STATUS, true).apply()

                progressBarLogin?.visibility = View.GONE

                sharedViewModel.fetchUserById(user.uid)

                val intent = Intent(requireActivity(), MainActivity::class.java)
                startActivity(intent)
            }
            .addOnFailureListener { e ->
                Timber.e("$TAG: Error due to -> ${e.localizedMessage}")
                progressBarLogin?.visibility = View.GONE
                when (e) {
                    is FirebaseAuthInvalidCredentialsException -> {
                        notifyUser("Invalid Password")
                    }
                    is FirebaseAuthInvalidUserException -> {
                        when (e.errorCode) {
                            "ERROR_USER_NOT_FOUND" -> {
                                notifyUser("No account matches this email")
                            }
                            "ERROR_USER_DISABLED" -> {
                                notifyUser("The account has been disabled")
                            }
                            else -> notifyUser("Incorrect email address")
                        }
                    }
                    else -> notifyUser(e.localizedMessage!!)
                }

            }

    }

    private fun notifyUser(message: String) {
        requireView().showSingleActionSnackbar(message)
    }


    private fun goToSignup() {
        findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
    }

}