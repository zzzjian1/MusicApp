package com.zzzjian.music.data.media

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.zzzjian.music.domain.model.Song

import android.content.ContentUris

class MediaScanner {
    fun scanSongs(context: Context): List<Song> {
        val list = mutableListOf<Song>()
        val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.MIME_TYPE
        )
        // More robust selection: Music OR explicit FLAC support
        val selection = "(${MediaStore.Audio.Media.IS_MUSIC} != 0 OR ${MediaStore.Audio.Media.MIME_TYPE} LIKE 'audio/flac' OR ${MediaStore.Audio.Media.MIME_TYPE} LIKE 'application/x-flac')"
        
        context.contentResolver.query(uri, projection, selection, null, null)?.use { c ->
            val idIdx = c.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleIdx = c.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistIdx = c.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val albumIdx = c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val durationIdx = c.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val dataIdx = c.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            val albumIdIdx = c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
            while (c.moveToNext()) {
                val id = c.getLong(idIdx)
                val title = c.getString(titleIdx) ?: ""
                val artist = c.getString(artistIdx) ?: ""
                val album = c.getString(albumIdx) ?: ""
                val duration = c.getLong(durationIdx)
                val path = c.getString(dataIdx) ?: ""
                val albumId = c.getLong(albumIdIdx)
                
                val contentUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id).toString()
                
                val albumArtUri = ContentUris.withAppendedId(
                    Uri.parse("content://media/external/audio/albumart"),
                    albumId
                ).toString()
                
                list.add(
                    com.zzzjian.music.domain.model.Song(
                        id,
                        title,
                        artist,
                        album,
                        duration,
                        path,
                        albumId,
                        albumArtUri,
                        contentUri
                    )
                )
            }
        }
        return list
    }
}
