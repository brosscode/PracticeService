package com.sandbox.serviceproject.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sandbox.serviceproject.data.db.HighScoreEntity.Companion.TABLE_NAME
import com.sandbox.serviceproject.domain.Score

@Entity(tableName = TABLE_NAME)
class HighScoreEntity(
    @ColumnInfo(name = SCORE_VALUE) val scoreValue: Long,
    @ColumnInfo(name = DATE) val date: Long,
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = DATABASE_ID)
    var entityId: Int = 0

    companion object {
        const val TABLE_NAME = "high_score"
        const val DATABASE_ID = "high_score_database_id"
        const val DATE = "high_score_date"
        const val SCORE_VALUE = "high_score_duration"
    }
}

fun HighScoreEntity.toScore(): Score {
    return Score(this.scoreValue, this.date)
}
