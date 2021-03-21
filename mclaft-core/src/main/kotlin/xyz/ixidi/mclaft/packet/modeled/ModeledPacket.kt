package xyz.ixidi.mclaft.packet.modeled

import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.ByteWriteChannel
import xyz.ixidi.mclaft.packet.ConnectionState
import xyz.ixidi.mclaft.packet.Packet
import xyz.ixidi.mclaft.packet.PacketSource
import xyz.ixidi.mclaft.packet.modeled.field.Field
import xyz.ixidi.mclaft.packet.modeled.field.io.FieldIO
import xyz.ixidi.mclaft.packet.modeled.field.io.ShortFieldIO
import xyz.ixidi.mclaft.packet.modeled.field.io.StringFieldIO
import xyz.ixidi.mclaft.packet.modeled.field.io.UUIDFieldIO
import xyz.ixidi.mclaft.packet.modeled.field.io.VarIntFieldIO

abstract class ModeledPacket(
    override val id: Int,
    override val state: ConnectionState,
    override val source: PacketSource
) : Packet {

    private val fields = ArrayList<Field<*>>()

    private fun <T> f(io: FieldIO<T>) = Field(io).also { fields.add(it) }

    protected fun varInt() = f(VarIntFieldIO)
    protected fun short() = f(ShortFieldIO)
    protected fun string() = f(StringFieldIO)
    protected fun uuid() = f(UUIDFieldIO)

    override suspend fun ByteReadChannel.read() {
        fields.forEach {
            it.read(this)
        }
    }

    override suspend fun ByteWriteChannel.write() {
        fields.forEach {
            it.write(this)
        }
    }

}