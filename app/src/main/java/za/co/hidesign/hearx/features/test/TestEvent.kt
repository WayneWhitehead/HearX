package za.co.hidesign.hearx.features.test

sealed interface TestEvent {
    data class InputChanged(val input: String) : TestEvent
    object StartRound : TestEvent
    object SubmitDigits : TestEvent
    object SubmitTestResult : TestEvent
}
