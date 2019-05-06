package com.twt.lgz.neteasecloudmusic.service

import com.twt.lgz.neteasecloudmusic.model.bean.*
import com.twt.lgz.neteasecloudmusic.model.MainModel
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
    fun getPlaylist(@Query("uid") uid: String?): Call<MyPlaylistBean>

    @GET("/playlist/detail")
    fun getListInfo(@Query("id") id: String?): Call<ListInfoBean>

    @GET("/song/url")
    fun getMusicURL(@Query("id") id: String?): Call<MusicBean>

    @GET("/check/music")
    fun checkMusic(@Query("id") id: String?): Call<CheckBean>

    @GET("/song/detail?")
    fun getMusicDetail(@Query("ids") ids: String?): Call<MusicDetailBean>


    companion object : ApiService by MainModel()
}