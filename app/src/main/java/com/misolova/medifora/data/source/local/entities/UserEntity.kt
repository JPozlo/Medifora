package com.misolova.medifora.data.source.local.entities

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.sql.Date

@Entity(
    tableName = "user_table",
    indices = [Index("email", unique = true)]
)
data class UserEntity @JvmOverloads constructor(
    var name: String,
    var email: String,
    var password: String,
    var accountCreatedAt: Date
){
    @PrimaryKey(autoGenerate = true) var userID: Int? = null
    var photo: Bitmap? = null
    var userQuestions: List<QuestionEntity>? = null
    var userAnswers: List<AnswerEntity>? = null
}