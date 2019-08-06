package com.twt.lgz.neteasecloudmusic.common

import android.media.MediaPlayer

object MusicPlayer : MediaPlayer.OnPreparedListener{
    override fun onPrepared(mp: MediaPlayer?) {
        musicPlayer.start()
    }

    val musicPlayer = MediaPlayer()
    var id: String = ""

    val isPlaying
        get() = musicPlayer.isPlaying

    fun play(url: String?, pid: String) {
        musicPlayer.stop()
        id = pid
        musicPlayer.reset()
        musicPlayer.setDataSource(url)
        musicPlayer.prepareAsync()

//        musicPlayer.prepare()
//        musicPlayer.start()
    }

    fun pause() {
        if (isPlaying) musicPlayer.pause()
    }


    fun endPause() {
        if (!isPlaying) musicPlayer.start()
    }

    fun stop() {
        if (isPlaying) musicPlayer.stop()
    }
}