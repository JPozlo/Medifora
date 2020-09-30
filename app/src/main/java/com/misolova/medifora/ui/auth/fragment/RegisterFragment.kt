package com.misolova.medifora.ui.auth.fragment

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.misolova.medifora.R
import com.misolova.medifora.data.source.remote.FirebaseProfileService
import com.misolova.medifora.domain.model.UserInfo
import com.misolova.medifora.ui.auth.viewmodel.AuthViewModel
import com.misolova.medifora.ui.home.MainActivity
import com.misolova.medifora.ui.home.viewmodel.MainViewModel
import com.misolova.medifora.util.Constants.KEY_USER_ID
import com.misolova.medifora.util.Constants.KEY_USER_STATUS
import com.misolova.medifora.util.Constants.USER_DATA_BUNDLE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber
import javax.inject.Inject
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
@ExperimentalTime
@AndroidEntryPoint
class RegisterFragment : Fragment() {
    companion object {
        @JvmStatic
        fun newInstance() =
            RegisterFragment().apply {
                arguments = Bundle().apply {
                    RegisterFragment()
                }
            }
        private const val TAG = "REGISTER_FRAGMENT"
    }

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private val authViewModel: AuthViewModel by activityViewModels()

    private val sharedViewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnRegisterUser = view.findViewById(R.id.btnRegister) as MaterialButton?
        val switchScreenButton = view.findViewById(R.id.btnSwitchToLogin) as MaterialButton

        val usernameEditText = view.findViewById(R.id.etUsername) as EditText?
        val emailEditText = view.findViewById(R.id.etEmail) as EditText?
        val passwordEditText = view.findViewById(R.id.etPassword) as EditText?
        val confirmPasswordEditText = view.findViewById(R.id.etConfirmPassword) as EditText?
        val checkboxAcceptTerms = view.findViewById(R.id.checkboxAcceptTerms) as CheckBox?

        btnRegisterUser?.setOnClickListener {
            val username = usernameEditText?.text.toString()
            val email = emailEditText?.text.toString()
            val password = passwordEditText?.text.toString()
            val confirmPassword = confirmPasswordEditText?.text.toString()

            val termsAndConditionsStatus = checkboxAcceptTerms?.isChecked
            if (!authViewModel.validateSignUpFields(email = email, password = password, username = username, confirmPassword = confirmPassword)) {
                Snackbar.make(view, "Please enter valid input", Snackbar.LENGTH_LONG).show()
                Timber.e("Invalid input")
            } else {
                if (termsAndConditionsStatus!!) {
                    register(username ,email, password)
                } else {
                    Snackbar.make(
                        view,
                        "Please accept the terms and conditions",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }

        switchScreenButton.setOnClickListener {
            goToLogin()
        }

    }


    private fun register(username: String, email: String, password: String) {
        authViewModel.signUpFunction(email, password)
            .addOnSuccessListener {
                val fireUser = it.user
                val user = UserInfo(
                    userId = fireUser?.uid!!,
                    name = username,
                    email = email,
                    password = password,
                    accountCreatedAt = System.currentTimeMillis(),
                    photo = null
                )
                Timber.d("$TAG: The user id is -> ${fireUser.uid}")
                FirebaseProfileService.createUser(user)
                sharedPreferences.edit().putString(KEY_USER_ID, fireUser.uid)
                sharedPreferences.edit().putBoolean(KEY_USER_STATUS, true).apply()
                val intent = Intent(requireActivity(), MainActivity::class.java)
                intent.apply {
                    this.putExtra(USER_DATA_BUNDLE, fireUser)
                }
                startActivity(intent)
            }
            .addOnFailureListener {
                Timber.e("${TAG}: Error due to -> ${it.message}")
            }
    }


    private fun goToLogin() {
        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
    }
}