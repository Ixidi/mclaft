package xyz.ixidi.mclaft.connection

import io.ktor.network.sockets.Socket
import io.ktor.network.sockets.openReadChannel
import io.ktor.network.sockets.openWriteChannel
import io.ktor.util.deflated
import io.ktor.util.toByteArray
import io.ktor.utils.io.ByteChannel
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.copyTo
import io.ktor.utils.io.writeFully
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

        val packetChannel = ByteChannel(true)
        readChannel.copyTo(packetChannel, packetLength.toLong())

        val uncompressedData = when (compression) {
            is Compression.Disabled -> packetChannel.toByteArray()
            is Compression.Enabled -> {
                val dataLength = packetChannel.readVarInt() //TODO check
                if (dataLength == 0) {
                    packetChannel.toByteArray()
                } else {
                    val deflated = (packetChannel as ByteReadChannel).deflated()
                    val b = deflated.toByteArray()

                    if (b.size < dataLength) throw Exception("Packet real size is not equal dataLength.")
                    if (dataLength < (compression as Compression.Enabled).threshold) throw Exception("dataLength below server threshold.")

                    b
                }
            }
        }

        val dataChannel = ByteChannel()
        dataChannel.writeFully(uncompressedData)
        val id = dataChannel.readVarInt()
        val packet = PacketLibrary.matchPacket(id, state, incomingPackets)
        packet.run { dataChannel.read() }

        return packet
    }

    suspend fun writePacket(packet: Packet) {
        val dataChannel = ByteChannel(true)
        dataChannel.writeVarInt(packet.id)
        packet.run { dataChannel.write() }

        val dataBytes = dataChannel.toByteArray()
        when (compression) {
            is Compression.Disabled -> {
                writeChannel.writeVarInt(dataBytes.size)
                writeChannel.writeFully(dataBytes)
                return
            }
            is Compression.Enabled -> {

            }
        }
    }

}