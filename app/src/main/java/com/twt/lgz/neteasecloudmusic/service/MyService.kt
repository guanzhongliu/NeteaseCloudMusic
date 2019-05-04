package com.twt.lgz.neteasecloudmusic.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder

class MyService : Service() {
    private var playList: ArrayList<String?> = ArrayList()
    private var position = -1
    private var id: String? = ""
    private var url: String? = ""
    private var myBinder = MyBinder()
    private var mediaPlayer: MediaPlayer? = null

    inner class MyBinder : Binder() {
        fun getService(): MyService {
            return this@MyService
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return myBinder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        id = intent?.getStringExtra("url")

        if (id != null) {
            play(id)
            mediaPlayer?.setOnErrorListener { _, _, _ ->
                play(id)
                false
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {

        super.onDestroy()
    }

    private fun playByPosition(position: Int): Boolean {
        when (mediaPlayer) {
            null -> mediaPlayer = MediaPlayer()
        }
        mediaPlayer?.let {
            it.reset()
            try {
                it.setDataSource(playList[position])
                it.prepare()
                it.start()
                return true
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return false
    }

    fun play(url: String?): Boolean {
        when (mediaPlayer) {
            null -> mediaPlayer = MediaPlayer()
        }
        mediaPlayer?.let {
            it.reset()
            try {
                it.setDataSource(url)
                it.prepare()
                it.start()
                if (!playList.contains(url)) {
                    playList.add(url)
                    position++
                }
                return true
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return false
    }

    fun playNext() {
        position++
        if (position >= playList.size) {
            position = 0
        }
        url = playList[position]
        if (playByPosition(position)) mediaPlayer?.seekTo(0)
    }

    fun playPrior() {
        position--
        when (position) {
            -1 -> position = playList.size - 1
        }
        url = playList[position]
        if (playByPosition(position)) mediaPlayer?.seekTo(0)
    }

    fun playingControl() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
            } else {
                it.start()
            }
        }
    }

    fun isPlaying(): Boolean {
        return mediaPlayer!!.isPlaying
    }

    fun getCurrentPosition(): Int {
        return mediaPlayer?.currentPosition ?: 0
    }

    fun getDuration(): Int {
        return mediaPlayer?.duration?: 4
    }

    fun seekTo(position: Int) {
        mediaPlayer?.seekTo(position)
    }
}