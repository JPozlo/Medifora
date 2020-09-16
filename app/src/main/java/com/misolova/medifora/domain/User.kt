package com.misolova.medifora.domain

data class User(
    val ID: String,
    val name: String,
    val email: String,
    val password: String,
    val accountCreatedAt: String
) {
}