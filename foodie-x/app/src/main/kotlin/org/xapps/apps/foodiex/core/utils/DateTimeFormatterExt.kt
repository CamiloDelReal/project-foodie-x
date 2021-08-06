package org.xapps.apps.foodiex.core.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter


const val DATE_TIME_PATTERN_DB = "yyyyMMddhhmmssSSSSSS"
const val DATE_PATTERN_DB = "yyyyMMdd"
const val TIME_PATTERN_DB = "hhmmssSSSSSS"


fun LocalDateTime.parseToString(format: String? = null): String {
    val pattern = format ?: DATE_TIME_PATTERN_DB
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return format(formatter)
}

fun LocalDate.parseToString(format: String? = null): String {
    val pattern = format ?: DATE_PATTERN_DB
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return format(formatter)
}

fun LocalTime.parseToString(format: String? = null): String {
    val pattern = format ?: TIME_PATTERN_DB
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return format(formatter)
}