package com.twt.lgz.neteasecloudmusic.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.twt.lgz.neteasecloudmusic.R
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.startActivity


@SuppressLint("Registered")
class PrepareActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_login)
        login_button.setOnClickListener {
            startActivity<LoginActivity>()
        }
    }
}