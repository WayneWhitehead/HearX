package za.co.hidesign.hearx.features.test

data class TestUiState(
    val round: Int = 0,
    val input: String = "",
    val showDialog: Boolean = false,
    val score: Int = 0,
    val difficulty: Int = 5,
    val isPlaying: Boolean = false,
    val isWaiting: Boolean = true,
    val totalRounds: Int = 10,
    val results: List<Pair<List<Int>, String>> = emptyList()
)