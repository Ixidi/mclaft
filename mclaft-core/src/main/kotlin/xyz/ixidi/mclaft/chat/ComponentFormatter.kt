package xyz.ixidi.mclaft.chat

interface ComponentFormatter {

    fun format(text: String, vararg textParameters: Pair<String, Any?>): Component

}