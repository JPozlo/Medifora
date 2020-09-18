package com.misolova.medifora.domain

data class User(
    val ID: Int,
    val name: String,
    val email: String,
    val password: String,
    val accountCreatedAt: String,
    val questions: ArrayList<Question>,
    val answers: ArrayList<Answer>
) {
}