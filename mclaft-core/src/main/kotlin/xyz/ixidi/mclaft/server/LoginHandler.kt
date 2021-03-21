package xyz.ixidi.mclaft.server

import xyz.ixidi.mclaft.connection.Compression
import xyz.ixidi.mclaft.connection.Connection
import xyz.ixidi.mclaft.packet.ConnectionState
import xyz.ixidi.mclaft.packet.library.client.login.ClientLoginStartPacket
import xyz.ixidi.mclaft.packet.library.server.login.ServerLoginSuccessPacket
import xyz.ixidi.mclaft.packet.library.server.login.ServerSetCompressionPacket
import java.util.*

class LoginHandler(
        private val server: Server
) {

    suspend fun handle(connection: Connection) {
        connection.state = ConnectionState.LOGIN
        val loginStartPacket = connection.readPacket() as? ClientLoginStartPacket ?: throw Exception("Login start packet was expected.")

        val name = loginStartPacket.name
        //if (server.players[name] != null) throw Exception("Player already $name online.")
        val t = 10 //TODO
        //connection.writePacket(ServerSetCompressionPacket().apply { threshold = t })
        //connection.compression = Compression.Enabled(t)


        connection.writePacket(ServerLoginSuccessPacket().also {
            it.name = name
            it.uuid = UUID.randomUUID() //TODO
        })
        print("end")
    }

}