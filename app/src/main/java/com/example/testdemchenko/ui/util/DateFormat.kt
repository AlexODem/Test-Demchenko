package com.example.testdemchenko.ui.util

import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

class DateFormat {
    companion object {
        private const val FORMAT_FULL = "d MMM yyyy"
        private const val FORMAT_SHORT = "d MMM"

        fun printFullDate(millis: Long?): String {
            return if (millis == null) {
                "NaN"
            } else {
                val dateTime = DateTime(millis * 1000, DateTimeZone.UTC)
                val format: DateTimeFormatter = DateTimeFormat.forPattern(FORMAT_FULL)
                dateTime.toString(format)
            }
        }

        fun printShortDate(millis: Long?) :String {
            return if (millis == null) {
                "NaN"
            } else {
                val dateTime = DateTime(millis * 1000)
                val format: DateTimeFormatter = DateTimeFormat.forPattern(FORMAT_SHORT)
                dateTime.toString(format)
            }
        }
    }
}