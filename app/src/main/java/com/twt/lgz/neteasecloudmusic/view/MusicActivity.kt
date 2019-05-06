package com.twt.lgz.neteasecloudmusic.view

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.widget.*
import com.bumptech.glide.Glide
import com.orhanobut.hawk.Hawk
import com.twt.lgz.neteasecloudmusic.R
import com.twt.lgz.neteasecloudmusic.model.Status
import com.twt.lgz.neteasecloudmusic.service.MyService
import com.twt.lgz.neteasecloudmusic.service.NetService
import kotlinx.android.synthetic.main.activity_music.*
import kotlinx.coroutines.android.UI
import kotlinx.coroutines.launch

class MusicActivity : AppCompatActivity() {

    internal var id: String? = null
    internal var name: String? = null
    private var artist: String? = null
    private lateinit var currentTime: TextView
    private var myService: MyService? = null
    private var picUrl: String? = ""
    private var sCnn: ServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            myService = (service as MyService.MyBinder).getService()
            handler.post(runnable_play)
            //handler.post(runnable_seekbar)
        }
    }
    val handler = Handler()
    val runnable_play = Runnable {
        myService?.let {
            total_time.text = convertToTime(it.getDuration())
            current_time.text = convertToTime(it.getCurrentPosition())
            seek_bar.progress = it.getCurrentPosition()
            seek_bar.max = it.getDuration()
        }
    }
//    val runnable_seekbar = object : Runnable {
//        override fun run() {
//            myService?.let {
//                currentTime.text = convertToTime(it.getCurrentPosition())
//                seek_bar.progress = it.getCurrentPosition()
//            }
//            handler.postDelayed(this, 1L)
//        }
//    }


    /***
     * onCreate()
     */
    @SuppressLint("ShowToast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music)
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
        getURL(id)


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
                        setOnClick()
                        playNow()
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

        NetService.getMusicDetail(id!!) { status, data ->
            launch(UI) {
                when (status) {
                    Status.Success -> {
                        Hawk.put("musicpic$id", data?.songs?.get(0)?.al?.picUrl)
                        val pic = Hawk.get("musicpic$id", "")
                        Glide.with(this@MusicActivity)
                            .load(pic)
                            .into(rotateView)
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

    }

    fun convertToTime(number: Int): String {
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
//        seek_bar.setOnSeekBarChangeListener(
//            object : SeekBar.OnSeekBarChangeListener {
//                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
//                    if (fromUser) {
//                        seekBar?.let {
//                            myService?.seekTo(it.progress)
//                            currentTime.text = convertToTime(progress)
//                        }
//                    }
//
//                }
//
//                override fun onStartTrackingTouch(seekBar: SeekBar?) {
//                }
//
//                override fun onStopTrackingTouch(seekBar: SeekBar?) {
//                }
//            })
    }


}
