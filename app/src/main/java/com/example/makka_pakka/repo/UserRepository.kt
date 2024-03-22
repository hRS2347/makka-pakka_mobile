package com.example.makka_pakka.repo


class UserRepository(
) {
    companion object {
        private const val LAST_LOGIN = "last_login_"
        private const val TEXT_PREFIX = "上一次登录\n"
    }
}

