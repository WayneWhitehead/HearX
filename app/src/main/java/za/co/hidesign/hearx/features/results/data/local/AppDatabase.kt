package za.co.hidesign.hearx.features.results.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import za.co.hidesign.hearx.features.results.data.model.TestResultEntity

@Database(entities = [TestResultEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun testResultDao(): TestResultDao
}