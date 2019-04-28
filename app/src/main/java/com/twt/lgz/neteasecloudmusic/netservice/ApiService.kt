package com.twt.lgz.neteasecloudmusic.netservice

import com.twt.lgz.neteasecloudmusic.model.Bean.LoginBean
import com.twt.lgz.neteasecloudmusic.model.MainModel
import com.twt.lgz.neteasecloudmusic.model.Bean.MyPlaylistBean
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/login/cellphone")
    fun login(@Query("phone") phoneNum: String, @Query("password") pwd: String): Call<LoginBean>

    @GET("/logout")
    fun logout(): Call<ResponseBody>

    @GET("/user/playlist")
    fun getPlaylist(@Query("uid") uid: Int): Call<MyPlaylistBean>

    companion object : ApiService by MainModel()
}