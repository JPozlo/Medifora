package com.misolova.medifora.util

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class DateConversion {
    fun convertDate(timestamp: Timestamp): String{
        val dateInTimestamp =  timestamp
        val millis = dateInTimestamp.seconds * 1000 + dateInTimestamp.nanoseconds / 1000000
        val dateFormat = SimpleDateFormat("HH:mm, MM/dd/yyyy")
        val myDate = Date(millis)
        return dateFormat.format(myDate).toString()
    }
}