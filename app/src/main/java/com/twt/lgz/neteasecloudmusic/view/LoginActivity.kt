package com.twt.lgz.neteasecloudmusic.view

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.twt.lgz.neteasecloudmusic.R
import com.twt.lgz.neteasecloudmusic.model.Status
import com.twt.lgz.neteasecloudmusic.model.DataBase
import com.twt.lgz.neteasecloudmusic.service.NetService
import kotlinx.android.synthetic.main.layout_title.*
import kotlinx.android.synthetic.main.activity_loginmain.*
import kotlinx.coroutines.android.UI
import kotlinx.coroutines.launch
import org.jetbrains.anko.startActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_loginmain)
        back_button.setOnClickListener {
            finish()
        }
        login_btn_main.setOnClickListener {
            if (putPhone.text.toString() == "") {
                Toast.makeText(this, "请输入电话号码", Toast.LENGTH_SHORT).show()
            } else {
                NetService.login(putPhone.text.toString(), putPassword.text.toString()) { status, data ->
                    launch(UI) {
                        when (status) {
                            Status.Success -> {
                                Toast.makeText(
                                    this@LoginActivity,
                                    "${data!!.profile?.nickname}，登陆成功",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                val share = getSharedPreferences("data", Context.MODE_PRIVATE)
                                DataBase.dataUpdate(
                                    share,
                                    data,
                                    putPhone.text.toString(),
                                    putPassword.text.toString()
                                )
                                startActivity<MainActivity>()
                                finish()
                            }
                            Status.UNMATCHED -> Toast.makeText(
                                this@LoginActivity,
                                "密码错误",
                                Toast.LENGTH_SHORT
                            ).show()
                            else -> Toast.makeText(
                                this@LoginActivity,
                                "出现了问题T_T",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }
}
