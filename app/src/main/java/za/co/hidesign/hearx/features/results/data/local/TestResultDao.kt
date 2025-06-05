package za.co.hidesign.hearx.features.results.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import za.co.hidesign.hearx.features.results.data.model.TestResultEntity

@Dao
interface TestResultDao {
    @Insert
    suspend fun insert(result: TestResultEntity)

    @Query("SELECT * FROM test_results ORDER BY score DESC")
    fun getAllResults(): Flow<List<TestResultEntity>>
}