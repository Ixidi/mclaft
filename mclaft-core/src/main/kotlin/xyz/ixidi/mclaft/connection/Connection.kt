package xyz.ixidi.mclaft.connection

import io.ktor.network.sockets.Socket
import io.ktor.network.sockets.openReadChannel
import io.ktor.network.sockets.openWriteChannel
import io.ktor.util.deflated
import io.ktor.util.toByteArray
import io.ktor.utils.io.*
import xyz.ixidi.mclaft.packet.ConnectionState
import xyz.ixidi.mclaft.packet.Packet
import xyz.ixidi.mclaft.packet.PacketLibrary
import xyz.ixidi.mclaft.packet.PacketSource
import xyz.ixidi.mclaft.packet.channel.readVarInt
import xyz.ixidi.mclaft.packet.channel.writeVarInt

class Connection(
        private val socket: Socket,
        private val incomingPackets: PacketSource
) {

    var compression: Compression = Compression.Disabled
    var state: ConnectionState = ConnectionState.HANDSHAKE

    private val readChannel = socket.openReadChannel()
    private val writeChannel = socket.openWriteChannel(true)

    suspend fun readPacket(): Packet {

        val packetLength = readChannel.readVarInt()

        val packetDataChannel = ByteChannel(true)
        when (val c = compression) {
            is Compression.Disabled -> {
                readChannel.copyTo(packetDataChannel, packetLength.toLong())
            }
            is Compression.Enabled -> {
                val dataLength = readChannel.readVarInt()
                when {
                    dataLength == 0 -> readChannel.copyTo(packetDataChannel)
                    dataLength < c.threshold -> throw Exception("Packet is below server threshold.")
                    else -> {
                        val deflated = readChannel.deflated()
                        deflated.copyTo(packetDataChannel, dataLength.toLong())
                    }
                }
            }
        }
        val id = packetDataChannel.readVarInt()
        val packet = PacketLibrary.matchPacket(id, state, incomingPackets)
        packet.run { packetDataChannel.read() }
        return packet
    }

    suspend fun writePacket(packet: Packet) {
        val packetDataChannel = ByteChannel(true)
        packetDataChannel.writeVarInt(packet.id)
        packet.run { packetDataChannel.write() }
        packetDataChannel.close()

        val packetChannel = ByteChannel(true)
        when (val c = compression) {
            is Compression.Disabled -> {
                packetDataChannel.copyTo(packetChannel)
            }
            is Compression.Enabled -> {
                val b = packetDataChannel.toByteArray()
                if (b.size >= c.threshold) {
                    packetChannel.writeVarInt(b.size)
                    packetDataChannel.copyTo((packetChannel as ByteWriteChannel).deflated())
                } else {
                    packetChannel.writeVarInt(0)
                    packetDataChannel.copyTo(packetChannel)
                }
            }
        }

        packetChannel.close()
        val b = packetChannel.toByteArray()
        writeChannel.writeVarInt(b.size)
        writeChannel.writeFully(b)
    }

    suspend fun disconnect() {
        socket.close()
    }
}