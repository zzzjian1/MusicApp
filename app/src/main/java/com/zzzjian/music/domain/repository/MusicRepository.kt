package com.zzzjian.music.domain.repository

import android.content.Context
import com.zzzjian.music.data.media.MediaScanner
import com.zzzjian.music.domain.model.Song

class MusicRepository(private val scanner: MediaScanner = MediaScanner()) {
    suspend fun getAllSongs(context: Context): List<Song> {
        return scanner.scanSongs(context)
    }
}
