package xyz.ixidi.mclaft.server

import io.ktor.network.selector.ActorSelectorManager
import io.ktor.network.sockets.Socket
import io.ktor.network.sockets.aSocket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import xyz.ixidi.mclaft.connection.Connection
import xyz.ixidi.mclaft.packet.PacketSource
import xyz.ixidi.mclaft.packet.library.client.handshake.ClientHandshakePacket
import xyz.ixidi.mclaft.server.player.ServerPlayerRepository

class Server {

    private val loginHandler = LoginHandler(this)

    lateinit var players: ServerPlayerRepository
    private set

    internal suspend fun handleNewConnection(socket: Socket) {
        val connection = Connection(socket, PacketSource.CLIENT)

        val handshake = connection.readPacket() as? ClientHandshakePacket
                ?: throw Exception("Handshake packet was expected.")
        when (handshake.nextState) {
            ClientHandshakePacket.NextState.STATUS -> {
                //TODO handle status
                connection.disconnect()
            }
            ClientHandshakePacket.NextState.LOGIN -> {
                loginHandler.handle(connection)
                delay(10000)
            }
            ClientHandshakePacket.NextState.UNKNOWN -> TODO()
        }
    }

}

suspend fun runMinecraftServer(port: Int, application: suspend Server.() -> Unit = {}) {
    val server = Server()
    application(server)

    val socketServer = aSocket(ActorSelectorManager(Dispatchers.IO)).tcp().bind(port = port)
    while (true) {
        val newSocket = socketServer.accept()
        server.handleNewConnection(newSocket)
    }
}

fun main() {
    runBlocking {
        runMinecraftServer(25565)
    }
}