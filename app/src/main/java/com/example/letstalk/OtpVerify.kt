package com.example.letstalk

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.letstalk.databinding.ActivityOtpVerifyBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class OtpVerify : AppCompatActivity(), TextWatcher {
    lateinit var binding: ActivityOtpVerifyBinding

    lateinit var mAuth: FirebaseAuth
    lateinit var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    lateinit var verificationId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_verify)

        binding = ActivityOtpVerifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val number = intent.getStringExtra("NUMBER")
        binding.tvNumber.setText(number)

        binding.let {
            it.etOne.addTextChangedListener(this)
            it.etTwo.addTextChangedListener(this)
            it.etThree.addTextChangedListener(this)
            it.etFour.addTextChangedListener(this)
            it.etFive.addTextChangedListener(this)
            it.etSix.addTextChangedListener(this)
        }

        mAuth = FirebaseAuth.getInstance()
        mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {

            }
            override fun onVerificationFailed(p0: FirebaseException) {
                Toast.makeText(
                    this@OtpVerify,
                    "Verification Failed" + p0,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                verificationId = p0
            }
        }
        val phoneNumber = "+91" + (number)
        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(phoneNumber)
            .setActivity(this)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setCallbacks(mCallbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(s: Editable?) {
        if (s!!.length == 1) {
            if (binding.etOne.length() == 1) {
                binding.etTwo.requestFocus()
            }
            if (binding.etTwo.length() == 1) {
                binding.etThree.requestFocus()
            }
            if (binding.etThree.length() == 1) {
                binding.etFour.requestFocus()
            }
            if (binding.etFour.length() == 1) {
                binding.etFive.requestFocus()
            }
            if (binding.etFive.length() == 1) {
                binding.etSix.requestFocus()
            } else if (s.isEmpty()) {
                if (binding.etSix.length() == 0) {
                    binding.etFive.requestFocus()
                }
                if (binding.etFive.length() == 0) {
                    binding.etFour.requestFocus()
                }
                if (binding.etFour.length() == 0) {
                    binding.etThree.requestFocus()
                }
                if (binding.etThree.length() == 0) {
                    binding.etTwo.requestFocus()
                }
                if (binding.etTwo.length() == 0) {
                    binding.etOne.requestFocus()
                }
            }
        }
    }

    private fun isFieldValid(): Boolean {
        val one = binding.etOne.text.toString()
        val two = binding.etTwo.text.toString()
        val three = binding.etThree.text.toString()
        val four = binding.etFour.text.toString()
        val five = binding.etFive.text.toString()
        val six = binding.etSix.text.toString()
        val sb: StringBuilder = StringBuilder()

        sb.append(one).append(two).append(three).append(four).append(five).append(six)
        if (!sb.isEmpty()) {
            val otp = sb
            return true
        } else {
            Toast.makeText(this, "otp cannnot be empty", Toast.LENGTH_LONG).show()
            return false
        }
    }
}