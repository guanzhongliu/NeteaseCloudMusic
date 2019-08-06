package com.twt.lgz.neteasecloudmusic.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import cn.edu.twt.retrox.recyclerviewdsl.Item
import cn.edu.twt.retrox.recyclerviewdsl.ItemAdapter
import cn.edu.twt.retrox.recyclerviewdsl.ItemManager
import com.orhanobut.hawk.Hawk
import com.twt.lgz.neteasecloudmusic.R
import com.twt.lgz.neteasecloudmusic.model.bean.ListInfoBean
import com.twt.lgz.neteasecloudmusic.model.Status
import com.twt.lgz.neteasecloudmusic.service.NetService
import kotlinx.android.synthetic.main.activity_playlist.*
import kotlinx.coroutines.android.UI
import kotlinx.coroutines.launch

class PlaylistActivity : AppCompatActivity() {

    internal var id: String? = null
    private lateinit var itemManager: ItemManager
    private lateinit var itemAdapter: ItemAdapter
    private val list: MutableList<Item> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)
        supportActionBar?.hide()
        val i = intent
        val b = i.extras
        if (b != null) {
            id = b.getString("id")
        }
        initView()

    }

    private fun initView() {
        NetService.getListInfo(id) { status, data ->
            launch(UI) {
                when (status) {
                    Status.Success -> {
                        Hawk.put("listinfo$id", data?.playlist)
                        updatePlaylist()
                    }
                    Status.UNMATCHED -> Toast.makeText(
                        this@PlaylistActivity,
                        "未获取歌单详情",
                        Toast.LENGTH_SHORT
                    ).show()
                    else -> Toast.makeText(
                        this@PlaylistActivity,
                        "出现了问题T_T  $id",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

        }

    }

    private fun updatePlaylist() {
        val data = Hawk.get<ListInfoBean.PlaylistBean?>("listinfo$id")?.tracks
        var i = 0
        data?.forEach {
            val item = SongInfoItem(
                it.name,
                it.ar!![0].name,
                it.al!!.name,
                "",
                ++i,
                it.id
            )
            list.add(item)
        }
        playlist_info_rc.apply {
            itemManager = ItemManager(list)
            itemAdapter = ItemAdapter(itemManager)
            adapter = itemAdapter
            layoutManager = LinearLayoutManager(this.context)
        }
    }
}
