package xyz.ixidi.mclaft.chat

import java.awt.Color

interface Component {

    var text: String
    var color: Color
    val emphasises: MutableSet<Emphasis>

    var clickEvent: ClickEvent
    var hoverEvent: HoverEvent

    val extras: List<Component>

    fun addExtra(component: Component)
    fun asText(): String

}