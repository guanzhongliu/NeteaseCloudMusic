package com.twt.lgz.neteasecloudmusic.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.twt.lgz.neteasecloudmusic.R

class PlaylistActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)
        supportActionBar?.hide()


    }
}
