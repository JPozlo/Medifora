package com.misolova.medifora.domain

data class Question(
    val ID: Int,
    val content: String,
    val answerIDs: ArrayList<Int>,
    val totalAnswersNumber: Int,
    val author: String,
    val createdAt: String
) {
}