package xyz.ixidi.mclaft.server.player

interface ServerPlayerRepository {

    operator fun get(name: String, exactName: Boolean = false): ServerPlayer?

    fun getAll(): List<ServerPlayer>

}