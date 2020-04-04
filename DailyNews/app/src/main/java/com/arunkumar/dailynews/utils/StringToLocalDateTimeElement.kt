package com.arunkumar.dailynews.utils

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.DateTimeParseException
import java.lang.reflect.Type
import java.text.ParseException

class StringToLocalDateTimeElement : JsonDeserializer<LocalDateTime> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): LocalDateTime {
        json?.let {
            val dateTimeFormatters = arrayOf(
                //DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss+00:00")
            )

            dateTimeFormatters.forEach { format ->
                try {
                    return LocalDateTime.parse(it.asString, format)
                } catch (dtpe: DateTimeParseException) {
                }
            }
            return LocalDateTime.now()
        } ?: run {
            return LocalDateTime.now()
        }
    }
}