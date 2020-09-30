package com.misolova.medifora.util

interface AuthListener {
    fun onStarted()
    fun onSuccess()
    fun onFailure(message: String)
}