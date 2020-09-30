package com.misolova.medifora.data.source.remote

import com.google.firebase.auth.FirebaseAuth

class FirebaseSource {

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    fun login(email: String, password: String) = firebaseAuth.signInWithEmailAndPassword(email, password)

    fun register(email: String, password: String) = firebaseAuth.createUserWithEmailAndPassword(email, password)

    fun logout() = firebaseAuth.signOut()

    fun currentUser() = firebaseAuth.currentUser

}