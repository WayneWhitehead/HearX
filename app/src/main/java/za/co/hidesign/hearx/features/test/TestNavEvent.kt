package za.co.hidesign.hearx.features.test

sealed interface TestNavEvent {
    object NavigateHome : TestNavEvent
    object DisplayResultsDialog : TestNavEvent
    data class ShowErrorDialog(val title: String? = null, val description: String? = null) : TestNavEvent
    object HideErrorDialog : TestNavEvent
}