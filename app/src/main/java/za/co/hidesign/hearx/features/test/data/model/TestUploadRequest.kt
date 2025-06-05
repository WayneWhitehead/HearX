package za.co.hidesign.hearx.features.test.data.model

data class TestUploadRequest(
    val score: Int,
    val rounds: List<Round>
) {
    data class Round(
        val difficulty: Int,
        val triplet_played: String,
        val triplet_answered: String
    )
}