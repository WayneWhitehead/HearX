package za.co.hidesign.hearx.features.test

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import za.co.hidesign.hearx.features.test.data.model.TestUploadRequest
import za.co.hidesign.hearx.features.test.domain.usecase.GenerateTripletListUseCase
import za.co.hidesign.hearx.features.test.domain.usecase.PlayAudioUseCase
import za.co.hidesign.hearx.features.test.domain.usecase.SaveTestResultUseCase
import za.co.hidesign.hearx.features.test.domain.usecase.UploadTestResultUseCase
import javax.inject.Inject

@HiltViewModel
class TestViewModel @Inject constructor(
    generateTripletListUseCase: GenerateTripletListUseCase,
    private val playAudioUseCase: PlayAudioUseCase,
    private val uploadTestResultUseCase: UploadTestResultUseCase,
    private val saveTestResultUseCase: SaveTestResultUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TestUiState())
    val uiState: StateFlow<TestUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableStateFlow<TestNavEvent?>(null)
    val navigationEvent: StateFlow<TestNavEvent?> = _navigationEvent.asStateFlow()

    private val triplets = generateTripletListUseCase(TOTAL_ROUNDS)

    private var playJob: Job? = null

    fun onNavEvent(event: TestNavEvent) {
        viewModelScope.launch {
            when (event) {
                TestNavEvent.NavigateHome -> {
                    cancelAudio()
                    _navigationEvent.emit(TestNavEvent.NavigateHome)
                }
                TestNavEvent.DisplayResultsDialog -> {
                    _navigationEvent.emit(TestNavEvent.DisplayResultsDialog)
                }
                is TestNavEvent.ShowErrorDialog -> {
                    _uiState.value = _uiState.value.copy(showErrorDialog = true, errorTitle = event.title, errorDescription = event.description)
                }
                TestNavEvent.HideErrorDialog -> {
                    _uiState.value = _uiState.value.copy(showErrorDialog = false)
                }
            }
        }
    }

    fun onEvent(event: TestEvent) {
        viewModelScope.launch {
            when (event) {
                is TestEvent.InputChanged -> onInputChange(event.input)
                TestEvent.StartRound -> startRound()
                TestEvent.SubmitDigits -> submitDigits()
                TestEvent.SubmitTestResult -> submitTestScore()
            }
        }
    }

    private suspend fun startRound() {
        val state = _uiState.value
        if (state.round < TOTAL_ROUNDS) {
            _uiState.value = state.copy(isWaiting = true)
            val delayMs = if (state.round == 0) FIRST_ROUND_DELAY_MS else OTHER_ROUND_DELAY_MS
            val seconds = (delayMs / 1000).toInt()
            for (i in seconds downTo 1) {
                _uiState.value = _uiState.value.copy(countdown = i)
                delay(1000)
            }
            _uiState.value = _uiState.value.copy(countdown = 0, isWaiting = false, isPlaying = true)
            val currentTriplet = triplets[state.round]
            playJob = viewModelScope.launch {
                playAudioUseCase(currentTriplet, state.difficulty) {
                    _uiState.value = _uiState.value.copy(isPlaying = false)
                }
            }
        } else if (state.round == TOTAL_ROUNDS) {
            onEvent(TestEvent.SubmitTestResult)
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
        val newDifficulty = if (correct) (state.difficulty + 1).coerceAtMost(TOTAL_ROUNDS) else (state.difficulty - 1).coerceAtLeast(1)
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
        val request = createTestUploadRequest(state)

        withContext(Dispatchers.IO) {
            uploadTestResultUseCase(request)
                .onSuccess {
                    saveTestResultUseCase(state.score)
                        .onSuccess {
                            _navigationEvent.emit(TestNavEvent.DisplayResultsDialog)
                        }
                        .onFailure { error ->
                            _navigationEvent.emit(TestNavEvent.ShowErrorDialog(
                                title = error.message,
                                description = error.cause?.message
                            ))
                        }
                }
                .onFailure { error ->
                    _navigationEvent.emit(TestNavEvent.ShowErrorDialog(
                        title = error.message,
                        description = error.cause?.message
                    ))
                }
        }
    }

    private fun createTestUploadRequest(state: TestUiState): TestUploadRequest {
        return TestUploadRequest(
            score = state.score,
            rounds = state.results.map { (triplet, answer) ->
                TestUploadRequest.Round(
                    difficulty = state.difficulty,
                    triplet_played = triplet.joinToString(""),
                    triplet_answered = answer
                )
            }
        )
    }

    companion object {
        const val TOTAL_ROUNDS = 10
        const val FIRST_ROUND_DELAY_MS = 3000L
        const val OTHER_ROUND_DELAY_MS = 2000L
    }
}