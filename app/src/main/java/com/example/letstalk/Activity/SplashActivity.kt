package com.example.letstalk.Activity

import android.app.ActivityManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.letstalk.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_splesh)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        splashTime()
        test()
    }

    private fun splashTime() {
        val timer: Thread = object : Thread() {
            override fun run() {
                try {
                    sleep(2500)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                } finally {
                    val loginIntent = Intent(this@SplashActivity, PhoneAuthActivity::class.java)
                    startActivity(loginIntent)
                    finish()
                }
            }
        }
        timer.start()
    }

    private fun test() {
        val am: ActivityManager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val process: List<ActivityManager.RunningAppProcessInfo> = am.runningAppProcesses
        for (processInfo:ActivityManager.RunningAppProcessInfo in process) {
            if (processInfo.importance==ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND){
                for (activeProcess in processInfo.pkgList){
                    Log.d("Processing ", activeProcess)
                }
            }
        }
    }
}