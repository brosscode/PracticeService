package com.sandbox.serviceproject.domain

import com.sandbox.serviceproject.data.db.HighScoreEntity

data class Score(
    val score: Long,
    val date: Long,
)

fun Score.toHighScoreEntity(): HighScoreEntity = HighScoreEntity(this.score, this.date)
