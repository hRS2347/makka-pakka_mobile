package com.example.makka_pakka.model

import java.sql.Date

data class UserInfo(
    // 用户ID
    var id: Int?,
    // 邮箱
    var email: String?,
    // 昵称
    val name: String?,
    // 头像地址
    var avatarUrl: String?,
    // 性别，0: 男 1: 女
    var sex: Int?,
    // 属地
    var region: String?,
    //生日
    var birthday: Date?,
    //账户创建时间
    var createTime: Date?,
    // 喜好标签是否选择
    // 0: 未选择 1: 选择
    var isHobbySelected: Int?,
    // 简介
    var decription: String?
)
