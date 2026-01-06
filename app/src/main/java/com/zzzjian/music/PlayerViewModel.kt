package com.zzzjian.music

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.zzzjian.music.domain.model.PlaybackState
import com.zzzjian.music.domain.model.RepeatMode
import com.zzzjian.music.domain.model.Song
import com.zzzjian.music.domain.repository.MusicRepository
import com.zzzjian.music.player.PlayerManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlayerViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = MusicRepository()
    private val _songs = MutableStateFlow<List<Song>>(emptyList())
    val songs: StateFlow<List<Song>> = _songs
    val playback: StateFlow<PlaybackState> = PlayerManager.state

    fun initialize() {
        PlayerManager.initialize(getApplication())
        viewModelScope.launch {
            val s = repo.getAllSongs(getApplication())
            _songs.value = s
        }
    }

    fun play(index: Int) {
        PlayerManager.setQueue(_songs.value, index)
    }

    fun playPause() {
        PlayerManager.playPause()
    }

    fun next() {
        PlayerManager.next(_songs.value)
    }

    fun previous() {
        PlayerManager.previous(_songs.value)
    }

    fun setRepeat(mode: RepeatMode) {
        PlayerManager.setRepeat(mode)
    }
}
