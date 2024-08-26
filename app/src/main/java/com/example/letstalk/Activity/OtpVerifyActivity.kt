package com.example.letstalk.Activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.letstalk.R
import com.example.letstalk.Uitil.progresDialog
import com.example.letstalk.databinding.ActivityOtpVerifyBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class OtpVerifyActivity : AppCompatActivity() {
    lateinit var binding: ActivityOtpVerifyBinding

    lateinit var mAuth: FirebaseAuth
    lateinit var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    lateinit var verificationId: String
    lateinit var credential: PhoneAuthCredential
    lateinit var number: String
    lateinit var dialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_verify)

        binding = ActivityOtpVerifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        number = intent.getStringExtra("NUMBER").toString()
        binding.tvNumber.setText(number)

        dialog = progresDialog(this, "Sending Otp...")

        mAuth = FirebaseAuth.getInstance()
        mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                Log.d("TESTLOG : oncomplete ", p0.smsCode)
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                dialog.dismiss()
                Log.d("TESTLOG : oncomplete ", p0.message.toString())
                Toast.makeText(
                    this@OtpVerifyActivity,
                    "OTP Verification Failed" + p0.message,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                dialog.dismiss()
                verificationId = p0
            }
        }
        sendCodetoPhoneNumber(number)

        binding.submit.setOnClickListener {
            if (binding.etOtp.text.isNullOrBlank()) {
                Toast.makeText(this, "Null or Blank is not allowed", Toast.LENGTH_SHORT).show()
            } else {
                verifyCode(binding.etOtp.text.toString())
            }
        }
    }

    private fun sendCodetoPhoneNumber(number: String?) {
        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(number)
            .setActivity(this)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setCallbacks(mCallbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun verifyCode(code: String) {
        credential = PhoneAuthProvider.getCredential(verificationId, code)
        signInwithCredentials(credential)
    }

    private fun signInwithCredentials(credential: PhoneAuthCredential) {

        mAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                startActivity(Intent(this, SetupUserProfile::class.java))
                finishAffinity()
            } else {
                Toast.makeText(this, "Please Enter Valid OTP", Toast.LENGTH_LONG).show()
            }
        }
    }
}