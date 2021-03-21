package xyz.ixidi.mclaft.packet.channel

import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.writeFully
import java.nio.charset.StandardCharsets
import java.util.*

suspend fun ByteWriteChannel.writeString(string: String) {
    val array = string.toByteArray(StandardCharsets.UTF_8)
    writeVarInt(array.size)
    writeFully(array)
}

suspend fun ByteWriteChannel.writeVarInt(int: Int) {
    var value = int
    do {
        var temp = (value and 127)
        value = value ushr 7
        if (value != 0) {
            temp = temp or 128
        }
        writeByte(temp.toByte())
    } while (value != 0)
}

suspend fun ByteWriteChannel.writeVarLong(long: Long) {
    var value = long
    do {
        var temp = (value and 127)
        value = value ushr 7
        if (value != 0L) {
            temp = temp or 128
        }
        writeByte(temp.toByte())
    } while (value != 0L)
}

suspend fun ByteWriteChannel.writeUUID(uuid: UUID) {
    writeLong(uuid.mostSignificantBits)
    writeLong(uuid.leastSignificantBits)
}