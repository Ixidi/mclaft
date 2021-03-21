package xyz.ixidi.mclaft.chat

sealed class HoverEvent {

    object None : HoverEvent()
    class ShowTooltip(val tooltip: Component) : HoverEvent()

}