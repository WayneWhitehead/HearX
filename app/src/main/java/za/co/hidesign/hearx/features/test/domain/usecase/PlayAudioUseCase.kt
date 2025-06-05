package za.co.hidesign.hearx.features.test.domain.usecase

import android.content.Context
import android.media.MediaPlayer
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import za.co.hidesign.hearx.R
import javax.inject.Inject

class PlayAudioUseCase @Inject constructor(@ApplicationContext private val context: Context) {

    companion object {
        const val NOISE_START_DELAY_MS = 500L
        const val DIGIT_PLAY_DELAY_MS = 700L
        const val NOISE_END_DELAY_MS = 300L
    }

    private val noiseResMap = mapOf(
        1 to R.raw.noise_1,
        2 to R.raw.noise_2,
        3 to R.raw.noise_3,
        4 to R.raw.noise_4,
        5 to R.raw.noise_5,
        6 to R.raw.noise_6,
        7 to R.raw.noise_7,
        8 to R.raw.noise_8,
        9 to R.raw.noise_9,
        10 to R.raw.noise_10
    )
    private val digitResMap = mapOf(
        1 to R.raw.digit_1,
        2 to R.raw.digit_2,
        3 to R.raw.digit_3,
        4 to R.raw.digit_4,
        5 to R.raw.digit_5,
        6 to R.raw.digit_6,
        7 to R.raw.digit_7,
        8 to R.raw.digit_8,
        9 to R.raw.digit_9
    )

    suspend operator fun invoke(triplet: List<Int>, difficulty: Int, onComplete: () -> Unit) =
        withContext(Dispatchers.IO) {
            val noiseRes = noiseResMap[difficulty] ?: return@withContext
            val noisePlayer = MediaPlayer.create(context, noiseRes)
            noisePlayer.start()
            delay(NOISE_START_DELAY_MS)
            for (digit in triplet) {
                val digitRes = digitResMap[digit] ?: continue
                val digitPlayer = MediaPlayer.create(context, digitRes)
                digitPlayer.start()
                delay(DIGIT_PLAY_DELAY_MS)
                digitPlayer.release()
            }
            delay(NOISE_END_DELAY_MS)
            noisePlayer.stop()
            noisePlayer.release()
            onComplete()
        }
}