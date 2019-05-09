package com.twt.lgz.neteasecloudmusic.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.widget.Toast
import com.twt.lgz.neteasecloudmusic.common.MusicPlayer
import javax.security.auth.callback.Callback

class MyService : Service() , MediaPlayer.OnPreparedListener{
    override fun onPrepared(mp: MediaPlayer?) {
        mediaPlayer.start()
    }

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

        return super.onStartCommand(intent, flags, startId)
    }

    private fun playByPosition(position: Int): Boolean {
        mediaPlayer.let {
            it.reset()
            try {
                it.setDataSource(playList[position])
                it.setOnPreparedListener{
                    it.start()
                }
                it.prepareAsync()

                return true
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return false
    }

    private fun play(url: String?): Boolean {
        MusicPlayer.id = url!!
        mediaPlayer.let {
            it.reset()
            try {
                it.setDataSource(url)
                it.setOnPreparedListener{
                    it.start()
                }
                it.prepareAsync()
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
        id = playList[position]
        if (playByPosition(position)) mediaPlayer.seekTo(0)
    }

    fun playPrior() {
        position--
        when (position) {
            -1 -> position = playList.size - 1
        }
        id = playList[position]
        if (playByPosition(position)) mediaPlayer.seekTo(0)
    }

    fun playingControl() {
        mediaPlayer.let {
            if (it.isPlaying) {
                it.pause()
            } else {
                it.start()
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