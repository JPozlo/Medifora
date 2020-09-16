package com.misolova.medifora.domain

data class Question(
    val ID: String,
    val content: String,
    val answer: String,
    val totalAnswersNumber: Int,
    val author: String,
    val createdAt: String
) {
}