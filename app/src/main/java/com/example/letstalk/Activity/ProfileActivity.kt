package com.example.letstalk.Activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.letstalk.Uitil.AppLog
import com.example.letstalk.Uitil.AppStatic
import com.example.letstalk.Uitil.placeHolder
import com.example.letstalk.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth

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
                Glide.with(this).load(it.imageUrl).placeholder(placeHolder(this))
                    .into(binding.ivProfile)
            },
            onCancelled = {
                AppLog.logger(it.message)
            })
        binding.logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, PhoneAuthActivity::class.java))
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}