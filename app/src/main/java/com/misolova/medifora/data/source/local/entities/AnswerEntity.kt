package com.misolova.medifora.data.source.local.entities

import androidx.room.*
import java.sql.Date

@Entity(
    tableName = "answer_table",
    indices = [Index("votes"), Index("createdAt")],
    foreignKeys = [
        ForeignKey(entity = QuestionEntity::class, parentColumns = ["id"], childColumns = ["questionID"]),
        ForeignKey(entity = UserEntity::class, parentColumns = ["id"], childColumns = ["authorID"])
    ]
)
data class AnswerEntity(
    val content: String,
    val questionID: Int,
    @Embedded
    val question: QuestionEntity,
    @Embedded
    val author: UserEntity,
    val authorID: Int,
    val votes: Int,
    val createdAt: Date
) {
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null
}