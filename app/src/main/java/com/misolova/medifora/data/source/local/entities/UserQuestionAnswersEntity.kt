package com.misolova.medifora.data.source.local.entities

import androidx.room.Embedded
import androidx.room.Relation

data class UserQuestionAnswersEntity(
    @Embedded val user: UserEntity,
    @Relation(
        entity = QuestionEntity::class,
        parentColumn = "userID",
        entityColumn = "questionAuthorID"
    )
    val questions: List<QuestionAnswerEntity>
) {
}