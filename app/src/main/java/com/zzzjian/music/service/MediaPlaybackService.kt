package com.zzzjian.music.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import coil.ImageLoader
import coil.request.ImageRequest
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.zzzjian.music.MainActivity
import com.zzzjian.music.R
import com.zzzjian.music.player.PlayerManager

class MediaPlaybackService : Service() {

    private var playerNotificationManager: PlayerNotificationManager? = null
    private var mediaSession: MediaSessionCompat? = null
    private var mediaSessionConnector: MediaSessionConnector? = null
    
    private val NOTIFICATION_ID = 101
    private val NOTIFICATION_CHANNEL_ID = "music_channel_id"
    private val MEDIA_SESSION_TAG = "MusicAppSession"

    override fun onCreate() {
        super.onCreate()
        PlayerManager.initialize(applicationContext)
        val player = PlayerManager.getPlayer() ?: return

        // 1. 初始化 MediaSession
        mediaSession = MediaSessionCompat(this, MEDIA_SESSION_TAG).apply {
            isActive = true
        }

        // 2. 连接 ExoPlayer 和 MediaSession
        mediaSessionConnector = MediaSessionConnector(mediaSession!!)
        mediaSessionConnector?.setPlayer(player)

        // 3. 设置通知
        setupNotification(player)
    }

    private fun setupNotification(player: Player) {
        val descriptionAdapter = object : PlayerNotificationManager.MediaDescriptionAdapter {
            override fun getCurrentContentTitle(player: Player): CharSequence {
                return PlayerManager.state.value.currentSong?.title ?: "Hajimi Music"
            }

            override fun createCurrentContentIntent(player: Player): PendingIntent? {
                val intent = Intent(applicationContext, MainActivity::class.java)
                return PendingIntent.getActivity(
                    applicationContext, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            }

            override fun getCurrentContentText(player: Player): CharSequence? {
                return PlayerManager.state.value.currentSong?.artist ?: "Hajimi一下，烦恼全放下"
            }

            override fun getCurrentLargeIcon(
                player: Player,
                callback: PlayerNotificationManager.BitmapCallback
            ): Bitmap? {
                val song = PlayerManager.state.value.currentSong
                
                // Use Coil to load image asynchronously
                val context = applicationContext
                val loader = ImageLoader(context)
                val request = ImageRequest.Builder(context)
                    .data(song?.coverUrl ?: R.drawable.ic_hajimi_logo)
                    .target { result ->
                        val bitmap = (result as BitmapDrawable).bitmap
                        callback.onBitmap(bitmap)
                    }
                    .build()
                
                loader.enqueue(request)
                return null 
            }
        }

        playerNotificationManager = PlayerNotificationManager.Builder(
            this,
            NOTIFICATION_ID,
            NOTIFICATION_CHANNEL_ID
        )
            .setChannelNameResourceId(R.string.app_name)
            .setChannelDescriptionResourceId(R.string.app_name)
            .setMediaDescriptionAdapter(descriptionAdapter)
            .setNotificationListener(object : PlayerNotificationManager.NotificationListener {
                override fun onNotificationPosted(
                    notificationId: Int,
                    notification: Notification,
                    ongoing: Boolean
                ) {
                    if (ongoing) {
                        startForeground(notificationId, notification)
                    } else {
                        stopForeground(false)
                    }
                }

                override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
                    stopSelf()
                }
            })
            .build()

        // 关键步骤：设置 MediaSessionToken，启用 MediaStyle
        playerNotificationManager?.setMediaSessionToken(mediaSession!!.sessionToken)
        playerNotificationManager?.setSmallIcon(R.drawable.ic_launcher_foreground)
        playerNotificationManager?.setUseFastForwardAction(false)
        playerNotificationManager?.setUseRewindAction(false)
        playerNotificationManager?.setUseNextActionInCompactView(true)
        playerNotificationManager?.setUsePreviousActionInCompactView(true)

        playerNotificationManager?.setPlayer(player)
    }

    override fun onDestroy() {
        mediaSession?.release()
        mediaSessionConnector?.setPlayer(null)
        playerNotificationManager?.setPlayer(null)
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
