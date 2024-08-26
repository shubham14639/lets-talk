package com.example.letstalk.Activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.letstalk.R
import com.example.letstalk.Uitil.place
import com.example.letstalk.Uitil.progresDialog
import com.example.letstalk.databinding.ActivitySetupUserProfileBinding
import com.example.letstalk.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage

class SetupUserProfile : AppCompatActivity() {
    lateinit var binding: ActivitySetupUserProfileBinding
    var auth: FirebaseAuth? = null
    var database: FirebaseDatabase? = null
    var storage: FirebaseStorage? = null
    var selectedImage: Uri? = null
    var isUserExist: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_user_profile)

        binding = ActivitySetupUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        checkUserExist()
        supportActionBar?.hide()
        binding.imageView.setOnClickListener {
            val intent = Intent()
            intent.setAction(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            startActivityForResult(intent, 22)
        }
        binding.continueBtn.setOnClickListener {
            val name = binding.nameBox.text.toString()
            if (name.isNotEmpty()) {
                if (isUserExist == true) {
                    createUserData(name)
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    createUserData(name)
                }
            } else {
                binding.nameBox.setError("Name cannot be empty")
                Toast.makeText(this, "Please Enter Valid Details", Toast.LENGTH_LONG).show()
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


    fun checkUserExist() {
        isUserExist = false
        val dialog = progresDialog(this, "Please wait..")
        val ref = database!!.getReference("users").child(auth?.uid!!)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    isUserExist = true
                    val user: Users? = snapshot.getValue(Users::class.java)
                    Log.d("TESTLOG", "User data is " + user!!.name)
                    Glide.with(this@SetupUserProfile).load(user.userProfile)
                        .placeholder(place(this@SetupUserProfile,10f,30f)).into(binding.imageView)
                    binding.nameBox.setText(user.name)
                    dialog.dismiss()
                } else {
                    isUserExist = false
                    dialog.dismiss()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                dialog.dismiss()
                isUserExist = false
                Log.d("TESTLOG", "user exits" + error.message)
            }
        })
    }

    fun createUserData(name: String) {
        if (selectedImage != null) {
            val dal = progresDialog(this, "Creating User Profile...")
            val storageRef = storage!!.reference.child("Profiles").child(auth?.uid.toString())
            storageRef.putFile(selectedImage!!).addOnCompleteListener {
                val uid = auth!!.uid.toString()
                val phone = auth?.currentUser?.phoneNumber.toString()
                if (it.isSuccessful) {
                    storageRef.getDownloadUrl().addOnCompleteListener {
                        val imageurl = it.result.toString()
                        val users = Users(uid, name, phone, imageurl,"","")
                        database?.reference?.child("users")?.child(uid)?.setValue(users)
                            ?.addOnCompleteListener {
                                dal.dismiss()
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            }
                    }
                } else {
                    val user = Users(uid, name, phone, "No Image","","")
                    database!!.reference
                        .child("users")
                        .child(uid)
                        .setValue(user)
                        .addOnSuccessListener {
                            dal.dismiss()
                            startActivity(Intent(this@SetupUserProfile, MainActivity::class.java))
                            finish()
                        }
                }
            }
        } else {
            Toast.makeText(this, "Image cannot found", Toast.LENGTH_LONG).show()
        }
    }
}
