package com.misolova.medifora.data.source.local.entities

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.sql.Date

@Entity(tableName = "user_table",
    indices = [Index("email", unique = true), Index("accountCreatedAt")]
)
data class UserEntity(
    val name: String,
    val email: String,
    val photo: Bitmap?,
    val password: String,
    val questions: List<QuestionEntity>,
    val answers: List<AnswerEntity>,
    val accountCreatedAt: Date
){
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null
}