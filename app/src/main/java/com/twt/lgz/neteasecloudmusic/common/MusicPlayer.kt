package com.twt.lgz.neteasecloudmusic.common

import android.media.MediaPlayer

object MusicPlayer{
    val musicPlayer = MediaPlayer()
    var id: String = ""
    val isPlaying
        get() = musicPlayer.isPlaying

    fun play(url: String?, pid: String) {
        musicPlayer.stop()
        id = pid
        musicPlayer.reset()//重置播放器
        musicPlayer.setDataSource(url)
        musicPlayer.prepare()
        musicPlayer.start()
    }

    fun pause() {
        if (isPlaying) musicPlayer.pause()
    }

    fun stop() {
        if (isPlaying) musicPlayer.stop()
    }

    fun endPause() {
        if (!isPlaying) musicPlayer.start()
    }
}