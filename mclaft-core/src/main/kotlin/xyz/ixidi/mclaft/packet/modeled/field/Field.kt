package xyz.ixidi.mclaft.packet.modeled.field

import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.ByteWriteChannel
import xyz.ixidi.mclaft.packet.modeled.field.io.FieldIO
import kotlin.reflect.KProperty

class Field<T>(
    private val io: FieldIO<T>
) {

    class FieldNotInitializedException : Exception("Field is not initialized.")

    private var value: T? = null

    private fun value() = value ?: throw FieldNotInitializedException()

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T = value ?: throw FieldNotInitializedException()

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = value
    }

    suspend fun read(input: ByteReadChannel) {
        value = io.run { input.readField() }
    }

    suspend fun write(output: ByteWriteChannel) {
        io.run { output.writeField(value()) }
    }

}