package za.co.hidesign.hearx.features.test.data

import za.co.hidesign.hearx.features.results.data.local.TestResultDao
import za.co.hidesign.hearx.features.results.data.model.TestResultEntity
import za.co.hidesign.hearx.features.test.data.model.TestUploadRequest
import za.co.hidesign.hearx.features.test.data.remote.TestApi
import javax.inject.Inject

class TestRepository @Inject constructor(
    private val api: TestApi,
    private val testResultDao: TestResultDao
) {
    suspend fun uploadTest(request: TestUploadRequest) {
        api.uploadTest(request)
    }

    suspend fun saveTestResult(score: Int) {
        testResultDao.insert(TestResultEntity(score = score))
    }

    fun getAllResults() = testResultDao.getAllResults()
}