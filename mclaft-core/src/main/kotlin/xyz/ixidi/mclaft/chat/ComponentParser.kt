package xyz.ixidi.mclaft.chat

interface ComponentParser {

    fun parse(input: String, vararg textParameters: Pair<String, Any?>): Component

}