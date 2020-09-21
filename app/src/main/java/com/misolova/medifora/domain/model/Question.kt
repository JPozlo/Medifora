package com.misolova.medifora.domain.model


data class Question(
    val content: String,
    val answerIDs: List<Int>,
    val answers: List<Answer>,
    val totalNumberOfAnswers: Int,
    val authorID: Int,
    val createdAt: Long
) {
    val ID: Int? = null
}