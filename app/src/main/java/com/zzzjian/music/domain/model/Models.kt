package com.zzzjian.music.domain.model

data class Song(
    val id: Long,
    val title: String,
    val artist: String,
    val album: String,
    val duration: Long,
    val path: String,
    val albumId: Long?,
    val coverUrl: String? = null
)

data class Playlist(
    val id: Long,
    val name: String,
    val createdAt: Long,
    val songs: List<Song>
)

enum class RepeatMode { NONE, ONE, ALL }

data class PlaybackState(
    val isPlaying: Boolean,
    val currentSong: Song?,
    val position: Long,
    val duration: Long,
    val repeatMode: RepeatMode
)
