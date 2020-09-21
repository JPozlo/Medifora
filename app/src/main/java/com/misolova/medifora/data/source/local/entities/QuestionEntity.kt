package com.misolova.medifora.data.source.local.entities

import androidx.room.*
import java.sql.Date

@Fts4
@Entity(
    tableName = "question_table"
)
data class QuestionEntity @JvmOverloads constructor(
    var questionContent: String,
    var questionAuthorID: Int?,
    var questionCreatedAt: Date
) {
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "rowid") var questionID: Int? = null
    var questionAnswerIDs: List<Int>? = null
    var totalNumberOfAnswers: Int? = null
}