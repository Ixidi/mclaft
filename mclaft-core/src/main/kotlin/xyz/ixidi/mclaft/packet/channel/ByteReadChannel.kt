package xyz.ixidi.mclaft.packet.channel

import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.readFully
import java.nio.charset.StandardCharsets
import java.util.*

suspend fun ByteReadChannel.readString(maxSize: Int): String {
    val length = readVarInt()
    if (length != 0 && length > maxSize) {
        throw Exception("String is too long.")
    }

    val array = ByteArray(length)
    readFully(array)
    return String(array, StandardCharsets.UTF_8)
}

suspend fun ByteReadChannel.readString(): String {
    val length = readVarInt()
    val array = ByteArray(length)
    readFully(array)
    return String(array, StandardCharsets.UTF_8)
}

suspend fun ByteReadChannel.readVarInt(): Int {
    var numRead = 0
    var result = 0
    var read: Int
    do {
        read = readByte().toInt()
        val value = read and 127
        result = result or (value shl 7 * numRead)

        numRead++
        if (numRead > 5) {
            throw RuntimeException("VarInt is too big")
        }
    } while (read and 128 != 0)

    return result
}

suspend fun ByteReadChannel.readVarLong(): Long {
    var numRead = 0
    var result: Long = 0
    var read: Int
    do {
        read = readByte().toInt()
        val value = read and 127
        result = result or (value shl 7 * numRead).toLong()

        numRead++
        if (numRead > 10) {
            throw RuntimeException("VarLong is too big")
        }
    } while (read and 128 != 0)

    return result
}

suspend fun ByteReadChannel.readUUID(): UUID = UUID(readLong(), readLong())