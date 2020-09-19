package com.misolova.medifora.data.source.local.entities

import androidx.room.*
import java.sql.Date

@Fts4
@Entity(
    tableName = "question_table",
    indices = [Index("totalNumberOfAnswers"), Index("createdAt")],
    foreignKeys = [
        ForeignKey(entity = UserEntity::class, parentColumns = ["id"], childColumns = ["authorID"])
    ]
)
data class QuestionEntity(
    val content: String,
    val answerIDs: List<Int>,
    val answers: List<AnswerEntity>,
    val totalNumberOfAnswers: Int,
    val authorID: Int,
    @Embedded
    val author: UserEntity,
    val createdAt: Date
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "rowid")
    val id: Int? = null
}