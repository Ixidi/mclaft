package xyz.ixidi.mclaft.packet.modeled.field.io

import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.ByteWriteChannel
import xyz.ixidi.mclaft.packet.channel.readUUID
import xyz.ixidi.mclaft.packet.channel.writeUUID
import java.util.*

object UUIDFieldIO : FieldIO<UUID> {

    override suspend fun ByteReadChannel.readField(): UUID = readUUID()

    override suspend fun ByteWriteChannel.writeField(value: UUID) {
        writeUUID(value)
    }

}