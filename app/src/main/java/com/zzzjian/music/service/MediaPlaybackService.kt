package com.zzzjian.music.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.zzzjian.music.player.PlayerManager

class MediaPlaybackService : Service() {
    override fun onCreate() {
        PlayerManager.initialize(applicationContext)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
