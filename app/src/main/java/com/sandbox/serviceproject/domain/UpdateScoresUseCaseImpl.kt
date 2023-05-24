package com.sandbox.serviceproject.domain

import com.sandbox.serviceproject.data.repository.HighScoreRepository

class UpdateScoresUseCaseImpl(
    private val repository: HighScoreRepository,
) : UpdateScoresUseCase {
    override suspend fun invoke(score: Score) {
        repository.updateScores(score.toHighScoreEntity())
    }
}
