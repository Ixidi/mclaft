package xyz.ixidi.mclaft.packet.library.server.login

import xyz.ixidi.mclaft.packet.ConnectionState
import xyz.ixidi.mclaft.packet.PacketSource
import xyz.ixidi.mclaft.packet.modeled.ModeledPacket

class ServerLoginSuccessPacket : ModeledPacket(0x02, ConnectionState.LOGIN, PacketSource.SERVER) {

    var uuid by uuid()
    var name by string()

}