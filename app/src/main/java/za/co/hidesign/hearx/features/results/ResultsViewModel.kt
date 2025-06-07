package za.co.hidesign.hearx.features.results

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import za.co.hidesign.hearx.features.results.data.model.TestResultEntity
import za.co.hidesign.hearx.features.results.domain.usecase.GetTestResultsUseCase
import javax.inject.Inject

@HiltViewModel
class ResultsViewModel @Inject constructor(
    getTestResultsUseCase: GetTestResultsUseCase
) : ViewModel() {
    val results: StateFlow<List<TestResultEntity>> =
        getTestResultsUseCase().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
}