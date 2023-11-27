package com.example.myapplication.api

import androidx.room.TypeConverters
import com.example.myapplication.database.entities.model.LocalDateTimeConverter
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.threeten.bp.LocalDateTime
import org.threeten.bp.DateTimeUtils.toLocalDateTime
import org.threeten.bp.format.DateTimeFormatter

@Serializer(forClass = LocalDateTime::class)
object LocalDateTimeSerializer : KSerializer<LocalDateTime> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
        encoder.encodeString(dateFormatter.format(value).toString())
    }

    override fun deserialize(decoder: Decoder): LocalDateTime {
        val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
        return LocalDateTime.parse(decoder.decodeString(), dateFormatter)
    }
}