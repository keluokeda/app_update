package com.ke.app_update.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.ke.app_update.lib.AppUpdateManager
import com.ke.app_update.lib.AppUpdateParams
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        lifecycleScope.launch {
            AppUpdateManager().start(
                this@MainActivity,
                AppUpdateParams(
                    "https://cdn2.gdcws.cn/cws/aicheMsg/app/202202/20220211172130_836.apk",
                    File(getExternalFilesDir(null), "apk"),
                    System.currentTimeMillis().toString() + ".apk"
                ),
            ) {
                Log.d("TAG",it.toString())
            }
        }





    }
}