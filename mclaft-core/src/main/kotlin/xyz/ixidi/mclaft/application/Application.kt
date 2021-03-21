package xyz.ixidi.mclaft.application

import io.ktor.network.selector.ActorSelectorManager
import io.ktor.network.sockets.Socket
import io.ktor.network.sockets.aSocket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import xyz.ixidi.mclaft.connection.Connection
import xyz.ixidi.mclaft.packet.PacketSource

class Application {

    internal suspend fun handleNewConnection(socket: Socket) {
        val connection = Connection(socket, PacketSource.CLIENT)
        println(connection.readPacket()::class.qualifiedName)
    }

}

suspend fun runMinecraftServer(port: Int, application: suspend Application.() -> Unit = {}) {
    val app = Application()
    application(app)

    val server = aSocket(ActorSelectorManager(Dispatchers.IO)).tcp().bind(port = port)
    while (true) {
        val newSocket = server.accept()
        GlobalScope.launch {
            app.handleNewConnection(newSocket)
        }
    }
}

fun main() {
    runBlocking {
        runMinecraftServer(25566)
    }
}