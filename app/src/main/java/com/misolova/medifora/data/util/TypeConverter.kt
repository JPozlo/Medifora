package com.misolova.medifora.data.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.misolova.medifora.data.source.local.entities.AnswerEntity
import com.misolova.medifora.data.source.local.entities.QuestionEntity
import java.io.ByteArrayOutputStream
import java.sql.Date

class TypeConverter {

    // Converter functions for bitmap transformation into DB-Compatible-Format and vice versa

    @TypeConverter
    fun fromBitmap(bitmap: Bitmap): ByteArray{
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }

    @TypeConverter
    fun toBitmap(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    // Converter functions for Date transformation into Long Format and vice versa

    @TypeConverter
    fun fromDate(date: Date): Long{
        return date.time
    }

    @TypeConverter
    fun toDate(long: Long): Date{
        return Date(long)
    }

    @TypeConverter
    fun fromQuestionEntityList(list: List<QuestionEntity>): String{
        if( list == null) return  ""
        val gson: Gson = Gson()
        val type = object: TypeToken<List<QuestionEntity>>(){}.type
        return gson.toJson(list, type)
    }

    @TypeConverter
    fun toQuestionEntityList(objectString: String): List<QuestionEntity> {
        if(objectString == null) return listOf()
        val gson = Gson()
        val type = object: TypeToken<List<QuestionEntity>>(){}.type
        return gson.fromJson(objectString, type)
    }

    @TypeConverter
    fun fromAnswerEntityList(list: List<AnswerEntity>): String{
        if( list == null) return  ""
        val gson: Gson = Gson()
        val type = object: TypeToken<List<AnswerEntity>>(){}.type
        return gson.toJson(list, type)
    }

    @TypeConverter
    fun toAnswerEntityList(objectString: String): List<AnswerEntity> {
        if(objectString == null) return listOf()
        val gson = Gson()
        val type = object: TypeToken<List<AnswerEntity>>(){}.type
        return gson.fromJson(objectString, type)
    }

    @TypeConverter
    fun fromAnswerIDList(list: List<Int>): String{
        if( list == null) return  ""
        val gson: Gson = Gson()
        val type = object: TypeToken<List<Int>>(){}.type
        return gson.toJson(list, type)
    }

    @TypeConverter
    fun toAnswerIDList(objectString: String): List<Int> {
        if(objectString == null) return listOf()
        val gson = Gson()
        val type = object: TypeToken<List<Int>>(){}.type
        return gson.fromJson(objectString, type)
    }
}