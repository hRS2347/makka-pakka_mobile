package com.example.makka_pakka.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import com.example.makka_pakka.LOGIN

object MediaControlUtil {
    fun volumeUp(context: Context) {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, 0)
        Log.d(LOGIN, "volume: ${audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)}")
    }

    fun volumeDown(context: Context) {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, 0)
        Log.d(LOGIN, "volume: ${audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)}")
    }

    fun brightnessUp(activity: Activity) {
        if (Settings.System.canWrite(activity)) {
            val resolver = activity.contentResolver
            Settings.System.putInt(
                resolver,
                Settings.System.SCREEN_BRIGHTNESS,
                getBrightness(activity) + 10
            )
        } else {
            Toast.makeText(activity, "需要修改系统设置的权限", Toast.LENGTH_LONG).show()
            // 请求用户授予WRITE_SETTINGS权限
            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
            intent.data = Uri.parse("package:" + activity.packageName)
            activity.startActivityForResult(intent, 200)
        }
        Log.d(LOGIN, "brightness: ${getBrightness(activity)}")
    }

    private fun getBrightness(activity: Activity): Int {
        val resolver = activity.contentResolver
        return Settings.System.getInt(resolver, Settings.System.SCREEN_BRIGHTNESS)
    }

    fun brightnessDown(activity: Activity) {
        if (Settings.System.canWrite(activity)) {
            val resolver = activity.contentResolver
            Settings.System.putInt(
                resolver,
                Settings.System.SCREEN_BRIGHTNESS,
                getBrightness(activity) - 10
            )
        } else {
            Toast.makeText(activity, "需要修改系统设置的权限", Toast.LENGTH_LONG).show()
            // 请求用户授予WRITE_SETTINGS权限
            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
            intent.data = Uri.parse("package:" + activity.packageName)
            activity.startActivity(intent)
        }
        Log.d(LOGIN, "brightness: ${getBrightness(activity)}")
    }


    fun setVolume(context: Context, volume: Int) {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0)
    }

    fun getVolume(context: Context): Int {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        return audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
    }
}