package com.timestampformatter

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

object TimestampFormatter {
    @RequiresApi(Build.VERSION_CODES.O)
    fun getDayNameFromTimestamp(timestamp: Long): String {
        val instant = Instant.ofEpochSecond(timestamp)
        val formatter = DateTimeFormatter.ofPattern("EEEE") // EEEE for full day name (e.g., Monday)
        return instant.atZone(ZoneId.systemDefault()).format(formatter)
    }

    fun convertTimestampToString(timestamp: Long): String {
        val date = Date(timestamp * 1000) // Convert seconds to milliseconds if needed
        val dateFormat = SimpleDateFormat("h a", Locale.getDefault())
        val formattedDate = dateFormat.format(date)

        return formattedDate
    }
}