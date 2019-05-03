package com.twt.lgz.neteasecloudmusic.model

import android.content.SharedPreferences
import com.twt.lgz.neteasecloudmusic.model.bean.LoginBean

object DataBase {
    fun dataUpdate(share: SharedPreferences, loginBean: LoginBean, phone: String, pwd: String) {
        share.edit()
            .putInt("status", 1)
            .putString("avatar_url", loginBean.profile?.avatarUrl)
            .putString("nickname", loginBean.profile?.nickname)
            .putInt("gender", loginBean.profile?.gender ?: 0)
            .putString("phoneNum", phone)
            .putString("pwd", pwd)
            .putString("id", loginBean.account?.id ?: "0")
            .putString("background_url", loginBean.profile?.backgroundUrl).apply()
    }

}