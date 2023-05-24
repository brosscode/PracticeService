package com.sandbox.serviceproject.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HighScoreDao {

    @Insert
    suspend fun insert(highScoreEntity: HighScoreEntity)

    @Query(
        """
            SELECT * FROM ${HighScoreEntity.TABLE_NAME}
        """
    )
    suspend fun getScores(): List<HighScoreEntity>

    @Query(
        """
            DELETE FROM ${HighScoreEntity.TABLE_NAME}
            WHERE ${HighScoreEntity.DATABASE_ID} NOT IN (
            SELECT ${HighScoreEntity.DATABASE_ID}
            FROM ${HighScoreEntity.TABLE_NAME}
            ORDER BY ${HighScoreEntity.SCORE_VALUE} DESC, ${HighScoreEntity.DATE} ASC LIMIT :tableSize
            )
        """
    ) suspend fun delete(tableSize:Int)
}
