package xyz.ixidi.mclaft.packet

import io.ktor.util.toByteArray
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.writeFully

class UnknownPacket(
    override val id: Int,
    override val state: ConnectionState,
    override val source: PacketSource
) : Packet {

    class PacketDataNotReadException : Exception()

    lateinit var data: ByteArray
    private set

    override suspend fun ByteReadChannel.read() {
        data = toByteArray()
    }

    override suspend fun ByteWriteChannel.write() {
        if (!::data.isInitialized) throw PacketDataNotReadException()
        writeFully(data)
    }

}