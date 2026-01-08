package com.zzzjian.music.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.zzzjian.music.data.db.entity.ChatHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {
    @Query("SELECT * FROM chat_history ORDER BY timestamp ASC")
    fun getAllMessages(): Flow<List<ChatHistoryEntity>>

    @Insert
    suspend fun insertMessage(message: ChatHistoryEntity)

    @Query("DELETE FROM chat_history")
    suspend fun clearHistory()
}
