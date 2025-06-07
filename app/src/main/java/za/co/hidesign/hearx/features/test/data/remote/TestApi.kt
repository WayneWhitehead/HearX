package za.co.hidesign.hearx.features.test.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import za.co.hidesign.hearx.features.test.data.model.TestUploadRequest

interface TestApi {
    @Headers("Content-Type: application/json")
    @POST("https://enoqczf2j2pbadx.m.pipedream.net")
    suspend fun uploadTest(@Body request: TestUploadRequest): Response<Unit>
}