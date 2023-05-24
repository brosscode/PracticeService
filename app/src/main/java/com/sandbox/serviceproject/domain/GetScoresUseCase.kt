package com.sandbox.serviceproject.domain

interface GetScoresUseCase {
    suspend operator fun invoke(): List<Score>
}
