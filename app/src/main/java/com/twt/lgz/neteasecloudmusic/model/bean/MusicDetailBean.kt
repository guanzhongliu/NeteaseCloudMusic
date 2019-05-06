package com.twt.lgz.neteasecloudmusic.model.bean

class MusicDetailBean {

    /**
     * songs : [{"name":"海阔天空","id":347230,"pst":0,"t":0,"ar":[{"id":11127,"name":"Beyond","tns":[],"alias":[]}],"alia":[],"pop":100,"st":0,"rt":"600902000004240302","fee":8,"v":31,"crbt":null,"cf":"","al":{"id":34209,"name":"海阔天空","picUrl":"https://p1.music.126.net/QHw-RuMwfQkmgtiyRpGs0Q==/102254581395219.jpg","tns":[],"pic":102254581395219},"dt":326348,"h":{"br":320000,"fid":0,"size":13070578,"vd":0.109906},"m":{"br":160000,"fid":0,"size":6549371,"vd":0.272218},"l":{"br":96000,"fid":0,"size":3940469,"vd":0.228837},"a":null,"cd":"1","no":1,"rtUrl":null,"ftype":0,"rtUrls":[],"djId":0,"copyright":1,"s_id":0,"mv":376199,"rtype":0,"rurl":null,"mst":9,"cp":7002,"publishTime":746812800000}]
     * privileges : [{"id":347230,"fee":8,"payed":0,"st":0,"pl":128000,"dl":0,"sp":7,"cp":1,"subp":1,"cs":false,"maxbr":999000,"fl":128000,"toast":false,"flag":0,"preSell":false}]
     * code : 200
     */

    var code: Int = 0
    var songs: List<SongsBean>? = null
    var privileges: List<PrivilegesBean>? = null

    class SongsBean {
        /**
         * name : 海阔天空
         * id : 347230
         * pst : 0
         * t : 0
         * ar : [{"id":11127,"name":"Beyond","tns":[],"alias":[]}]
         * alia : []
         * pop : 100
         * st : 0
         * rt : 600902000004240302
         * fee : 8
         * v : 31
         * crbt : null
         * cf :
         * al : {"id":34209,"name":"海阔天空","picUrl":"https://p1.music.126.net/QHw-RuMwfQkmgtiyRpGs0Q==/102254581395219.jpg","tns":[],"pic":102254581395219}
         * dt : 326348
         * h : {"br":320000,"fid":0,"size":13070578,"vd":0.109906}
         * m : {"br":160000,"fid":0,"size":6549371,"vd":0.272218}
         * l : {"br":96000,"fid":0,"size":3940469,"vd":0.228837}
         * a : null
         * cd : 1
         * no : 1
         * rtUrl : null
         * ftype : 0
         * rtUrls : []
         * djId : 0
         * copyright : 1
         * s_id : 0
         * mv : 376199
         * rtype : 0
         * rurl : null
         * mst : 9
         * cp : 7002
         * publishTime : 746812800000
         */

        var name: String? = null
        var id: String? = null
        var pst: Int = 0
        var t: Int = 0
        var pop: Int = 0
        var st: Int = 0
        var rt: String? = null
        var fee: Int = 0
        var v: Int = 0
        var crbt: Any? = null
        var cf: String? = null
        var al: AlBean? = null
        var dt: Int = 0
        var h: HBean? = null
        var m: MBean? = null
        var l: LBean? = null
        var a: Any? = null
        var cd: String? = null
        var no: Int = 0
        var rtUrl: Any? = null
        var ftype: Int = 0
        var djId: Int = 0
        var copyright: Int = 0
        var s_id: Int = 0
        var mv: Int = 0
        var rtype: Int = 0
        var rurl: Any? = null
        var mst: Int = 0
        var cp: Int = 0
        var publishTime: Long = 0
        var ar: List<ArBean>? = null
        var alia: List<*>? = null
        var rtUrls: List<*>? = null

        class AlBean {
            /**
             * id : 34209
             * name : 海阔天空
             * picUrl : https://p1.music.126.net/QHw-RuMwfQkmgtiyRpGs0Q==/102254581395219.jpg
             * tns : []
             * pic : 102254581395219
             */

            var id: String? = null
            var name: String? = null
            var picUrl: String? = null
            var pic: Long = 0
            var tns: List<*>? = null
        }

        class HBean {
            /**
             * br : 320000
             * fid : 0
             * size : 13070578
             * vd : 0.109906
             */

            var br: Int = 0
            var fid: Int = 0
            var size: Int = 0
            var vd: Double = 0.toDouble()
        }

        class MBean {
            /**
             * br : 160000
             * fid : 0
             * size : 6549371
             * vd : 0.272218
             */

            var br: Int = 0
            var fid: Int = 0
            var size: Int = 0
            var vd: Double = 0.toDouble()
        }

        class LBean {
            /**
             * br : 96000
             * fid : 0
             * size : 3940469
             * vd : 0.228837
             */

            var br: Int = 0
            var fid: Int = 0
            var size: Int = 0
            var vd: Double = 0.toDouble()
        }

        class ArBean {
            /**
             * id : 11127
             * name : Beyond
             * tns : []
             * alias : []
             */

            var id: String? = null
            var name: String? = null
            var tns: List<*>? = null
            var alias: List<*>? = null
        }
    }

    class PrivilegesBean {
        /**
         * id : 347230
         * fee : 8
         * payed : 0
         * st : 0
         * pl : 128000
         * dl : 0
         * sp : 7
         * cp : 1
         * subp : 1
         * cs : false
         * maxbr : 999000
         * fl : 128000
         * toast : false
         * flag : 0
         * preSell : false
         */

        var id: String? = null
        var fee: Int = 0
        var payed: Int = 0
        var st: Int = 0
        var pl: Int = 0
        var dl: Int = 0
        var sp: Int = 0
        var cp: Int = 0
        var subp: Int = 0
        var isCs: Boolean = false
        var maxbr: Int = 0
        var fl: Int = 0
        var isToast: Boolean = false
        var flag: Int = 0
        var isPreSell: Boolean = false
    }
}
