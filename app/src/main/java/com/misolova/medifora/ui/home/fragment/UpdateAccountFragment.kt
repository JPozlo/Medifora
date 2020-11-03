package com.misolova.medifora.ui.home.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.misolova.medifora.R
import com.misolova.medifora.ui.home.viewmodel.MainViewModel
import com.misolova.medifora.util.Constants.KEY_USER_ID
import com.misolova.medifora.util.isEmailValid
import com.misolova.medifora.util.showSingleActionSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber
import javax.inject.Inject
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
@ExperimentalTime
@AndroidEntryPoint
class UpdateAccountFragment : Fragment() {

    companion object {
        private const val TAG = "UPDATE ACCOUNT FRAGMENT"
    }

    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnUpdateName = view.findViewById(R.id.btnUpdateName) as MaterialButton?
        val btnUpdateEmail = view.findViewById(R.id.btnUpdateEmail) as MaterialButton?
        val btnUpdatePasswords = view.findViewById(R.id.btnUpdatePassword) as MaterialButton?

        val etUsername = view.findViewById(R.id.etUpdateName) as EditText?
        val etEmail = view.findViewById(R.id.etUpdateEmail) as EditText?

        val etNewPassword = view.findViewById(R.id.etNewPass) as EditText?
        val etConfirmNewPassword = view.findViewById(R.id.etConfirmNewPass) as EditText?

        btnUpdateName?.setOnClickListener {
            val name = etUsername?.text.toString()
            if (name.isBlank()) {
                notifyUser("Please enter a name!")
                return@setOnClickListener
            }
            val user = viewModel.user
            Timber.e("$TAG: User -> $user")
            user
                ?.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(name).build())
                ?.addOnSuccessListener {
                    val id = sharedPreferences.getString(KEY_USER_ID, "")!!
                    viewModel.updateName(name, id).addOnSuccessListener {
                        notifyUser("Name successfully updated")
                        findNavController().navigate(R.id.homeFragment)
                    }.addOnFailureListener { e ->
                        notifyUser(e.localizedMessage!!)
                    }
                }?.addOnFailureListener { e ->
                    notifyUser(e.localizedMessage!!)
                }
        }

        btnUpdateEmail?.setOnClickListener {
            val email = etEmail?.text.toString()
            if (!email.isEmailValid() || email.isBlank()) {
                notifyUser("Please enter a valid email")
                return@setOnClickListener
            }
            FirebaseAuth.getInstance().currentUser?.updateEmail(email)?.addOnSuccessListener {
                val id = sharedPreferences.getString(KEY_USER_ID, "")!!
                viewModel.updateEmail(email, id).addOnSuccessListener {
                    notifyUser("Email successfully updated")
                    findNavController().navigate(R.id.homeFragment)
                }.addOnFailureListener { e ->
                    notifyUser(e.localizedMessage!!)
                }

            }?.addOnFailureListener { e ->
                notifyUser(e.localizedMessage!!)
            }
        }

        btnUpdatePasswords?.setOnClickListener {
            val newPass = etNewPassword?.text.toString()
            val confirmPass = etConfirmNewPassword?.text.toString()
            if (newPass != confirmPass) {
                notifyUser("Passwords do not match!")
                return@setOnClickListener
            }
            if (newPass.isBlank() || confirmPass.isBlank()) {
                notifyUser("Please enter password values")
                return@setOnClickListener
            }
            FirebaseAuth.getInstance().currentUser?.updatePassword(newPass)?.addOnSuccessListener {
                notifyUser("Password successfully updated!")
                findNavController().navigate(R.id.homeFragment)
            }?.addOnFailureListener { e ->
                notifyUser(e.localizedMessage!!)
            }
        }
    }

    private fun notifyUser(message: String) = requireView().showSingleActionSnackbar(message)

}