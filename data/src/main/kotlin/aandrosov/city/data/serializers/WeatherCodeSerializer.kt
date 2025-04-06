package aandrosov.city.data.serializers

import aandrosov.city.data.models.WeatherCodeModel
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class WeatherCodeSerializer : KSerializer<WeatherCodeModel> {
    override val descriptor = PrimitiveSerialDescriptor("aandrosov.city.data.models.WeatherCodeModel", PrimitiveKind.BYTE)

    override fun serialize(encoder: Encoder, value: WeatherCodeModel) {
        encoder.encodeByte(value.code)
    }

    override fun deserialize(decoder: Decoder): WeatherCodeModel {
        val byte = decoder.decodeByte()
        return WeatherCodeModel.entries.find { it.code == byte }!!
    }
}