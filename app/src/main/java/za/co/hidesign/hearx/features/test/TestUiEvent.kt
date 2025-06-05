package za.co.hidesign.hearx.features.test

sealed class TestUiEvent {
    data class InputChanged(val input: String) : TestUiEvent()
    object StartRound : TestUiEvent()
    object SubmitDigits : TestUiEvent()
    object SubmitTestScore : TestUiEvent()
    object DismissDialog : TestUiEvent()
}