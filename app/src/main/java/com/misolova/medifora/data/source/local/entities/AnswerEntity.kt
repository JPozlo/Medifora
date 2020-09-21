package com.misolova.medifora.data.source.local.entities

import androidx.room.*
import java.sql.Date

@Entity(
    tableName = "answer_table",
    indices = [Index("votes"), Index("answerCreatedAt")]
)
data class AnswerEntity @JvmOverloads constructor(
    var content: String,
    var answerQuestionID: Int?,
    var answerAuthorID: Int,
    var answerCreatedAt: Date
) {
    @PrimaryKey(autoGenerate = true) var answerID: Int? = null
    var votes: Int? = null
}