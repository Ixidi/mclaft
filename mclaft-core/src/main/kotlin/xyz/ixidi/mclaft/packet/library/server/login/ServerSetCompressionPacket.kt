package xyz.ixidi.mclaft.packet.library.server.login

import xyz.ixidi.mclaft.packet.ConnectionState
import xyz.ixidi.mclaft.packet.PacketSource
import xyz.ixidi.mclaft.packet.modeled.ModeledPacket

class ServerSetCompressionPacket : ModeledPacket(0x03, ConnectionState.LOGIN, PacketSource.SERVER) {

    var threshold by varInt()

}