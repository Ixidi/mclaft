package xyz.ixidi.mclaft.packet.modeled.field.io

import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.ByteWriteChannel

object ShortFieldIO : FieldIO<Short> {

    override suspend fun ByteReadChannel.readField(): Short = readShort()

    override suspend fun ByteWriteChannel.writeField(value: Short) {
        writeShort(value)
    }


}