package com.example.util

import kotlinx.serialization.modules.SerializersModule
import java.math.BigDecimal
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

// https://github.com/Kotlin/kotlinx.serialization/blob/master/docs/serializers.md#contextual-serialization
val KtorSerializersModule = SerializersModule {
    contextual(OffsetDateTime::class, OffsetDateTimeSerializer)
    contextual(LocalDate::class, LocalDateSerializer)
    contextual(UUID::class, UUIDSerializer)
    contextual(BigDecimal::class, BigDecimalSerializer)
    contextual(Currency::class, CurrencySerializer)
    contextual(ZoneId::class, ZoneIdSerializer)
}

private object UUIDSerializer : SinglePropertySerializer<UUID>(
    UUID::class.qualifiedName,
    stringFrom = { it.toString() },
    instanceFrom = { UUID.fromString(it) }
)

private object OffsetDateTimeSerializer : SinglePropertySerializer<OffsetDateTime>(
    OffsetDateTime::class.qualifiedName,
    stringFrom = { it.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME) },
    instanceFrom = { OffsetDateTime.parse(it, DateTimeFormatter.ISO_OFFSET_DATE_TIME) }
)

private object LocalDateSerializer : SinglePropertySerializer<LocalDate>(
    LocalDate::class.qualifiedName,
    stringFrom = { it.toString() },
    instanceFrom = { LocalDate.parse(it) }
)

private object BigDecimalSerializer : SinglePropertySerializer<BigDecimal>(
    BigDecimal::class.qualifiedName,
    stringFrom = { it.toPlainString() },
    instanceFrom = { BigDecimal(it) }
)

private object ZoneIdSerializer : SinglePropertySerializer<ZoneId>(
    ZoneId::class.qualifiedName,
    stringFrom = { it.toString() },
    instanceFrom = { ZoneId.of(it) }
)

private object CurrencySerializer : SinglePropertySerializer<Currency>(
    Currency::class.qualifiedName,
    stringFrom = { it.currencyCode },
    instanceFrom = { Currency.getInstance(it) }
)
