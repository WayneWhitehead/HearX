package za.co.hidesign.hearx.features.results.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "test_results")
data class TestResultEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val score: Int,
    val timestamp: Long = System.currentTimeMillis()
)