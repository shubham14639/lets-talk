package com.example.letstalk.Activity

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.letstalk.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splesh)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        spleshTime()
    }

    private fun spleshTime() {
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
}