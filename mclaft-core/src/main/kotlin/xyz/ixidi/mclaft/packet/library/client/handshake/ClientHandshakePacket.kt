package xyz.ixidi.mclaft.packet.library.client.handshake

import xyz.ixidi.mclaft.packet.ConnectionState
import xyz.ixidi.mclaft.packet.PacketSource
import xyz.ixidi.mclaft.packet.modeled.ModeledPacket

class ClientHandshakePacket : ModeledPacket(0x00, ConnectionState.HANDSHAKE, PacketSource.CLIENT) {

    enum class NextState {
        STATUS,
        LOGIN,
        UNKNOWN;
    }

    var protocolVersion by varInt()
    var serverAddress by string()
    var port by short()
    private var _nextState by varInt()

    var nextState: NextState
        get() = when(_nextState) {
            1 -> NextState.STATUS
            2 -> NextState.LOGIN
            else -> NextState.UNKNOWN
        }
        set(value) {
            _nextState = when (value) {
                NextState.STATUS -> 1
                NextState.LOGIN -> 2
                NextState.UNKNOWN -> 0
            }
        }

}