package xyz.ixidi.mclaft.packet.modeled.field.io

import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.ByteWriteChannel
import xyz.ixidi.mclaft.packet.channel.readVarInt
import xyz.ixidi.mclaft.packet.channel.writeVarInt

object VarIntFieldIO : FieldIO<Int> {

    override suspend fun ByteReadChannel.readField(): Int = readVarInt()

    override suspend fun ByteWriteChannel.writeField(value: Int) {
        writeVarInt(value)
    }


}