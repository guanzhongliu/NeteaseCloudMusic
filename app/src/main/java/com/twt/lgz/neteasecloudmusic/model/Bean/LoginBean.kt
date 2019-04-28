package com.twt.lgz.neteasecloudmusic.model.Bean

class LoginBean {
    var loginType: Int = 0
    var code: Int = 0
    var account: AccountBean? = null
    var profile: ProfileBean? = null
    var bindings: List<BindingsBean>? = null


    class AccountBean {

        var id: Int = 0
        var userName: String? = null
        var type: Int = 0
        var status: Int = 0
        var whitelistAuthority: Int = 0
        var createTime: Long = 0
        var salt: String? = null
        var tokenVersion: Int = 0
        var ban: Int = 0
        var baoyueVersion: Int = 0
        var donateVersion: Int = 0
        var vipType: Int = 0
        var viptypeVersion: String? = null
        var isAnonimousUser: Boolean = false
    }

    class ProfileBean {


        var detailDescription: String? = null
        var isFollowed: Boolean = false
        var avatarImgIdStr: String? = null
        var backgroundImgIdStr: String? = null
        var accountStatus: Int = 0
        var vipType: Int = 0
        var province: Int = 0
        var birthday: Long = 0
        var nickname: String? = null
        var djStatus: Int = 0
        var isDefaultAvatar: Boolean = false
        var avatarUrl: String? = null
        var avatarImgId: Long = 0
        var backgroundUrl: String? = null
        var userId: Int = 0
        var gender: Int = 0
        var experts: ExpertsBean? = null
        var backgroundImgId: Long = 0
        var userType: Int = 0
        var city: Int = 0
        var isMutual: Boolean = false
        var remarkName: Any? = null
        var expertTags: Any? = null
        var authStatus: Int = 0
        var description: String? = null
        var signature: String? = null
        var authority: Int = 0
        var avatarImgId_str: String? = null
        var followeds: Int = 0
        var follows: Int = 0
        var eventCount: Int = 0
        var playlistCount: Int = 0
        var playlistBeSubscribedCount: Int = 0

        class ExpertsBean
    }

    class BindingsBean {
        var refreshTime: Int = 0
        var tokenJsonStr: String? = null
        var userId: Int = 0
        var expiresIn: Int = 0
        var url: String? = null
        var isExpired: Boolean = false
        var id: Long = 0
        var type: Int = 0
    }
}
