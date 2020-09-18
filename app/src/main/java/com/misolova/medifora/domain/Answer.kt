package com.misolova.medifora.domain

data class Answer(
    val ID: Int,
    val content: String,
    val questionContent: String,
    val questionID: Int,
    val author: String,
    val createdAt: String
) {
}