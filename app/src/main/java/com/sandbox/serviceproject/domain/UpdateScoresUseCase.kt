package com.sandbox.serviceproject.domain

interface UpdateScoresUseCase {
    suspend operator fun invoke(score: Score)
}
