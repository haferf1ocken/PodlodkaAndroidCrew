package com.example.podlodkaandroidcrew.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg sessions: SessionDatabase)

    @Query("SELECT * FROM session_table")
    fun getAll(): Flow<List<SessionDatabase>>

    @Query("SELECT * FROM session_table WHERE id = :key")
    fun getSessionWithId(key: String): Flow<SessionDatabase>
}