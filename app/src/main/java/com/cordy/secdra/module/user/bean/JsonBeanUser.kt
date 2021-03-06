package com.cordy.secdra.module.user.bean

class JsonBeanUser {

    var status = 0
    var message: String? = null
    var data: DataBean? = null

    class DataBean {

        var id: String? = null
        var gender: String? = null
        var birthday: String? = null
        var name: String? = null
        var introduction: String? = null
        var address: String? = null
        var head: String? = null
        var background: String? = null
        var focus: String? = null

    }
}