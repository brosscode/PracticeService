package com.sandbox.serviceproject.data.repository

import com.sandbox.serviceproject.data.db.HighScoreEntity
import com.sandbox.serviceproject.domain.Score

interface HighScoreRepository {
    suspend fun getScores(): List<Score>
    suspend fun updateScores(scoreEntity: HighScoreEntity)
}
