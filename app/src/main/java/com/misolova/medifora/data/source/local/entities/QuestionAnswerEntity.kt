package com.misolova.medifora.data.source.local.entities

import androidx.room.Embedded
import androidx.room.Relation

data class QuestionAnswerEntity(
    @Embedded val question: QuestionEntity,
    @Relation(
        parentColumn = "rowid",
        entityColumn = "answerQuestionID"
    )
    val answers: List<AnswerEntity>
) {
}