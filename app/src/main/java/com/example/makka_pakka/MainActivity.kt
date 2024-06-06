package com.example.makka_pakka

import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.Rect
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.makka_pakka.boardcast.NetworkConnectChangedReceiver
import com.example.makka_pakka.boardcast.ReLoginReceiver
import com.example.makka_pakka.databinding.ActivityMainBinding
import com.example.makka_pakka.model.UserInfo
import com.example.makka_pakka.sound_flex.GestureControlListener
import com.example.makka_pakka.utils.HttpUtil
import com.example.makka_pakka.utils.PermissionUtil
import com.example.makka_pakka.utils.ViewUtil
import com.example.makka_pakka.utils.GsonUtil
import com.example.makka_pakka.utils.MediaControlUtil
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import okhttp3.Callback
import okhttp3.Response
import szu.stclkhlww.shushengyiqi.playsound.AudioTrackManager
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var isHobbySelectedAsk = false
    private lateinit var navController: NavController
    private lateinit var handler: Handler
    val viewModel: MainActivityViewModel by viewModels { MainActivityViewModel.Factory }
    var gestureController: GestureControlListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        val filter = IntentFilter()
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(NetworkConnectChangedReceiver(), filter)

        ContextCompat.registerReceiver(
            this,
            ReLoginReceiver(),
            IntentFilter(RELOGIN_ACTION),
            ContextCompat.RECEIVER_NOT_EXPORTED
        )

        PermissionUtil.setUp(this)
        if (!PermissionUtil.checkPermission()) {
            Toast.makeText(this, "请允许APP访问所有文件，否则无法使用。", Toast.LENGTH_SHORT).show();
            this.finish()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Toast.makeText(this, "请允许APP访问所有文件，否则无法使用。", Toast.LENGTH_SHORT)
                    .show();
                startActivity(Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION));
            }
        }

        HttpUtil.setUp { sendBroadcast(Intent(RELOGIN_ACTION)) }

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        //点击收起键盘
        window.decorView.setOnTouchListener { _, _ ->
            ViewUtil.hideKeyboard(this)
            false
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//FLAG_FORCE_NOT_FULLSCREEN   FLAG_FULLSCREEN FLAG_TRANSLUCENT_STATUS
        transparentStatusBar(window)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        navController = navHostFragment.navController
        val navView = binding.bottomNavigation

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
                    if (destination.id == R.id.mainFragment) {
                        //设置选中效果，选中18dp，未选中16dp
                        binding.home.textSize = 18f
                        binding.mine.textSize = 16f
                        binding.home.setTextColor(
                            ResourcesCompat.getColor(
                                resources,
                                R.color.black,
                                null
                            )
                        )
                        binding.mine.setTextColor(
                            ResourcesCompat.getColor(
                                resources,
                                R.color.stroke_grey,
                                null
                            )
                        )
                    } else {
                        binding.home.textSize = 16f
                        binding.mine.textSize = 18f
                        binding.home.setTextColor(
                            ResourcesCompat.getColor(
                                resources,
                                R.color.stroke_grey,
                                null
                            )
                        )
                        binding.mine.setTextColor(
                            ResourcesCompat.getColor(
                                resources,
                                R.color.black,
                                null
                            )
                        )
                    }
                }

                else -> {
                    if (navView.visibility == View.VISIBLE) {
                        //动画效果，下滑
                        navView.translationY = 0f
                        navView.animate().translationY(navView.height.toFloat())
                            .setDuration(500)
                            .start()
                    }
                    navView.visibility = View.GONE
                }
            }
        }




        MyApplication.instance.currentUser.observe(this) {
            if (it != null && it.isHobbySelected == 0 && !isHobbySelectedAsk) {
                isHobbySelectedAsk = true
                navController.popBackStack(R.id.mainFragment, false)
                navController.navigate(R.id.mainFragment)
            }
        }

        binding.home.setOnClickListener {
            toHome()
        }

        binding.mine.setOnClickListener {
            toMine()
        }

        handler = Handler(Handler.Callback {
            when (it.what) {
                1 -> {
                    //重新登录
                    HttpUtil.getUserInfo(object : Callback {
                        override fun onFailure(call: okhttp3.Call, e: IOException) {
                            Log.d("MainActivity", "onFailure: ${e.message}")
                        }

                        override fun onResponse(call: okhttp3.Call, response: Response) {
                            if (response.code == 200) {
                                val body = response.body?.string()
                                Log.i("MyApplication", "onResponse: $body")
                                try {
                                    val myResponse =
                                        GsonUtil.fromJsonToResponse(body, UserInfo::class.java)
                                    MyApplication.instance.currentUser.postValue(myResponse.data)
                                    handler.sendEmptyMessage(2)
                                } catch (e: Exception) {
                                    Log.e("MyApplication", "onResponse: ${e.message}")
                                    Log.e("MyApplication", "onResponse: $body")
                                }
                            } else {
                                Log.e("MyApplication", "onResponse: ${response.body?.string()}")
                            }
                        }

                    })
                }

                2 -> {
                    //重新登录
                    Toast.makeText(this@MainActivity, "Welcome back", Toast.LENGTH_SHORT)
                        .show()
                    navController.navigate(R.id.mainFragment)
                }
            }
            true
        })

        lifecycleScope.launch {
            MyApplication.instance.dataStoreRepository.readStringFromDataStore("token").take(1)
                .collect { value ->
                    Log.d("token", "MainActivity:token in sp: $value")
                    // 处理第一个发射的值
                    MyApplication.instance.currentToken = value ?: ""
                    if (MyApplication.instance.currentToken.isNotBlank()) {
                        //获取用户信息
                        MyApplication.instance.testAsyncJump {
//                        MyApplication.instance.reGetUserInfo {
                            //旧的token有效，直接进入主界面
                            handler.sendEmptyMessage(1)
                        }
                    }
                }
        }

        binding.btnBroadcast.setOnClickListener {
            findNavController(R.id.nav_host_fragment_activity_main).navigate(R.id.action_global_broadcastFragment)
        }
        viewModel.recordingTime.observe(this) {
            val time = it / 10F
            if (it > 18) {
                binding.tvCountingTime.text = "准备"
            } else
                binding.tvCountingTime.text = (time).toString()

            if (time > 1.2F) {
                binding.tvCountingTime.setTextColor(
                    ActivityCompat.getColor(
                        this,
                        R.color.secondary_color
                    )
                )
            } else if (time > 0.6F) {
                binding.tvCountingTime.setTextColor(
                    ActivityCompat.getColor(
                        this,
                        R.color.primary_color
                    )
                )
            } else {
                binding.tvCountingTime.setTextColor(
                    ActivityCompat.getColor(
                        this,
                        R.color.warning_red
                    )
                )
            }
        }
        viewModel.errorMsg.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
        viewModel.predictResult.observe(this) {
            Log.i("predict result", it.toString())
            binding.tvCountingTime.setTextColor(
                ActivityCompat.getColor(
                    this,
                    R.color.primary_color
                )
            )
            if (it == -1)
                binding.tvCountingTime.text = "准备"
            else {
                if (viewModel.isLocked.value!!)
                    binding.tvCountingTime.text = "未解锁：$it"
                else {
                    gestureController?.onGestureControl(it)
                    binding.tvCountingTime.text = "结果：$it"
                }
            }

        }

        binding.floatingTab.setOnClickListener {
            viewModel.switchPredictRunningState()
        }

        viewModel.isPredictRunningOn.observe(this) {
            binding.floatingTab.background = if (it && !viewModel.isLocked.value!!) {
                ActivityCompat.getDrawable(
                    this,
                    R.drawable.side_floating_white_card_on
                )
            } else if (it && viewModel.isLocked.value!!) {
                ActivityCompat.getDrawable(
                    this,
                    R.drawable.side_floating_white_card_locked
                )
            } else {
                ActivityCompat.getDrawable(
                    this,
                    R.drawable.side_floating_white_card
                )
            }
            if (!it) {
                binding.tvCountingTime.text = "暂停中"
                binding.tvCountingTime.setTextColor(
                    ActivityCompat.getColor(
                        this,
                        R.color.tertiary_color
                    )
                )
            }
        }

        viewModel.isLocked.observe(this) {
            binding.floatingTab.background = if (it) {
                ActivityCompat.getDrawable(
                    this,
                    R.drawable.side_floating_white_card_locked
                )
            } else {
                ActivityCompat.getDrawable(
                    this,
                    R.drawable.side_floating_white_card_on
                )
            }
        }

        viewModel.isPredictTabOn.observe(this) {
            //右滑消失 或 左滑出现
            if (it) {
                binding.floatingTab.visibility = View.VISIBLE
                binding.floatingTab.animate().translationX(0f).setDuration(300).start()
            } else {
                binding.floatingTab.animate().translationX(
                    binding.floatingTab.width.toFloat()
                ).setDuration(300).start()
            }
        }
        AudioTrackManager.startPlaying()

