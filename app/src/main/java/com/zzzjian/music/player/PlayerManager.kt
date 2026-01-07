package com.zzzjian.music.player

import android.content.Context
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.zzzjian.music.domain.model.PlaybackState
import com.zzzjian.music.domain.model.RepeatMode
import com.zzzjian.music.domain.model.Song
import kotlinx.coroutines.*

object PlayerManager {
    private var exo: ExoPlayer? = null
    private var currentPlaylist: List<Song> = emptyList()
    
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var progressJob: Job? = null

    private val _state = MutableStateFlow(
        PlaybackState(false, null, 0L, 0L, RepeatMode.NONE)
    )
    val state: StateFlow<PlaybackState> = _state

    private val listener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            _state.value = _state.value.copy(isPlaying = isPlaying)
            if (isPlaying) {
                startProgressUpdater()
            } else {
                stopProgressUpdater()
            }
        }

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            updateCurrentSong()
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            updateCurrentSong()
            if (playbackState == Player.STATE_READY && _state.value.isPlaying) {
                 startProgressUpdater()
            }
        }
    }

    private fun startProgressUpdater() {
        if (progressJob?.isActive == true) return
        progressJob = scope.launch {
            while (isActive) {
                val p = exo ?: break
                _state.value = _state.value.copy(
                    position = p.currentPosition,
                    duration = p.duration.coerceAtLeast(0L)
                )
                delay(500) // Update every 500ms
            }
        }
    }

    private fun stopProgressUpdater() {
        progressJob?.cancel()
        progressJob = null
    }

    fun initialize(context: Context) {
        if (exo == null) {
            exo = ExoPlayer.Builder(context).build().apply {
                addListener(listener)
            }
        }
    }
    
    private fun updateCurrentSong() {
        val p = exo ?: return
        val idx = p.currentMediaItemIndex
        val song = currentPlaylist.getOrNull(idx)
        // Only update if song changed to avoid unnecessary recompositions
        if (_state.value.currentSong != song) {
            _state.value = _state.value.copy(currentSong = song)
        }
    }

    fun setQueue(songs: List<Song>, index: Int) {
        val p = exo ?: return
        currentPlaylist = songs
        p.clearMediaItems()
        songs.forEach { s -> p.addMediaItem(MediaItem.fromUri(s.path)) }
        p.seekTo(index, 0L)
        p.prepare()
        p.play()
        // Listener will update state, but we set initial state here to be responsive
        _state.value = _state.value.copy(isPlaying = true, currentSong = songs.getOrNull(index))
    }

    fun prepareQueue(songs: List<Song>) {
        val p = exo ?: return
        currentPlaylist = songs
        p.clearMediaItems()
        songs.forEach { s -> p.addMediaItem(MediaItem.fromUri(s.path)) }
        p.seekTo(0, 0L)
        p.prepare()
        // Do NOT play
        _state.value = _state.value.copy(isPlaying = false, currentSong = songs.getOrNull(0))
    }

    fun playPause() {
        val p = exo ?: return
        if (p.isPlaying) {
            p.pause()
        } else {
            p.play()
        }
    }

    fun next() {
        val p = exo ?: return
        if (currentPlaylist.isEmpty()) return
        
        val currentIndex = p.currentMediaItemIndex
        val nextIndex = if (currentIndex < currentPlaylist.size - 1) currentIndex + 1 else 0
        
        p.seekTo(nextIndex, 0L)
        if (!p.isPlaying) {
            p.play()
        }
    }

    fun previous() {
        val p = exo ?: return
        if (currentPlaylist.isEmpty()) return
        
        val currentIndex = p.currentMediaItemIndex
        val prevIndex = if (currentIndex > 0) currentIndex - 1 else currentPlaylist.size - 1
        
        p.seekTo(prevIndex, 0L)
        if (!p.isPlaying) {
            p.play()
        }
    }

    fun setRepeat(mode: RepeatMode) {
        val p = exo ?: return
        when (mode) {
            RepeatMode.NONE -> p.repeatMode = ExoPlayer.REPEAT_MODE_OFF
            RepeatMode.ONE -> p.repeatMode = ExoPlayer.REPEAT_MODE_ONE
            RepeatMode.ALL -> p.repeatMode = ExoPlayer.REPEAT_MODE_ALL
        }
        _state.value = _state.value.copy(repeatMode = mode)
    }

    fun seekTo(position: Long) {
        val p = exo ?: return
        p.seekTo(position)
        _state.value = _state.value.copy(position = position)
    }
}
