package com.zzzjian.music.player

import android.content.Context
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.zzzjian.music.domain.model.PlaybackState
import com.zzzjian.music.domain.model.RepeatMode
import com.zzzjian.music.domain.model.Song

object PlayerManager {
    private var exo: ExoPlayer? = null
    private val _state = MutableStateFlow(
        PlaybackState(false, null, 0L, 0L, RepeatMode.NONE)
    )
    val state: StateFlow<PlaybackState> = _state

    fun initialize(context: Context) {
        if (exo == null) exo = ExoPlayer.Builder(context).build()
    }

    fun setQueue(songs: List<Song>, index: Int) {
        val p = exo ?: return
        p.clearMediaItems()
        songs.forEach { s -> p.addMediaItem(MediaItem.fromUri(s.path)) }
        p.seekTo(index, 0L)
        p.prepare()
        p.play()
        _state.value = _state.value.copy(isPlaying = true, currentSong = songs.getOrNull(index))
    }

    fun playPause() {
        val p = exo ?: return
        if (p.isPlaying) {
            p.pause()
            _state.value = _state.value.copy(isPlaying = false)
        } else {
            p.play()
            _state.value = _state.value.copy(isPlaying = true)
        }
    }

    fun next(songs: List<Song>) {
        val p = exo ?: return
        p.seekToNext()
        val idx = p.currentMediaItemIndex
        _state.value = _state.value.copy(currentSong = songs.getOrNull(idx))
    }

    fun previous(songs: List<Song>) {
        val p = exo ?: return
        p.seekToPrevious()
        val idx = p.currentMediaItemIndex
        _state.value = _state.value.copy(currentSong = songs.getOrNull(idx))
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
}
