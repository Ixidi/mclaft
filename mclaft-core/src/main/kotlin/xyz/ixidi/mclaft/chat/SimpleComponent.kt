package xyz.ixidi.mclaft.chat

import java.awt.Color

class SimpleComponent(
    override var text: String,
) : Component {

    override var color: Color = Color.WHITE
    override val emphasises: MutableSet<Emphasis> = HashSet()
    override var clickEvent: ClickEvent = ClickEvent.None
    override var hoverEvent: HoverEvent = HoverEvent.None
    override val extras: MutableList<Component> = ArrayList()

    override fun addExtra(component: Component) {
        extras.add(component)
    }

    override fun asText(): String = joinText()

    private fun Component.joinText(): String {
        return "$text${extras.joinToString("") { it.joinText() }}"
    }

}