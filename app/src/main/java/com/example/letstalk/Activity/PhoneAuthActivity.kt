package com.example.letstalk.Activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.letstalk.databinding.ActivityPhoneAuthBinding
import com.google.firebase.auth.FirebaseAuth

class PhoneAuthActivity : AppCompatActivity() {
    lateinit var binding: ActivityPhoneAuthBinding
    lateinit var mAuth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPhoneAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth= FirebaseAuth.getInstance()
        if (mAuth.currentUser!=null){
            startActivity(Intent(this, MainActivity::class.java))
            finishAffinity()
        }
        binding.submit.setOnClickListener {
            val n = binding.etPhone.text.toString()
            val number = "+91" + n
            val intent = Intent(this, OtpVerify::class.java)
            intent.putExtra("NUMBER", number)
            startActivity(intent)
        }
    }
}