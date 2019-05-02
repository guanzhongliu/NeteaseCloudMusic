package com.twt.lgz.neteasecloudmusic.model


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import cn.edu.twt.retrox.recyclerviewdsl.Item
import cn.edu.twt.retrox.recyclerviewdsl.ItemController
import com.bumptech.glide.Glide
import com.orhanobut.hawk.Hawk
import com.twt.lgz.neteasecloudmusic.R
import com.twt.lgz.neteasecloudmusic.netservice.NetService
import com.twt.lgz.neteasecloudmusic.view.LoginActivity
import com.twt.lgz.neteasecloudmusic.view.MusicActivity
import com.twt.lgz.neteasecloudmusic.view.PlaylistActivity
import kotlinx.android.synthetic.main.local_service.view.*
import kotlinx.android.synthetic.main.playlist_info.view.*
import kotlinx.android.synthetic.main.song_info.view.*
import kotlinx.coroutines.android.UI
import kotlinx.coroutines.launch
import org.jetbrains.anko.image
import org.jetbrains.anko.layoutInflater
import org.jetbrains.anko.textColor


class LocalListItem(val text: String?, val num: String?, val status: String) : Item {

    companion object : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val inflater = parent.context.layoutInflater
            val view = inflater.inflate(R.layout.local_service, parent, false)
            return ViewHolder(
                view,
                view.local_icon,
                view.title,
                view.count,
                view.local_speaker
            )
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ViewHolder
            item as LocalListItem
            holder.title.text = item.text
            holder.num.text = item.num
            when (holder.title.text) {
                "本地音乐" -> {
                    Glide.with(holder.view)
                        .load(R.drawable.music_icn_local)
                        .into(holder.imageView)
                    holder.isOn.visibility = View.VISIBLE
                }
                "下载管理" -> Glide.with(holder.view)
                    .load(R.drawable.music_icn_dld)
                    .into(holder.imageView)
                "我的电台" -> Glide.with(holder.view)
                    .load(R.drawable.music_icn_dj)
                    .into(holder.imageView)
                "我的收藏" -> Glide.with(holder.view)
                    .load(R.drawable.music_icn_artist)
                    .into(holder.imageView)
            }

            holder.view.setOnClickListener {
                //启动
            }

        }
    }

    class ViewHolder(
        val view: View,
        val imageView: ImageView,
        val title: TextView,
        val num: TextView,
        val isOn: ImageView
    ) :
        RecyclerView.ViewHolder(view)

    override val controller = Companion
}

class PlaylistInfoItem(
    val name: String?,
    val artist: String?,
    val album: String?,
    val mv: String?,
    val pos: Int,
    val id: String?
) : Item {

    companion object : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val inflater = parent.context.layoutInflater
            val view = inflater.inflate(R.layout.song_info, parent, false)
            return ViewHolder(
                view,
                view.mv,
                view.index,
                view.song_name,
                view.artist,
                view.album,
                view.in_word,
                view.more,
                view.playing
            )
        }

        @SuppressLint("ShowToast")
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ViewHolder
            item as PlaylistInfoItem
            holder.name.text = item.name
            holder.artist.text = item.artist
            holder.album.text = item.album
            holder.textView.text = " - "
            holder.index.text = item.pos.toString()

            holder.view.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("id", item.id)
                val intent = Intent(holder.view.context, MusicActivity::class.java)
                intent.putExtras(bundle)
                if (holder.index.visibility == View.VISIBLE) {
                    NetService.checkMusic(item.id!!) { status, data ->
                        launch(UI) {
                            if (data?.message == "ok") {
                                holder.index.visibility = View.GONE
                                holder.playing.visibility = View.VISIBLE
                                holder.view.context.startActivity(intent)
                            } else {
                                holder.name.textColor = Color.GRAY
                                holder.album.textColor = Color.GRAY
                                holder.artist.textColor = Color.GRAY
                                holder.index.textColor = Color.GRAY
                                holder.textView.textColor = Color.GRAY
                                holder.iv_mv.visibility = View.GONE
                            }
                        }
                    }

                } else {
                    holder.index.visibility = View.VISIBLE
                    holder.playing.visibility = View.GONE
                }

            }

            holder.view.more.setOnClickListener {
                //popup_window
            }

        }
    }

    class ViewHolder(
        val view: View,
        val iv_mv: ImageView,
        val index: TextView,
        val name: TextView,
        val artist: TextView,
        val album: TextView,
        val textView: TextView,
        val more: ImageView,
        val playing: ImageView
    ) :
        RecyclerView.ViewHolder(view)

    override val controller = Companion


}

class PlaylistItem(val text: String?, val num: String?, val iv: String?, val id: String?) : Item {

    companion object : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val inflater = parent.context.layoutInflater
            val view = inflater.inflate(R.layout.playlist_info, parent, false)
            return ViewHolder(
                view,
                view.iv_playlist,
                view.playlist_name,
                view.song_num,
                view.popup_menu
            )
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ViewHolder
            item as PlaylistItem
            holder.name.text = item.text
            holder.num.text = item.num

            Glide.with(holder.view)
                .load(item.iv)
                .into(holder.imageView)

            holder.view.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("id", item.id)
                val intent = Intent(holder.view.context, PlaylistActivity::class.java)
                intent.putExtras(bundle)
                holder.view.context.startActivity(intent)
            }
            holder.pop.setOnClickListener {
                //popup_window
            }
        }
    }

    class ViewHolder(
        val view: View,
        val imageView: ImageView,
        val name: TextView,
        val num: TextView,
        val pop: ImageView
    ) :
        RecyclerView.ViewHolder(view)

    override val controller = Companion
}

