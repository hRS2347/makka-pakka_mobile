package com.example.makka_pakka.model


data class RoomInfo(
    val id: Int,
    val number: Int, // 房间号
    val uid: Int, // 主播id
    val url: String?, // 封面图
    val name: String,// 直播间名
    val detail: String // 直播间描述
)
