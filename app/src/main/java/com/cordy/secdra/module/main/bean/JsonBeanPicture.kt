package com.cordy.secdra.module.main.bean

class JsonBeanPicture {

    var status: Int = 0
    var message: Any? = null
    var data: DataBean? = null

    class DataBean {

        var pageable: PageableBean? = null
        var isLast: Boolean = false
        var totalPages: Int = 0
        var totalElements: Int = 0
        var sort: SortBeanX? = null
        var isFirst: Boolean = false
        var number: Int = 0
        var numberOfElements: Int = 0
        var size: Int = 0
        var isEmpty: Boolean = false
        var content: List<ContentBean>? = null

        class PageableBean {
            /**
             * sort : {"sorted":false,"unsorted":true,"empty":true}
             * pageSize : 20
             * pageNumber : 0
             * offset : 0
             * unpaged : false
             * paged : true
             */

            var sort: SortBean? = null
            var pageSize: Int = 0
            var pageNumber: Int = 0
            var offset: Int = 0
            var isUnpaged: Boolean = false
            var isPaged: Boolean = false

            class SortBean {
                /**
                 * sorted : false
                 * unsorted : true
                 * empty : true
                 */

                var isSorted: Boolean = false
                var isUnsorted: Boolean = false
                var isEmpty: Boolean = false
            }
        }

        class SortBeanX {
            /**
             * sorted : false
             * unsorted : true
             * empty : true
             */

            var isSorted: Boolean = false
            var isUnsorted: Boolean = false
            var isEmpty: Boolean = false
        }

        class ContentBean {
            /**
             * id : 402880e5672087880167208a3d450016
             * introduction : 这是一张我从p站下载的图片，很好看啊，真的很好看啊，所以把他放在自己的网站上，侵删
             * url : 60865838_p0.jpg
             * userId : 402880e566ddba740166ddbce0b70000
             * name : ジャンヌ・ダルク
             * privacy : PUBLIC
             * focus : STRANGE
             * viewAmount : 0
             * likeAmount : 0
             * width : 984
             * height : 1403
             * tagList : ["Fate/Apocrypha","贞德(Fate)","贞德","版权","female saint","Fate/GrandOrder","黄金の精神"]
             * user : {"id":"402880e566ddba740166ddbce0b70000","phone":"1","name":"下雨","gender":"MALE","head":"FtSqi-rnoY4CdCnNEMa1wGMpGA9X","birthday":"2014-05-14 00:00:00","introduction":"这是我初始化的用户，名字随便向网上找的，头像也是，背景也是","address":"","background":"FoONWj3rouvkwOLSOLEjxlF9fo2X","focus":"SElF"}
             * createDate : 2018-11-17 15:19:50
             * modifiedDate : 2018-11-17 15:19:50
             */

            var id: String? = null
            var introduction: String? = null
            var url: String? = null
            var userId: String? = null
            var name: String? = null
            var privacy: String? = null
            var focus: String? = null
            var viewAmount: Int = 0
            var likeAmount: Int = 0
            var width: Int = 0
            var height: Int = 0
            var user: UserBean? = null
            var createDate: String? = null
            var modifiedDate: String? = null
            var tagList: List<String>? = null

            class UserBean {
                /**
                 * id : 402880e566ddba740166ddbce0b70000
                 * phone : 1
                 * name : 下雨
                 * gender : MALE
                 * head : FtSqi-rnoY4CdCnNEMa1wGMpGA9X
                 * birthday : 2014-05-14 00:00:00
                 * introduction : 这是我初始化的用户，名字随便向网上找的，头像也是，背景也是
                 * address :
                 * background : FoONWj3rouvkwOLSOLEjxlF9fo2X
                 * focus : SElF
                 */

                var id: String? = null
                var phone: String? = null
                var name: String? = null
                var gender: String? = null
                var head: String? = null
                var birthday: String? = null
                var introduction: String? = null
                var address: String? = null
                var background: String? = null
                var focus: String? = null
            }
        }
    }
}
