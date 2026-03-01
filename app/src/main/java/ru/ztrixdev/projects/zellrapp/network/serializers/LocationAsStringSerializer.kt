package ru.ztrixdev.projects.zellrapp.network.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ru.ztrixdev.projects.zellrapp.network.types.Location

object LocationAsStringSerializer : KSerializer<Location> {
    private val InvalidLocationStringException: Exception = Exception("The provided location string does not match the POINT(LONGITUDE LATITUDE) template, therefore is invalid.")
    private val InvalidCoordinatesException: Exception = Exception("The provided coordinates violate restrictions, therefore are invalid.")


    override val descriptor: SerialDescriptor
        = PrimitiveSerialDescriptor("ru.ztrixdev.projects.zellrapp.supabase.types.Location",
        PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Location {
        val string = decoder.decodeString()
        val lonLatRaw = string.substringAfter("POINT(").substringBeforeLast(')').split(' ')
        if (lonLatRaw.size != 2)
            throw InvalidLocationStringException

        val lonLat = Pair(lonLatRaw[0].toDoubleOrNull(), lonLatRaw[1].toDoubleOrNull())
        if (lonLat.first == null || lonLat.second == null)
            throw InvalidLocationStringException

        if (lonLat.second!! !in  -90.0..90.0 || lonLat.first!! !in -180.0..180.0)
            throw InvalidCoordinatesException

        return Location(latitude = lonLat.second!!, longitude = lonLat.first!!)
    }

    override fun serialize(encoder: Encoder, value: Location) {
        encoder.encodeString("POINT(${value.longitude} ${value.latitude})")
    }
}