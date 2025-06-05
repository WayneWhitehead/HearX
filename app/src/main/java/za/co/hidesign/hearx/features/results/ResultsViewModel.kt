package za.co.hidesign.hearx.features.results

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import za.co.hidesign.hearx.features.test.data.TestRepository
import javax.inject.Inject

@HiltViewModel
class ResultsViewModel @Inject constructor(
    repository: TestRepository
) : ViewModel() {
    val results = repository.getAllResults()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}