package com.twt.lgz.neteasecloudmusic.netservice

import com.twt.lgz.neteasecloudmusic.model.Status
import com.twt.lgz.neteasecloudmusic.model.Bean.LoginBean
import com.twt.lgz.neteasecloudmusic.model.Bean.MyPlaylistBean
import kotlinx.coroutines.launch
import java.lang.Exception

object NetService {
    fun login(phone: String, password: String, logData: (Status, LoginBean?) -> (Unit)) {
        val call = ApiService.login(phone, password)
        launch {
            try {
                val loginDataBean = call.execute().body()
                if (loginDataBean == null)
                    logData(Status.unmatched, null)
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
                    playListData(Status.unmatched, null)
                else
                    playListData(Status.Success, myPlaylistBean)
            } catch (e: Exception) {
                playListData(Status.ERROR, null)
                e.printStackTrace()
            }
        }
    }

    //fun getListInfo(id: String?, pla)
}

