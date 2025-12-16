package com.notasapp

import com.notasapp.utils.DateUtils
import org.junit.Assert.*
import org.junit.Test

class DateUtilsTest {

    @Test
    fun formatDateRetornaFormatoCorrecto() {
        val timestamp = 1609459200000L
        val result = DateUtils.formatDate(timestamp)
        assertTrue(result.matches(Regex("\\d{2}/\\d{2}/\\d{4}")))
    }

    @Test
    fun formatDateTimeRetornaFormatoCorrecto() {
        val timestamp = 1609459200000L
        val result = DateUtils.formatDateTime(timestamp)
        assertTrue(result.matches(Regex("\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}")))
    }

    @Test
    fun relativeTimeRecienteHaceUnMomento() {
        val now = System.currentTimeMillis()
        val result = DateUtils.getRelativeTime(now - 30_000)
        assertEquals("Hace un momento", result)
    }

    @Test
    fun relativeTimeCincoMinutos() {
        val now = System.currentTimeMillis()
        val result = DateUtils.getRelativeTime(now - 300_000)
        assertTrue(result.startsWith("Hace") && result.contains("min"))
    }

    @Test
    fun relativeTimeDosHoras() {
        val now = System.currentTimeMillis()
        val result = DateUtils.getRelativeTime(now - 7_200_000)
        assertTrue(result.contains("h"))
    }
}
