package za.co.hidesign.hearx.features.test.data

import za.co.hidesign.hearx.features.test.data.model.TestUploadRequest
import za.co.hidesign.hearx.features.test.data.remote.TestApi
import javax.inject.Inject

class TestRepository @Inject constructor(private val api: TestApi) {
    suspend fun uploadTest(request: TestUploadRequest) {
        api.uploadTest(request)
    }
}