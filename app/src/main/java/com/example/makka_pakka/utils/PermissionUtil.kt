package com.example.makka_pakka.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.makka_pakka.MainActivity
import com.tbruyelle.rxpermissions3.RxPermissions

object PermissionUtil {
    private lateinit var rxPermissions: RxPermissions
    fun setUp(context: Context) {
        rxPermissions = RxPermissions(context as MainActivity)
    }

    @SuppressLint("CheckResult")
    fun checkPermission(): Boolean {
        var flag = true
        rxPermissions.requestEach(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.MANAGE_EXTERNAL_STORAGE,
            Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.MANAGE_MEDIA,
            Manifest.permission.CAMERA,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_SETTINGS,
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.WRITE_CALENDAR
//            android.permission.READ_CALENDAR or android.permission.WRITE_CALENDAR

        ).subscribe { permission ->
            if (!permission.granted) flag = false
        }
        return flag
    }
}