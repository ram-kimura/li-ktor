package com.example.util

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json

val jsonSerializer = Json {
    serializersModule = KtorSerializersModule
    ignoreUnknownKeys = true
    encodeDefaults = true
    prettyPrint = true
}

open class SinglePropertySerializer<T>(
    serialName: String?,
    private val stringFrom: (T) -> String,
    private val instanceFrom: (String) -> T
) :
    KSerializer<T> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(serialName ?: "UnknownClass", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: T) {
        encoder.encodeString(stringFrom(value))
    }

    override fun deserialize(decoder: Decoder): T {
        return instanceFrom(decoder.decodeString())
    }
}
