package com.sandbox.serviceproject.data.repository

import android.util.Log
import com.sandbox.serviceproject.data.db.HighScoreDao
import com.sandbox.serviceproject.data.db.HighScoreDatabase
import com.sandbox.serviceproject.data.db.HighScoreEntity
import com.sandbox.serviceproject.data.db.toScore
import com.sandbox.serviceproject.domain.Score

class HighScoreRepositoryImpl(
    private val dao: HighScoreDao,
) : HighScoreRepository {
    override suspend fun getScores(): List<Score> {
        return try {
            dao.getScores().map { it.toScore() }
        } catch (e: Exception) {
            Log.e("Repository", "Error when trying to get scores from Database")
            listOf()
        }
    }

    override suspend fun updateScores(scoreEntity: HighScoreEntity) {
        try {
            dao.insert(scoreEntity)
            dao.delete(HighScoreDatabase.TABLE_SIZE)
        } catch (e: Exception) {
            Log.e("Repository", "Error when trying to insert or delete")
        }
    }
}
