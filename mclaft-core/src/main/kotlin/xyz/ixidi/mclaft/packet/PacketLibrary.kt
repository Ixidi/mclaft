package xyz.ixidi.mclaft.packet

import xyz.ixidi.mclaft.packet.library.client.handshake.ClientHandshakePacket
import xyz.ixidi.mclaft.packet.library.client.login.ClientLoginStartPacket
import xyz.ixidi.mclaft.packet.library.server.login.ServerLoginSuccessPacket
import xyz.ixidi.mclaft.packet.library.server.login.ServerSetCompressionPacket

object PacketLibrary {

    private class PacketInfo(
        val id: Int,
        val state: ConnectionState,
        val source: PacketSource,
        val create: () -> Packet
    )

    private val packets = ArrayList<PacketInfo>()

    init {
        p { ClientHandshakePacket() }
        p { ClientLoginStartPacket() }

        p { ServerSetCompressionPacket() }
        p { ServerLoginSuccessPacket() }
    }

    fun matchPacket(id: Int, state: ConnectionState, source: PacketSource): Packet =
        packets.firstOrNull { it.id == id && it.state == state && it.source == source }?.create?.invoke() ?: UnknownPacket(id, state, source)

    private fun p(create: () -> Packet) {
        create().run {
            packets.add(PacketInfo(id, state, source, create))
        }
    }

}