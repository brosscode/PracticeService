package com.sandbox.serviceproject.domain

import com.sandbox.serviceproject.data.repository.HighScoreRepository

class GetScoresUseCaseImpl(
    private val repository: HighScoreRepository,
) : GetScoresUseCase {
    override suspend fun invoke(): List<Score> {
        return repository.getScores()
            .sortedWith(compareByDescending<Score> { it.score }.thenBy { it.date })
    }
}