//        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object :
//            ViewTreeObserver.OnGlobalLayoutListener {
//            private var isKeyboardShowing = false
//
//            override fun onGlobalLayout() {
//                if (MyApplication.instance.keyboardHeight != 0f) { // 只需要获取一次
//                    return
//                }
//                val r = Rect()
//                binding.root.getWindowVisibleDisplayFrame(r)
//                val screenHeight = binding.root.rootView.height
//                val keypadHeight = screenHeight - r.bottom
//
//                if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
//                    // Keyboard is opened
//                    Log.d("Keyboard Height", "Height: $keypadHeight")
//                    // 转换为rem并处理
//                    val keyboardHeightInRem = ViewUtil.pxToRem(keypadHeight)
//                    Log.d("Keyboard Height in rem", "Height in rem: $keyboardHeightInRem")
//                    MyApplication.instance.keyboardHeight = keyboardHeightInRem
//                }
//
//            }
//        })
    }

    fun toMine() {
        navController.navigate(R.id.action_global_mineFragment)
    }

    fun toHome() {
        navController.popBackStack(R.id.mainFragment, false)
        navController.navigate(R.id.mainFragment)
    }

    private var backPressedTime: Long = 0
    private val backPressedInterval: Long = 2000 // 2 seconds


    fun transparentStatusBar(window: Window) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        var systemUiVisibility = window.decorView.systemUiVisibility
        systemUiVisibility =
            systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.decorView.systemUiVisibility = systemUiVisibility
        window.statusBarColor = Color.TRANSPARENT

        //设置状态栏文字颜色
        setStatusBarTextColor(window, false)
        test()
    }

    fun setStatusBarTextColor(window: Window, light: Boolean) {
        var systemUiVisibility = window.decorView.systemUiVisibility
        systemUiVisibility = if (light) { //白色文字
            systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        } else { //黑色文字
            systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        window.decorView.systemUiVisibility = systemUiVisibility
    }

    fun reLogin() {
        //弹出所有界面，回到封面
        navController.popBackStack(R.id.coverFragment, false)
    }

    /***
     * 测试
     */
    private fun testC() = Unit
    private fun test() {

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(NetworkConnectChangedReceiver())
        unregisterReceiver(ReLoginReceiver())
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    fun addOnPressBackListener(listener: OnPressBackListener) {
        onPressBackListenerStack.add(listener)
    }

    fun removeOnPressBackListener(listener: OnPressBackListener) {
        onPressBackListenerStack.remove(listener)
    }

    private val onPressBackListenerStack = mutableListOf<OnPressBackListener>()
    override fun onBackPressed() {
        try {
            if (onPressBackListenerStack.isNotEmpty()) {
                onPressBackListenerStack.last().doWhenBackPressed() // 调用栈顶的 doWhenBackPressed 方法
                return
            }

            //上面没有把事件消费掉，就执行默认的返回操作
            Log.d("MainActivity", "onBackPressed: ${navController.currentDestination?.id}")
            //将返回的目的地绑定bottom
            if (navController.currentDestination?.id != R.id.mainFragment) {
                navController.popBackStack()
                return
            }
            // 获取当前时间
            val currentTime = System.currentTimeMillis()
            // 如果当前时间与上次按下返回键的时间间隔小于设定的时间间隔
            if (currentTime - backPressedTime < backPressedInterval) {
                // 调用父类的 onBackPressed 方法，退出应用
                super.onBackPressed()
            } else {
                // 提示再次点击返回键退出应用
                Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()
                // 更新上次按下返回键的时间
                backPressedTime = currentTime
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "onBackPressed: ${e.message}")
        }
    }

    fun volumeUp() = MediaControlUtil.volumeUp(this)
    fun volumeDown() = MediaControlUtil.volumeDown(this)
    fun brightnessUp() = MediaControlUtil.brightnessUp(this)
    fun brightnessDown() = MediaControlUtil.brightnessDown(this)
    fun switchNav() {
        if (navController.currentDestination?.id == R.id.mainFragment) {
            toMine()
        } else {
            toHome()
        }
    }

}