package com.example.makka_pakka.boardcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.os.Parcelable
import android.util.Log
import android.widget.Toast


class NetworkConnectChangedReceiver : BroadcastReceiver() {
    private fun getConnectionType(type: Int): String {
        var connType = ""
        if (type == ConnectivityManager.TYPE_MOBILE) {
            connType = "移动网络"
        } else if (type == ConnectivityManager.TYPE_WIFI) {
            connType = "WIFI网络"
        }
        return connType
    }

    override fun onReceive(context: Context, intent: Intent) {
//        if (WifiManager.WIFI_STATE_CHANGED_ACTION == intent.action) { // 监听wifi的打开与关闭，与wifi的连接无关
//            val wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0)
//            Log.e("NETWORK_STATE", "wifiState:$wifiState")
//            when (wifiState) {
//                WifiManager.WIFI_STATE_DISABLED -> {}
//                WifiManager.WIFI_STATE_DISABLING -> {}
//            }
//        }
        // 监听wifi的连接状态即是否连上了一个有效无线路由
        if (WifiManager.NETWORK_STATE_CHANGED_ACTION == intent.action) {
            val parcelableExtra = intent
                .getParcelableExtra<Parcelable>(WifiManager.EXTRA_NETWORK_INFO)
            if (null != parcelableExtra) {
                // 获取联网状态的NetWorkInfo对象
                val networkInfo = parcelableExtra as NetworkInfo
                //获取的State对象则代表着连接成功与否等状态
                val state = networkInfo.state
                //判断网络是否已经连接
                val isConnected = state == NetworkInfo.State.CONNECTED
                Log.e("NETWORK_STATE", "isConnected:$isConnected")
                if (isConnected) {
                } else {

                }
            }
        }
        // 监听网络连接，包括wifi和移动数据的打开和关闭,以及连接上可用的连接都会接到监听
        if (ConnectivityManager.CONNECTIVITY_ACTION == intent.action) {
            //获取联网状态的NetworkInfo对象
            val info = intent
                .getParcelableExtra<NetworkInfo>(ConnectivityManager.EXTRA_NETWORK_INFO)
            if (info != null) {
                //如果当前的网络连接成功并且网络连接可用
                if (NetworkInfo.State.CONNECTED == info.state && info.isAvailable) {
                    if (info.type == ConnectivityManager.TYPE_WIFI
                        || info.type == ConnectivityManager.TYPE_MOBILE
                    ) {
                        Log.i("NETWORK_STATE", getConnectionType(info.type) + "连上")
                        if (info.type == ConnectivityManager.TYPE_WIFI) {
                            //wifi
                            Toast.makeText(context, "正在使用WIFI", Toast.LENGTH_SHORT).show()
                        } else {
                            //移动网络
                            Toast.makeText(context, "正在使用移动数据，请注意流量消耗", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Log.i("NETWORK_STATE", getConnectionType(info.type) + "断开")
                }
            }
        }
    }
}