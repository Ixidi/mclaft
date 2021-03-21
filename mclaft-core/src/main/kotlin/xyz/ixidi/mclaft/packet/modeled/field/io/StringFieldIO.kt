package xyz.ixidi.mclaft.packet.modeled.field.io

import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.ByteWriteChannel
import xyz.ixidi.mclaft.packet.channel.readString
import xyz.ixidi.mclaft.packet.channel.writeString

object StringFieldIO : FieldIO<String> {

    override suspend fun ByteReadChannel.readField(): String = readString()

    override suspend fun ByteWriteChannel.writeField(value: String) {
        writeString(value)
    }


}