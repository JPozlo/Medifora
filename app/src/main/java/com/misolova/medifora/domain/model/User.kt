package com.misolova.medifora.domain.model

import android.graphics.Bitmap
import com.misolova.medifora.domain.model.Answer
import com.misolova.medifora.domain.model.Question

data class User(
    val name: String,
    val email: String,
    val photo: Bitmap?,
    val password: String,
    val accountCreatedAt: Long,
    val questions: List<Question>,
    val answers: List<Answer>
) {
}