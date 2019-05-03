package com.twt.lgz.neteasecloudmusic.view

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.widget.*
import com.orhanobut.hawk.Hawk
import com.twt.lgz.neteasecloudmusic.R
import com.twt.lgz.neteasecloudmusic.service.MyService
import com.twt.lgz.neteasecloudmusic.service.NetService
import kotlinx.android.synthetic.main.activity_music.*
import kotlinx.coroutines.android.UI
import kotlinx.coroutines.launch

class MusicActivity : AppCompatActivity() {

    internal var id: String? = null
    internal var name: String? = null
    private var artist: String? = null
    private lateinit var priorButton: Button
    private lateinit var nextButton: Button
    private lateinit var pauseOrPlayButton: Button
    private lateinit var image: ImageView
    private lateinit var seekBar: SeekBar
    private lateinit var totalTime: TextView
    private lateinit var currentTime: TextView
    val musicList = ArrayList<String>()
    private var animator: ObjectAnimator? = null
    private var myService: MyService? = null
    private var picUrl: String? = ""
    private var sCnn: ServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            myService = (service as MyService.MyBinder).getService()
            if (musicList.isEmpty()) {
                myService = null
            }
            handler.post(runnable)
        }
    }
    val handler = Handler()
    val runnable = Runnable {
        Log.d("runnable!", "run!")
        myService?.let {
            totalTime.text = convertToTime(it.getDuration())
            currentTime.text = convertToTime(it.getCurrentPosition())
            seekBar.progress = it.getCurrentPosition()
            seekBar.max = it.getDuration()
        }
    }
    val runnable2 = object : Runnable {
        //seekBar 自动移动。当前时间改变
        override fun run() {
            myService?.let {
                currentTime.text = convertToTime(it.getCurrentPosition())
                seekBar.progress = it.getCurrentPosition()
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
        initView()
        setClick()
        getURL(id)
        playNow()

    }

    @SuppressLint("ShowToast")
    private fun playNow() {
        val sIntent = Intent(this, MyService::class.java)
        val url = Hawk.get("musicinfo$id", "")
        if (url != "") {
            sIntent.putExtra("url", url)
            startService(sIntent)
            bindService(sIntent, sCnn, BIND_AUTO_CREATE)
        } else {
            Toast.makeText(this, "获取歌曲url过程中出现了问题", Toast.LENGTH_SHORT)
        }
    }

    private fun initView() {
        priorButton = findViewById(R.id.playing_prior)
        nextButton = findViewById(R.id.playing_next)
        pauseOrPlayButton = findViewById(R.id.pause_play)
        seekBar = findViewById(R.id.seekbar)
        totalTime = findViewById(R.id.total_time)
        currentTime = findViewById(R.id.current_time)
    }


    private fun getURL(id: String?) {

        NetService.getMusicURL(id) { status, data ->
            launch(UI) {
                when (status) {
                    com.twt.lgz.neteasecloudmusic.model.Status.Success -> {
                        Hawk.put("musicinfo$id", data?.data?.get(0)?.url)
                        Toast.makeText(
                            this@MusicActivity,
                            "成功获取歌曲",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    com.twt.lgz.neteasecloudmusic.model.Status.UNMATCHED -> Toast.makeText(
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

    private fun setClick() {
        pauseOrPlayButton.setOnClickListener {
            myService?.let {
                if (it.isPlaying()) {
                    pauseOrPlayButton.setBackgroundResource(R.drawable.play)
                    animator?.pause()
                } else {
                    pauseOrPlayButton.setBackgroundResource(R.drawable.pause)
                    animator?.resume()
                }
                it.pauseOrPlay()
            }
        }
        seekBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        seekBar?.let {
                            myService?.seekTo(it.progress)
                            currentTime.text = convertToTime(progress)
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
