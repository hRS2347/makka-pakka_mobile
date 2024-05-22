package com.example.makka_pakka.model

//  uid: key, Live_url: v.Live_url,
//  Cover url: v.Cover url,
//  Title: v.Title,
//  Name: v.Name

data class LiveInfo(
    val uid: Int, // 直播人
    val title: String, // 直播标题
    val cover_url: String, // 直播封面的url
    val live_url: String, // 直播的url
    val name: String, // 直播人的名字
)
