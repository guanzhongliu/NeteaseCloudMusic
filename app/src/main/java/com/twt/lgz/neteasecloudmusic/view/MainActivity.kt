package com.twt.lgz.neteasecloudmusic.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import cn.edu.twt.retrox.recyclerviewdsl.Item
import cn.edu.twt.retrox.recyclerviewdsl.ItemAdapter
import cn.edu.twt.retrox.recyclerviewdsl.ItemManager
import com.orhanobut.hawk.Hawk
import com.twt.lgz.neteasecloudmusic.R
import com.twt.lgz.neteasecloudmusic.model.Bean.MyPlaylistBean
import com.twt.lgz.neteasecloudmusic.model.PlaylistInfoItem
import com.twt.lgz.neteasecloudmusic.model.PlaylistItem
import com.twt.lgz.neteasecloudmusic.model.Status
import com.twt.lgz.neteasecloudmusic.netservice.NetService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.android.UI
import kotlinx.coroutines.launch
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var itemManager: ItemManager
    private lateinit var itemAdapter: ItemAdapter
    val list: MutableList<Item> = arrayListOf()

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        Hawk.init(this)
            .build()
        initView()
    }

    private fun initView() {
        sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE)
        val status = sharedPreferences.getInt("status", LOGOUT)
        if (status == LOGOUT) {
            startActivity<PrepareActivity>()
            finish()
        }
        NetService.getPlaylist(sharedPreferences.getInt("id", 0)) { status, data ->
            launch(UI) {
                when (status) {
                    Status.Success -> {
                        Hawk.put("playlist", data?.playlist)
                        updatePlaylist()
                    }
                    Status.unmatched -> Toast.makeText(
                        this@MainActivity,
                        "未获取歌单",
                        Toast.LENGTH_SHORT
                    ).show()
                    else -> Toast.makeText(
                        this@MainActivity,
                        "出现了问题T_T",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }


        info_button.setOnClickListener {
            NetService.logout()
            sharedPreferences.edit()
                .putInt("status", LOGOUT).apply()
            startActivity<PrepareActivity>()
            finish()
        }

    }

    fun updatePlaylist() {
        val data = Hawk.get<List<MyPlaylistBean.PlaylistBean>>("playlist")
        data?.forEach {
            val item = PlaylistItem(it.name, it.trackCount.toString() + "首")
            list.add(item)
        }
        playlist_rc.apply {
            itemManager = ItemManager(list)
            itemAdapter = ItemAdapter(itemManager)
            adapter = itemAdapter
            layoutManager = LinearLayoutManager(this.context)

        }
    }

    companion object {
        const val LOGIN = 1
        const val LOGOUT = 0
    }


}
