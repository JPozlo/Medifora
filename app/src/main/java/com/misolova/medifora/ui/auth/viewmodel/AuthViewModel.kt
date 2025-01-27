package com.misolova.medifora.ui.auth.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.misolova.medifora.data.repo.MediforaRepository
import com.misolova.medifora.util.isEmailValid
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
@ExperimentalTime
class AuthViewModel @ViewModelInject constructor(
    private val mediforaRepository: MediforaRepository
) : ViewModel() {

    companion object {
        private const val TAG = "AUTH_VIEW_MODEL"
    }

    private val _userIsAuthenticated = MutableLiveData<Boolean>()
    val userIsAuthenticated: LiveData<Boolean> = _userIsAuthenticated

    val user by lazy{
        FirebaseAuth.getInstance().currentUser
    }

    fun validateSignUpFields(
        email: String,
        password: String,
        username: String,
        confirmPassword: String
    ): Boolean {
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty()) return false
        if (!email.isEmailValid()) return false
        if (password != confirmPassword) return false
        if (password.count { it.isDigit() } < 3) return false
        return true
    }

    fun validateSignInFields(
        email: String,
        password: String
    ): Boolean {
        if (email.isEmpty() || password.isEmpty()) return false
        if (!email.isEmailValid()) return false
        if (password.count { it.isDigit() } < 3) return false
        return true
    }

    fun loginFunction(email: String, password: String): Task<AuthResult> {
        return login(email, password)
    }

    fun signUpFunction( email: String, password: String): Task<AuthResult> {
       return signUp(email, password)
    }


    private fun login(email: String, password: String): Task<AuthResult> {
        return mediforaRepository.login(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Timber.d("$TAG: User successfully signed in -> ${task.result?.user}")
                } else {
                    Timber.e("$TAG: Exception signing in user -> ${task.exception?.message}")
                }
            }
    }

    fun saveUser(name: String, email: String, userID: String) =
        mediforaRepository.createUser(userID, name, email)


//    fun saveUser(name: String, email: String, userID: String) = viewModelScope.launch {
//        FirebaseProfileService.createUser(name, email, userID)
//            .onCompletion { cause ->
//                Timber.d("${TAG}: Cause of completing user creation -> $cause")
//            }
//            .catch { cause ->
//                Timber.e("${TAG}: Caught the exception -> $cause")
//            }
//            .collect { value ->
//                Timber.d("${TAG}: The collected value is -> ${value.get().result}")
//            }
//    }

    private fun signUp( email: String, password: String): Task<AuthResult> {
        return mediforaRepository.register(email, password)
    }

}