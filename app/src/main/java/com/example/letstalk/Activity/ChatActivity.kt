package com.example.letstalk.Activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.letstalk.R
import com.example.letstalk.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {

    lateinit var binding: ActivityChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("name")
        val uid = intent.getStringExtra("uid")

        supportActionBar!!.title = name
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
            finish()
        return super.onSupportNavigateUp()
    }

}