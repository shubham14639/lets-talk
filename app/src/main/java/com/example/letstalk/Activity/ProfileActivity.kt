package com.example.letstalk.Activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.letstalk.Uitil.AppLog
import com.example.letstalk.Uitil.AppStatic
import com.example.letstalk.Uitil.place
import com.example.letstalk.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppStatic.checkUserExist(
            onDataChange = {
                binding.tvName.text = it.name
                binding.tvPhone.text = it.phone
                Glide.with(this).load(it.userProfile).placeholder(place(this))
                    .into(binding.ivProfile)
            },
            onCancelled = {
                AppLog.logger(it.message)
            })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}