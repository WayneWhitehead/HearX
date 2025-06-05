package za.co.hidesign.hearx.features.test

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import za.co.hidesign.hearx.features.test.data.model.TestUploadRequest
import za.co.hidesign.hearx.features.test.domain.usecase.GenerateTripletListUseCase
import za.co.hidesign.hearx.features.test.domain.usecase.PlayAudioUseCase
import za.co.hidesign.hearx.features.test.domain.usecase.UploadTestResultUseCase
import javax.inject.Inject

@HiltViewModel
class TestViewModel @Inject constructor(
    application: Application,
    generateTripletListUseCase: GenerateTripletListUseCase,
    private val playAudioUseCase: PlayAudioUseCase,
    private val uploadTestResultUseCase: UploadTestResultUseCase
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(TestUiState())
    val uiState: StateFlow<TestUiState> = _uiState

    private val _navigationEvent = MutableStateFlow<TestUiEvent?>(null)
    val navigationEvent: StateFlow<TestUiEvent?> = _navigationEvent

    private val triplets = generateTripletListUseCase(10)

    private var playJob: Job? = null

    fun onEvent(event: TestUiEvent) {
        viewModelScope.launch {
            when (event) {
                is TestUiEvent.InputChanged -> onInputChange(event.input)
                TestUiEvent.StartRound -> startRound()
                TestUiEvent.SubmitDigits -> submitDigits()
                TestUiEvent.NavigateHome -> {
                    cancelAudio()
                    _navigationEvent.emit(TestUiEvent.NavigateHome)
                }
                TestUiEvent.DisplayDialog -> {
                    _navigationEvent.emit(TestUiEvent.DisplayDialog)
                }
            }
        }
    }

    private suspend fun startRound() {
        val state = _uiState.value
        if (state.round <= state.totalRounds) {
            _uiState.value = state.copy(isWaiting = true)
            val delayMs = if (state.round == 1) FIRST_ROUND_DELAY_MS else OTHER_ROUND_DELAY_MS
            val seconds = (delayMs / 1000).toInt()
            for (i in seconds downTo 1) {
                _uiState.value = _uiState.value.copy(countdown = i)
                delay(1000)
            }
            _uiState.value = _uiState.value.copy(countdown = 0, isWaiting = false, isPlaying = true)
            val currentTriplet = triplets[state.round-1]
            playJob = viewModelScope.launch {
                playAudioUseCase(currentTriplet, state.difficulty) {
                    _uiState.value = _uiState.value.copy(isPlaying = false)
                }
            }
        } else if (state.round > state.totalRounds) {
            submitTestScore()
            onEvent(TestUiEvent.DisplayDialog)
        }
    }

    private fun cancelAudio() {
        playJob?.cancel()
        playJob = null
    }

    private fun onInputChange(newInput: String) {
        if (newInput.length <= 3) {
            _uiState.value = _uiState.value.copy(input = newInput)
        }
    }

    private fun submitDigits() {
        val state = _uiState.value
        val currentTriplet = triplets[state.round]
        val correct = state.input == currentTriplet.joinToString("")
        val newDifficulty = if (correct) (state.difficulty + 1).coerceAtMost(10) else (state.difficulty - 1).coerceAtLeast(1)
        val newScore = state.score + newDifficulty
        val newResults = state.results + (currentTriplet to state.input)
        _uiState.value = state.copy(
            results = newResults,
            difficulty = newDifficulty,
            score = newScore,
            round = state.round + 1,
            input = ""
        )
    }

    private suspend fun submitTestScore() {
        val state = _uiState.value
        val rounds = state.results.map { (triplet, answer) ->
            TestUploadRequest.Round(
                difficulty = state.difficulty,
                triplet_played = triplet.joinToString(""),
                triplet_answered = answer
            )
        }
        val request = TestUploadRequest(
            score = state.score,
            rounds = rounds
        )
        try {
            withContext(Dispatchers.IO) {
                uploadTestResultUseCase(request)
            }
        } catch (e: Exception) {

        }
    }

    companion object {
        const val FIRST_ROUND_DELAY_MS = 3000L
        const val OTHER_ROUND_DELAY_MS = 2000L
    }
}