package com.misolova.medifora.domain.model

data class Answer(
    val content: String,
    val questionID: Int,
    val question: Question,
    val authorID: Int,
    val createdAt: Long,
    val votes: Int
) {
}