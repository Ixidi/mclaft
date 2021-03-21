package xyz.ixidi.mclaft.connection

sealed class Compression {

    object Disabled : Compression()
    class Enabled(val threshold: Int) : Compression() {

        init {
            if (threshold <= 0) throw Exception("Threshold must be bigger than zero.")
        }

    }

}
