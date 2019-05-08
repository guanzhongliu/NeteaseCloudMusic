package com.twt.lgz.neteasecloudmusic.model

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object MainModel {
    private const val BASE_URL = "http://192.168.137.1:3000"
    private val client = OkHttpClient
        .Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .build()
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    inline operator fun <reified T> invoke(): T = retrofit.create(T::class.java)
}