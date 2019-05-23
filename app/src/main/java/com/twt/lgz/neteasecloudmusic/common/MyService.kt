package com.twt.lgz.neteasecloudmusic.common

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder

class MyService : Service() {

    private var playList: ArrayList<String?> = ArrayList()
    private var position = -1
    internal var id: String? = ""
    private var myBinder = MyBinder()
    private val mediaPlayer = MusicPlayer.musicPlayer

    inner class MyBinder : Binder() {
        fun getService(): MyService {
            return this@MyService
        }
    }

    override fun onCreate() {
        super.onCreate()
        mediaPlayer.setOnPreparedListener {
            it.start()
        }

    }

    override fun onBind(intent: Intent?): IBinder? {
        return myBinder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        id = intent?.getStringExtra("url")

        if (id != null) {
            play(id)
            mediaPlayer.setOnErrorListener { _, _, _ ->
                play(id)
                false
            }
        }

        return START_STICKY
    }

    private fun play(url: String?): Boolean {
        MusicPlayer.id = url!!
        mediaPlayer.apply {
            reset()
            try {
                setDataSource(url)
                prepareAsync()
//                it.prepare()
//                it.start()
//                if (!playList.contains(url)) {
//                    playList.add(url)
//                    position++
//                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return false
    }

    private fun playAll(position: Int): Boolean {
        mediaPlayer.let {
            it.reset()
            try {
                it.setDataSource(playList[position])
                it.prepareAsync()
//                it.prepare()
//                it.start()


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
        id = playList[position]
        if (playAll(position)) mediaPlayer.seekTo(0)
    }

    fun playPrior() {
        position--
        when (position) {
            -1 -> position = playList.size - 1
        }
        id = playList[position]
        if (playAll(position)) mediaPlayer.seekTo(0)
    }

    fun playingControl() {
        mediaPlayer.apply {
            if (isPlaying) {
                pause()
            } else {
                start()
            }
        }
    }

    fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }

    fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    fun getDuration(): Int {
        return mediaPlayer.duration
    }

    fun seekTo(position: Int) {
        mediaPlayer.seekTo(position)
    }

}