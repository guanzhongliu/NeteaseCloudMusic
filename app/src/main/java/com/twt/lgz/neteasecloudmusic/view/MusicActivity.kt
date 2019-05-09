package com.twt.lgz.neteasecloudmusic.view

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.orhanobut.hawk.Hawk
import com.twt.lgz.neteasecloudmusic.model.Status
import com.twt.lgz.neteasecloudmusic.service.MyService
import com.twt.lgz.neteasecloudmusic.service.NetService
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_music.*
import kotlinx.coroutines.android.UI
import kotlinx.coroutines.launch
import com.twt.lgz.neteasecloudmusic.R
import jp.wasabeef.glide.transformations.ColorFilterTransformation


class MusicActivity : AppCompatActivity() {

    internal var id: String? = null
    internal var name: String? = null
    private var artist: String? = null
    private var myService: MyService? = null

    val handler = Handler()
    private var sCnn: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            myService = (service as MyService.MyBinder).getService()
            handler.post(runnable_play)
        }

        override fun onServiceDisconnected(name: ComponentName?) {

        }
    }

    val runnable_play = Runnable {
        myService?.let {
            total_time.text = convertToTime(it.getDuration())
            current_time.text = convertToTime(it.getCurrentPosition())
            seek_bar.progress = it.getCurrentPosition()
            seek_bar.max = it.getDuration()
        }
    }
    val runnable_seekbar = object : Runnable {
        override fun run() {
            myService?.let {
                current_time.text = convertToTime(it.getCurrentPosition())
                seek_bar.progress = it.getCurrentPosition()
            }
            handler.postDelayed(this, 1L)
        }
    }


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
        music_name.text = name
        music_artist.text = artist
        current_time.text = "0:00"

        getURL(id)
    }

    override fun onResume() {
        super.onResume()
        handler.post(runnable_seekbar)

    }

    @SuppressLint("ShowToast")
    private fun playNow() {
        val sIntent = Intent(this, MyService::class.java)
        val url = Hawk.get("musicurl$id", "")
        if (url != "") {
            sIntent.putExtra("url", url)
            startService(sIntent)
            bindService(sIntent, sCnn, BIND_AUTO_CREATE)
            rotateView.play()
        } else {
            Toast.makeText(this, "获取歌曲url过程中出现了问题", Toast.LENGTH_SHORT)
        }
    }


    private fun getURL(id: String?) {
        if (Hawk.get("musicurl$id", "") == "") {
            NetService.getMusicURL(id) { status, data ->
                launch(UI) {
                    when (status) {
                        Status.Success -> {
                            Hawk.put("musicurl$id", data?.data?.get(0)?.url)
                            Toast.makeText(
                                this@MusicActivity,
                                "成功获取歌曲",
                                Toast.LENGTH_SHORT
                            ).show()
                            playNow()
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
            playNow()
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

    private fun convertToTime(number: Int): String {
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

    private fun doGlide(pic :String){
        Glide.with(this@MusicActivity)
            .load(pic)
            .into(rotateView)
        val multi = MultiTransformation(
            BlurTransformation(30, 25),
            ColorFilterTransformation(0x555555)
        )
        Glide.with(this@MusicActivity)
            .load(pic)
            .transform(multi)
            .into(music_background)
    }

    private fun setOnClick() {
        pause_play.setOnClickListener {
            myService?.let {
                if (it.isPlaying()) {
                    pause_play.setBackgroundResource(R.drawable.play)
                    rotateView.pause()
                } else {
                    pause_play.setBackgroundResource(R.drawable.pause)
                    rotateView.play()
                }
                it.playingControl()
            }
        }
        seek_bar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        seekBar?.let {
                            myService?.seekTo(it.progress)
                            current_time.text = convertToTime(progress)
                        }
                    }

                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }
            })
    }


}
