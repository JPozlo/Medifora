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
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.misolova.medifora.R
import com.misolova.medifora.ui.auth.viewmodel.AuthViewModel
import com.misolova.medifora.ui.home.MainActivity
import com.misolova.medifora.ui.home.viewmodel.MainViewModel
import com.misolova.medifora.util.Constants.KEY_USER_ID
import com.misolova.medifora.util.Constants.KEY_USER_STATUS
import com.misolova.medifora.util.showSingleActionSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_register.*
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
                notifyUser("Please enter valid input!")
                Timber.e("Invalid input")
            } else {
                if (termsAndConditionsStatus!!) {
                    register(username ,email, password)
                } else {
                   notifyUser("Please accept the terms and conditions before creating an account!")
                }
            }
        }

        switchScreenButton.setOnClickListener {
            goToLogin()
        }

    }

    private fun register(username: String, email: String, password: String) {
        progressBarRegister?.visibility = View.VISIBLE
        authViewModel.signUpFunction(email = email, password= password)
            .addOnSuccessListener {
                val fireUser = it.user
                val fireUserId = fireUser?.uid!!
                Timber.d("$TAG: The user id is -> $fireUserId")

                authViewModel.saveUser(name = username, email = email, userID = fireUserId)
                    .addOnSuccessListener {
                        Timber.d("$TAG: Executed after user save")
                        Timber.d("$TAG: Username -> $username")
                        Timber.d("$TAG: UserId -> $fireUserId")
                        Timber.d("$TAG: email -> $email")

                        sharedPreferences.edit().putString(KEY_USER_ID, fireUser.uid).apply()
                        sharedPreferences.edit().putBoolean(KEY_USER_STATUS, true).apply()

                        sharedViewModel.fetchUserById(fireUserId)

                        notifyUser("Account created successfully")

                        val intent = Intent(requireActivity(), MainActivity::class.java)
                        startActivity(intent)
                        progressBarRegister?.visibility = View.GONE

                    }
                    .addOnFailureListener {e ->
                        notifyUser(e.localizedMessage)
                    }

            }
            .addOnFailureListener {e ->
                Timber.e("${TAG}: Error due to -> ${e.localizedMessage}")
                progressBarRegister?.visibility = View.GONE
                when (e) {
                    is FirebaseAuthUserCollisionException -> {
                        notifyUser("There is a problem with the email address you entered")
                    }
                    is FirebaseAuthWeakPasswordException -> {
                        notifyUser("Weak Password")
                    }
                    else -> {
                        notifyUser(e.localizedMessage!!)
                    }
                }

            }
    }

    private fun notifyUser(message : String){
        requireView().showSingleActionSnackbar(message)
    }

    private fun goToLogin() {
        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
    }
}