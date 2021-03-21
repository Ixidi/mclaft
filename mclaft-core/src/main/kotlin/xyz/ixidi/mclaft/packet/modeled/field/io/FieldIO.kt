package xyz.ixidi.mclaft.packet.modeled.field.io

import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.ByteWriteChannel

interface FieldIO<T> {

    suspend fun ByteReadChannel.readField(): T
    suspend fun ByteWriteChannel.writeField(value: T)

}