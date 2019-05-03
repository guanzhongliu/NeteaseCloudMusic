package com.twt.lgz.neteasecloudmusic.model.bean

class MusicBean {

    /**
     * data : [{"id":405998841,"url":"http://m10.music.126.net/20190502233308/443e0f339dca9b073e3782cabe57ce04/ymusic/f3fb/235e/a216/2f95cb16b58562445a95eadda702952d.mp3","br":128000,"size":5558901,"md5":"2f95cb16b58562445a95eadda702952d","code":200,"expi":1200,"type":"mp3","gain":-2.18,"fee":8,"uf":null,"payed":0,"flag":132,"canExtend":false,"freeTrialInfo":null,"level":"standard","encodeType":"mp3"}]
     * code : 200
     */

    var code: Int = 0
    var data: List<DataBean>? = null

    class DataBean {
        /**
         * id : 405998841
         * url : http://m10.music.126.net/20190502233308/443e0f339dca9b073e3782cabe57ce04/ymusic/f3fb/235e/a216/2f95cb16b58562445a95eadda702952d.mp3
         * br : 128000
         * size : 5558901
         * md5 : 2f95cb16b58562445a95eadda702952d
         * code : 200
         * expi : 1200
         * type : mp3
         * gain : -2.18
         * fee : 8
         * uf : null
         * payed : 0
         * flag : 132
         * canExtend : false
         * freeTrialInfo : null
         * level : standard
         * encodeType : mp3
         */

        var id: String? = null
        var url: String? = null
        var br: Int = 0
        var size: Int = 0
        var md5: String? = null
        var code: Int = 0
        var expi: Int = 0
        var type: String? = null
        var gain: Double = 0.toDouble()
        var fee: Int = 0
        var uf: Any? = null
        var payed: Int = 0
        var flag: Int = 0
        var isCanExtend: Boolean = false
        var freeTrialInfo: Any? = null
        var level: String? = null
        var encodeType: String? = null
    }
}
