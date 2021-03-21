package xyz.ixidi.mclaft.packet

import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.ByteWriteChannel

interface Packet {

    val id: Int
    val state: ConnectionState
    val source: PacketSource

    suspend fun ByteReadChannel.read()
    suspend fun ByteWriteChannel.write()

}