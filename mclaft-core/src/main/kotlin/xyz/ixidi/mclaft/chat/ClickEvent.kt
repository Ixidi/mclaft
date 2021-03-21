package xyz.ixidi.mclaft.chat

sealed class ClickEvent {

    object None : ClickEvent()
    class OpenUrl(val url: String) : ClickEvent()
    class SuggestCommand(val command: String) : ClickEvent()
    class RunCommand(val command: String) : ClickEvent()

}
