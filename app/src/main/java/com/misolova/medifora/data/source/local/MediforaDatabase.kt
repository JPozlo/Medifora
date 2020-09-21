package com.misolova.medifora.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.misolova.medifora.data.source.local.entities.*
import com.misolova.medifora.data.util.TypeConverter

@Database( entities = [AnswerEntity::class, QuestionEntity::class, UserEntity::class], version = 1)
@TypeConverters(TypeConverter::class)
abstract class MediforaDatabase: RoomDatabase(){

    abstract fun getMediforaRoomDao(): MediforaDao
}