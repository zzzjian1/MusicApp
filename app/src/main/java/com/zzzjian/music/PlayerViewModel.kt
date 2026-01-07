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

import com.zzzjian.music.domain.model.MockData

enum class SongListType {
    ALL, FAVORITE, RECENT, DOWNLOAD
}

class PlayerViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = MusicRepository()
    
    private val _songs = MutableStateFlow<List<Song>>(emptyList())
    val songs: StateFlow<List<Song>> = _songs
    
    private val _favoriteSongs = MutableStateFlow<List<Song>>(emptyList())
    val favoriteSongs: StateFlow<List<Song>> = _favoriteSongs
    
    private val _recentSongs = MutableStateFlow<List<Song>>(emptyList())
    val recentSongs: StateFlow<List<Song>> = _recentSongs
    
    private val _downloadedSongs = MutableStateFlow<List<Song>>(emptyList())
    val downloadedSongs: StateFlow<List<Song>> = _downloadedSongs

    private var _currentQueue: List<Song> = emptyList()
    val playback: StateFlow<PlaybackState> = PlayerManager.state

    fun initialize() {
        PlayerManager.initialize(getApplication())
        viewModelScope.launch {
            // Observe playback state to update recent songs on auto-next
            PlayerManager.state.collect { state ->
                state.currentSong?.let { song ->
                    // Avoid adding duplicate if it's the same as top
                    if (_recentSongs.value.firstOrNull() != song) {
                        addToRecent(song)
                    }
                }
            }
        }

        viewModelScope.launch {
            // Try to load real songs first
            var s = repo.getAllSongs(getApplication())
            // Fallback to MockData if no local songs found (for testing/demo)
            // if (s.isEmpty()) {
            //    s = MockData.allSongs
            // }
            
            _songs.value = s
            
            // Auto-classify Downloaded songs based on path
            val realDownloads = s.filter { song -> 
                song.path.contains("Download", ignoreCase = true) || 
                song.path.contains("Downloader", ignoreCase = true)
            }
            
            _favoriteSongs.value = emptyList() // MockData.favoriteSongs
            _recentSongs.value = emptyList() // MockData.recentSongs
            
            // Merge mock downloads with real ones for demo purposes, or just use real ones
            // Let's prioritize real downloads if any found, otherwise keep mock to show UI
            if (realDownloads.isNotEmpty()) {
                _downloadedSongs.value = realDownloads
            } else {
                _downloadedSongs.value = emptyList() // MockData.downloadedSongs
            }
            
            _currentQueue = s // Default queue
            if (s.isNotEmpty()) {
                PlayerManager.prepareQueue(s)
            }
        }
    }

    fun play(songs: List<Song>, index: Int) {
        _currentQueue = songs
        PlayerManager.setQueue(songs, index)
        
        // Add to recent
        val song = songs.getOrNull(index)
        if (song != null) {
            addToRecent(song)
        }
    }
    
    private fun addToRecent(song: Song) {
        val currentList = _recentSongs.value.toMutableList()
        currentList.remove(song) // Remove if exists to move to top
        currentList.add(0, song) // Add to top
        if (currentList.size > 50) { // Limit recent history size
            currentList.removeAt(currentList.lastIndex)
        }
        _recentSongs.value = currentList
    }

    fun playPause() {
        PlayerManager.playPause()
    }

    fun next() {
        PlayerManager.next()
    }

    fun previous() {
        PlayerManager.previous()
    }

    fun setRepeat(mode: RepeatMode) {
        PlayerManager.setRepeat(mode)
    }

    fun seekTo(position: Long) {
        PlayerManager.seekTo(position)
    }

    fun deleteSong(song: Song, type: SongListType = SongListType.ALL) {
        when (type) {
            SongListType.ALL -> {
                val list = _songs.value.toMutableList()
                list.remove(song)
                _songs.value = list
            }
            SongListType.FAVORITE -> {
                val list = _favoriteSongs.value.toMutableList()
                list.remove(song)
                _favoriteSongs.value = list
            }
            SongListType.RECENT -> {
                val list = _recentSongs.value.toMutableList()
                list.remove(song)
                _recentSongs.value = list
            }
            SongListType.DOWNLOAD -> {
                val list = _downloadedSongs.value.toMutableList()
                list.remove(song)
                _downloadedSongs.value = list
            }
        }
    }

    fun restoreSong(song: Song, index: Int, type: SongListType = SongListType.ALL) {
        val targetFlow = when (type) {
            SongListType.ALL -> _songs
            SongListType.FAVORITE -> _favoriteSongs
            SongListType.RECENT -> _recentSongs
            SongListType.DOWNLOAD -> _downloadedSongs
        }
        
        val list = targetFlow.value.toMutableList()
        if (index in 0..list.size) {
            list.add(index, song)
        } else {
            list.add(song)
        }
        targetFlow.value = list
    }

    fun toggleFavorite(song: Song) {
        val currentList = _favoriteSongs.value.toMutableList()
        if (currentList.contains(song)) {
            currentList.remove(song)
        } else {
            currentList.add(0, song) // Add to top
        }
        _favoriteSongs.value = currentList
    }

    fun isFavorite(song: Song): Boolean {
        return _favoriteSongs.value.contains(song)
    }

    fun clearAllSongs() {
        _songs.value = emptyList()
        _currentQueue = emptyList()
        PlayerManager.stop()
    }
}
