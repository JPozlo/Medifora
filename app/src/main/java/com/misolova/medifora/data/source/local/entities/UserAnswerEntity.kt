package com.misolova.medifora.data.source.local.entities

import androidx.room.Embedded
import androidx.room.Relation

data class UserAnswerEntity(
    @Embedded val user: UserEntity,
    @Relation(
        parentColumn = "userID",
        entityColumn = "answerAuthorID"
    )
    val answers: List<AnswerEntity>

) {
}