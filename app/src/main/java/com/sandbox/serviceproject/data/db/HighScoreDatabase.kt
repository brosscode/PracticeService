package com.sandbox.serviceproject.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [HighScoreEntity::class], version = 1)
abstract class HighScoreDatabase : RoomDatabase() {
    abstract fun highScoreDao(): HighScoreDao

    companion object {
        const val NAME = "high_score_db"
        const val TABLE_SIZE = 10
    }
}
