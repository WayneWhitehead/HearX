package za.co.hidesign.hearx.features.test.domain.usecase

import za.co.hidesign.hearx.features.test.data.TestRepository
import javax.inject.Inject

class SaveTestResultUseCase @Inject constructor(private val repository: TestRepository) {
    suspend operator fun invoke(score: Int): Result<Unit> = runCatching {
        repository.saveTestResult(score)
    }
}
