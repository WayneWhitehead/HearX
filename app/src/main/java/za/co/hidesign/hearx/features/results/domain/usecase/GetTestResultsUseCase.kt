package za.co.hidesign.hearx.features.results.domain.usecase

import kotlinx.coroutines.flow.Flow
import za.co.hidesign.hearx.features.results.data.model.TestResultEntity
import za.co.hidesign.hearx.features.test.data.TestRepository
import javax.inject.Inject

class GetTestResultsUseCase @Inject constructor(private val repository: TestRepository) {
    operator fun invoke(): Flow<List<TestResultEntity>> = repository.getAllResults()
}