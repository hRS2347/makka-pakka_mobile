package com.example.makka_pakka

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.makka_pakka.databinding.ActivityMainBinding
import com.example.makka_pakka.utils.PermissionUtil
import com.example.makka_pakka.utils.ViewUtil
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var isHobbySelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        test()
        PermissionUtil.setUp(this)
        if (!PermissionUtil.checkPermission()) {
            Toast.makeText(this, "请允许APP访问所有文件，否则无法使用。", Toast.LENGTH_SHORT).show();
            this.finish()
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        //点击收起键盘
        window.decorView.setOnTouchListener { _, _ ->
            ViewUtil.hideKeyboard(this)
            false
        }


        val navView: BottomNavigationView = binding.bottomNavigation
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)
        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_broadcast -> {
                    try {
                        navController.popBackStack(R.id.mainFragment, false)
                        navController.navigate(R.id.mainFragment)
                    } catch (e: Exception) {
                        Log.i("nav", e.toString())
                    }
                    true
                }

                R.id.navigation_mine -> {
                    try {
                        navController.popBackStack(R.id.mainFragment, false)
                        navController.navigate(R.id.mineFragment)
                    } catch (e: Exception) {
                        Log.i("nav", e.toString())
                    }
                    true
                }

                else -> {
                    false
                }
            }
        }
        //让navview 出现时，动画效果更加平滑
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.mainFragment, R.id.mineFragment -> {
                    if (navView.visibility == View.GONE) {
                        //动画效果，上滑
                        navView.translationY = navView.height.toFloat()
                        navView.animate().translationY(0f).setDuration(500).start()
                    }

                    navView.visibility = View.VISIBLE
                    navView.selectedItemId = destination.id
                    when (destination.id) {
                        R.id.mainFragment -> {
                            navView.menu.findItem(R.id.navigation_broadcast).isChecked = true
                        }

                        R.id.mineFragment -> {
                            navView.menu.findItem(R.id.navigation_mine).isChecked = true
                        }
                    }
                }

                else -> {
                    if (navView.visibility == View.VISIBLE) {
                        //动画效果，下滑
                        navView.translationY = 0f
                        navView.animate().translationY(navView.height.toFloat()).setDuration(500).start()
                    }
                    navView.visibility = View.GONE
                }
            }
        }
    }

    /***
     * 测试
     */
    private fun testC() = Unit
    private fun test() {
    }

    private val DOUBLE_BACK_PRESS_TIME_DELAY: Long = 2000 // 定义两次返回间隔的时间，单位为毫秒

    private var lastBackPressTime: Long = 0 // 记录上一次按下返回键的时间

}