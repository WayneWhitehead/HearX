package za.co.hidesign.hearx.features.test

data class TestUiState(
    val round: Int = 1,
    val input: String = "",
    val score: Int = 0,
    val difficulty: Int = 5,
    val isPlaying: Boolean = false,
    val isWaiting: Boolean = true,
    val totalRounds: Int = 10,
    val countdown: Int = 0,
    val results: List<Pair<List<Int>, String>> = emptyList()
)