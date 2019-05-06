package com.twt.lgz.neteasecloudmusic.service

import com.twt.lgz.neteasecloudmusic.model.bean.*
import com.twt.lgz.neteasecloudmusic.model.Status
import kotlinx.coroutines.launch
import java.lang.Exception

object NetService {
    fun login(phone: String, password: String, logData: (Status, LoginBean?) -> (Unit)) {
        val call = ApiService.login(phone, password)
        launch {
            try {
                val loginDataBean = call.execute().body()
                if (loginDataBean == null)
                    logData(Status.UNMATCHED, null)
                else
                    logData(Status.Success, loginDataBean)
            } catch (e: Exception) {
                logData(Status.ERROR, null)
                e.printStackTrace()
            }
        }
    }

    fun logout() {
        val call = ApiService.logout()
        launch {
            call.execute().body()
        }
    }

    fun getPlaylist(uid: String?, playListData: (Status, MyPlaylistBean?) -> (Unit)) {
        val call = ApiService.getPlaylist(uid)
        launch {
            try {
                val myPlaylistBean = call.execute().body()
                if (myPlaylistBean == null)
                    playListData(Status.UNMATCHED, null)
                else
                    playListData(Status.Success, myPlaylistBean)
            } catch (e: Exception) {
                playListData(Status.ERROR, null)
                e.printStackTrace()
            }
        }
    }

    fun getListInfo(id: String?, ListData: (Status, ListInfoBean?) -> (Unit)) {
        val call = ApiService.getListInfo(id)
        launch {
            try {
                val listInfoBean = call.execute().body()
                if (listInfoBean == null)
                    ListData(Status.UNMATCHED, null)
                else
                    ListData(Status.Success, listInfoBean)
            } catch (e: Exception) {
                ListData(Status.ERROR, null)
                e.printStackTrace()
            }
        }
    }

    fun getMusicURL(id: String?, MusicData: (Status, MusicBean?) -> (Unit)) {
        val call = ApiService.getMusicURL(id)
        launch {
            try {
                val musicBean = call.execute().body()
                if (musicBean == null)
                    MusicData(Status.UNMATCHED, null)
                else
                    MusicData(Status.Success, musicBean)
            } catch (e: Exception) {
                MusicData(Status.ERROR, null)
                e.printStackTrace()
            }
        }
    }

    fun checkMusic(id: String, checkData: (Status, CheckBean?) -> (Unit)) {
        val call = ApiService.checkMusic(id)
        launch {
            try {
                val checkBean = call.execute().body()
                if (checkBean == null)
                    checkData(Status.UNMATCHED, null)
                else
                    checkData(Status.Success, checkBean)
            } catch (e: Exception) {
                checkData(Status.ERROR, null)
                e.printStackTrace()
            }
        }
    }

    fun getMusicDetail(ids: String, checkData: (Status, MusicDetailBean?) -> (Unit)) {
        val call = ApiService.getMusicDetail(ids)
        launch {
            try {
                val checkBean = call.execute().body()
                if (checkBean == null)
                    checkData(Status.UNMATCHED, null)
                else
                    checkData(Status.Success, checkBean)
            } catch (e: Exception) {
                checkData(Status.ERROR, null)
                e.printStackTrace()
            }
        }
    }
}

