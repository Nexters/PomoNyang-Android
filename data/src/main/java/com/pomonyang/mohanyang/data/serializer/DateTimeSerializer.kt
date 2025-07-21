package com.pomonyang.mohanyang.data.serializer

import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object LocalDateTimeSerializer : KSerializer<LocalDateTime> {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        encoder.encodeString(value.format(formatter))
    }

    override fun deserialize(decoder: Decoder): LocalDateTime {
        val dateTimeString = decoder.decodeString()
        return try {
            // 먼저 ISO_LOCAL_DATE_TIME 형식으로 파싱 시도
            LocalDateTime.parse(dateTimeString, formatter)
        } catch (e: Exception) {
            try {
                // 타임존 정보가 포함된 경우 ZonedDateTime으로 파싱 후 LocalDateTime으로 변환
                ZonedDateTime.parse(dateTimeString).toLocalDateTime()
            } catch (e2: Exception) {
                // 마지막으로 ISO_INSTANT 형식 시도 (UTC 기준)
                LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_INSTANT)
            }
        }
    }
}
