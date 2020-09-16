package com.misolova.medifora.domain

data class Answer(
    val ID: String,
    val content: String,
    val question: String,
    val author: String,
    val createdAt: String
) {
}