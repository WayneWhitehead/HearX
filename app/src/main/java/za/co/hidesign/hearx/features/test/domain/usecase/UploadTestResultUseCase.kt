package za.co.hidesign.hearx.features.test.domain.usecase

import za.co.hidesign.hearx.features.test.data.TestRepository
import za.co.hidesign.hearx.features.test.data.model.TestUploadRequest
import javax.inject.Inject

class UploadTestResultUseCase @Inject constructor(private val repository: TestRepository) {
    suspend operator fun invoke(request: TestUploadRequest): Result<Unit> {
        val response = repository.uploadTest(request)
        return if (!response.isSuccessful) {
            Result.failure(Exception("Failed to upload test result: ${response.code()} ${response.message()}"))
        } else {
            Result.success(Unit)
        }
    }
}
