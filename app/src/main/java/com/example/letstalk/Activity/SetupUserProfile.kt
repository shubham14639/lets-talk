package com.example.letstalk.Activity

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.letstalk.R
import com.example.letstalk.databinding.ActivitySetupUserProfileBinding
import com.example.letstalk.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage


class SetupUserProfile : AppCompatActivity() {
    lateinit var binding: ActivitySetupUserProfileBinding

    var auth: FirebaseAuth? = null
    var database: FirebaseDatabase? = null
    var storage: FirebaseStorage? = null
    var selectedImage: Uri? = null
    lateinit var dialoge: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_user_profile)

        binding = ActivitySetupUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        dialoge = ProgressDialog(this)
        dialoge.setMessage("Updating Profile")
        dialoge.setCancelable(false)

        supportActionBar?.hide()
        binding.imageView.setOnClickListener {
            val intent = Intent()
            intent.setAction(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            startActivityForResult(intent, 22)
        }
        binding.continueBtn.setOnClickListener {
            val name = binding.nameBox.text.toString()
            if (name.isEmpty()) {
                Toast.makeText(this, "Please Enter Your Name", Toast.LENGTH_LONG).show()
                binding.nameBox.error = "Please type a name"
            } else {
                dialoge.show()
            }
            if (selectedImage != null) {
                val storageRef = storage!!.reference.child("Profiles").child(auth?.uid.toString())
                storageRef.putFile(selectedImage!!).addOnCompleteListener {
                    if (it.isSuccessful) {
                        storageRef.getDownloadUrl().addOnCompleteListener {
                            val imageurl = it.toString()
                            val uid = auth!!.uid.toString()
                            val phone = auth!!.currentUser!!.phoneNumber.toString()
                            val name = binding.nameBox.text.toString()
                            val users = Users(uid, name, phone, imageurl)
                            database?.reference?.child("users")?.child(uid)?.setValue(users)
                                ?.addOnCompleteListener {
                                    dialoge.dismiss()
                                    val intent = Intent(this, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                        }
                    } else {
                        val uid = auth!!.uid.toString()
                        val phone = auth!!.currentUser.phoneNumber.toString()
                        val user = Users(uid, name, phone, "No Image")
                        database!!.reference
                            .child("users")
                            .child(uid)
                            .setValue(user)
                            .addOnSuccessListener {
                                dialoge.dismiss()
                                val intent =
                                    Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                    }
                }
            }

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 22 && resultCode == RESULT_OK) {
            selectedImage = data?.data
            binding.imageView.setImageURI(selectedImage)
        }
    }
}