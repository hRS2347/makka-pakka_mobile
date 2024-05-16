package com.example.makka_pakka.model

data class LiveInfo(val lid:Int, // 直播id , 后面传给web端
    val title:String, // 直播标题
    val url:String, // 直播封面的url
    val name:String, // 直播人的名字
    )
