package com.twt.lgz.neteasecloudmusic.view

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.orhanobut.hawk.Hawk
import com.twt.lgz.neteasecloudmusic.R
import com.twt.lgz.neteasecloudmusic.model.Status
import com.twt.lgz.neteasecloudmusic.netservice.NetService
import kotlinx.coroutines.android.UI
import kotlinx.coroutines.launch

class MusicActivity : AppCompatActivity() {

    internal var id: String? = null

    @SuppressLint("ShowToast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music)
        supportActionBar?.hide()
        val i = intent
        val b = i.extras
        if (b != null) {
            id = b.getString("id")
        }
        getURL(id)

    }

    private fun getURL(id: String?){

        NetService.getMusicURL(id) { status, data ->
            launch(UI) {
                when (status) {
                    com.twt.lgz.neteasecloudmusic.model.Status.Success -> {
                        Hawk.put("musicinfo$id", data?.data)
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
}
