package xyz.ixidi.mclaft.packet.library.client.login

import xyz.ixidi.mclaft.packet.ConnectionState
import xyz.ixidi.mclaft.packet.PacketSource
import xyz.ixidi.mclaft.packet.modeled.ModeledPacket

class ClientLoginStartPacket : ModeledPacket(0x00, ConnectionState.LOGIN, PacketSource.CLIENT) {

    var name by string()

}