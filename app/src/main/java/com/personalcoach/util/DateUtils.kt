package com.personalcoach.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    const val DAY_MS = 24 * 60 * 60 * 1000L

    fun startOfDay(timestampMs: Long): Long {
        val cal = Calendar.getInstance().apply {
            timeInMillis = timestampMs
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return cal.timeInMillis
    }

    fun endOfDay(timestampMs: Long): Long = startOfDay(timestampMs) + DAY_MS - 1

    fun startOfWeek(timestampMs: Long): Long {
        val cal = Calendar.getInstance().apply {
            timeInMillis = timestampMs
            set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return cal.timeInMillis
    }

    fun formatDate(timestampMs: Long, pattern: String = "MMM d, yyyy"): String {
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        return sdf.format(Date(timestampMs))
    }

    fun formatTime(timestampMs: Long): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(Date(timestampMs))
    }

    fun formatDateTime(timestampMs: Long): String {
        val sdf = SimpleDateFormat("MMM d, HH:mm", Locale.getDefault())
        return sdf.format(Date(timestampMs))
    }

    fun todayStartMs(): Long = startOfDay(System.currentTimeMillis())
}
