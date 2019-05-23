package com.twt.lgz.neteasecloudmusic.view

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.graphics.PorterDuff
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.orhanobut.hawk.Hawk
import com.twt.lgz.neteasecloudmusic.model.Status
import com.twt.lgz.neteasecloudmusic.common.MyService
import com.twt.lgz.neteasecloudmusic.service.NetService
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_music.*
import kotlinx.coroutines.android.UI
import kotlinx.coroutines.launch
import com.twt.lgz.neteasecloudmusic.R
import com.twt.lgz.neteasecloudmusic.common.MusicPlayer
import jp.wasabeef.glide.transformations.ColorFilterTransformation


class MusicActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener {

    internal var id: String? = null
    internal var name: String? = null
    private var artist: String? = null
    private var handler: Handler? = null
    private val musicPlayer = MusicPlayer.musicPlayer
    private var threadState: Boolean = false

    /***
     * onCreate()
     */
    @SuppressLint("ShowToast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = Color.TRANSPARENT
        supportActionBar?.hide()
        val i = intent
        val b = i.extras
        if (b != null) {
            id = b.getString("id")
            name = b.getString("name")
            artist = b.getString("artist")
        }
        initView()
        getURL(id)
    }

    @SuppressLint("SetTextI18n")
    private val runnable = Runnable {
        while (true) {//反复更新
            seek_bar.progress = (musicPlayer.currentPosition * 100f / musicPlayer.duration).toInt()
            if (threadState) break
            runOnUiThread {
                current_time.text = cvt2Time(musicPlayer.currentPosition)
                total_time.text = cvt2Time(musicPlayer.duration)
            }
            Thread.sleep(100)
        }
    }

    private fun addSeekerThread(){
        val handlerThread = HandlerThread("SeekerThread")
        handlerThread.start()
        handler = Handler(handlerThread.looper)
        handler!!.post(runnable)
    }

    override fun onDestroy() {
        threadState = true//修改flag指标
        if (handler != null) handler!!.removeCallbacks(runnable)
        super.onDestroy()
    }


    @SuppressLint("ShowToast")
    private fun playMusic() {

        val url = Hawk.get("musicurl$id", "")
        if (url != "") {
            if (MusicPlayer.id != id) {
                MusicPlayer.play(url, id!!)
                rotateView.start()
            } else {
                if (!MusicPlayer.isPlaying) {
                    pause_play.setBackgroundResource(R.drawable.play)
                } else rotateView.start()
            }


        } else {
            Toast.makeText(this, "获取歌曲url过程中出现了问题", Toast.LENGTH_SHORT)
        }
        addSeekerThread()
    }

    private fun initView() {
        music_name.text = name
        music_artist.text = artist
        current_time.text = "0:00"
        seek_bar.thumb.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP)
        seek_bar.progressDrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
    }

    private fun getURL(id: String?) {
        if (Hawk.get("musicurl$id", "") == "") {
            NetService.getMusicURL(id) { status, data ->
                launch(UI) {
                    when (status) {
                        Status.Success -> {
                            Hawk.put("musicurl$id", data?.data?.get(0)?.url)
                            playMusic()
                            setOnClick()
                        }
                        Status.UNMATCHED -> Toast.makeText(
                            this@MusicActivity,
                            "未获取歌曲详情",
                            Toast.LENGTH_SHORT
                        ).show()
                        else -> Toast.makeText(
                            this@MusicActivity,
                            "出现了问题T_T  $id",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        } else {
            playMusic()
            setOnClick()
        }

        if (Hawk.get("musicpic$id", "") == "") {
            NetService.getMusicDetail(id!!) { status, data ->
                launch(UI) {
                    when (status) {
                        Status.Success -> {
                            Hawk.put("musicpic$id", data?.songs?.get(0)?.al?.picUrl)
                            val pic = Hawk.get("musicpic$id", "")
                            doGlide(pic)

                        }
                        Status.UNMATCHED -> Toast.makeText(
                            this@MusicActivity,
                            "未获取歌曲图片",
                            Toast.LENGTH_SHORT
                        ).show()
                        else -> Toast.makeText(
                            this@MusicActivity,
                            "图片出现了问题T_T  $id",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

            }
        } else {
            doGlide(Hawk.get("musicpic$id", ""))
        }


    }

    private fun cvt2Time(number: Int): String {
        val time = number / 1000
        val minute = time / 60
        val second = time - minute * 60
        var totalTime = ""
        if (minute / 10 == 0) {//minute 个位
            totalTime += "0"
        }
        totalTime += "$minute:"
        if (second / 10 == 0) {
            totalTime += "0"
        }
        totalTime += "$second"
        return totalTime
    }

    private fun doGlide(pic: String) {
        Glide.with(this@MusicActivity)
            .load(pic)
            .into(rotateView)
        val multi = MultiTransformation(
            ColorFilterTransformation(0x3F3F3F),
            BlurTransformation(30, 20)
        )
        Glide.with(this@MusicActivity)
            .load(pic)
            .transform(multi)
            .into(music_background)
    }

    private fun setOnClick() {
        pause_play.setOnClickListener {
            if (MusicPlayer.isPlaying) {
                MusicPlayer.pause()
                pause_play.setBackgroundResource(R.drawable.play)
                rotateView.pause()
            } else {
                MusicPlayer.endPause()
                pause_play.setBackgroundResource(R.drawable.pause)
                rotateView.start()
            }
        }
        seek_bar.setOnSeekBarChangeListener(this)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if (fromUser) musicPlayer
            .seekTo(
                (progress.toDouble() / 100f * musicPlayer.duration)
                    .toInt()
            )
    }

}
