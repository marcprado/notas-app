package com.notasapp.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {

    fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    fun formatDateTime(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    fun getRelativeTime(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - timestamp

        return when {
            diff < 60_000 -> "Hace un momento"
            diff < 3_600_000 -> "Hace ${diff / 60_000} min"
            diff < 86_400_000 -> "Hace ${diff / 3_600_000} h"
            diff < 604_800_000 -> "Hace ${diff / 86_400_000} dias"
            else -> formatDate(timestamp)
        }
    }
}
