package com.example.letstalk.Activity

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.letstalk.R
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
        dialoge.setMessage("Loading Profile Please Wait")
        dialoge.setCancelable(false)
        dialoge.show()
        checkUserExist()
        supportActionBar?.hide()
        binding.imageView.setOnClickListener {
            val intent = Intent()
            intent.setAction(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            startActivityForResult(intent, 22)
        }
        binding.continueBtn.setOnClickListener {
            if (checkUserExist()) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                val name = binding.nameBox.text.toString()
                if (name.isEmpty() || selectedImage == null) {
                    Toast.makeText(this, "Please Enter Valid Details", Toast.LENGTH_LONG).show()
                    binding.nameBox.error = "Please type a name"
                } else {
                    createUserData(name)
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

    fun checkUserExist(): Boolean {
        val ref = database!!.getReference("users").child(auth?.uid!!)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user: Users? = snapshot.getValue(Users::class.java)
                Log.d("TESTLOG", "User data is " + user!!.name)
                Glide.with(this@SetupUserProfile).load(user.imageUrl).into(binding.imageView)
                binding.nameBox.setText(user.name)
                dialoge.hide()
            }

            override fun onCancelled(error: DatabaseError) {
                dialoge.hide()
                Log.d("TESTLOG", "user exits" + error.message)
            }
        })
        return true
    }

    fun createUserData(name: String) {
        if (selectedImage != null) {
            val dia = ProgressDialog(this)
            dia.setMessage("Updating Profile")
            dia.setCancelable(false)
            dia.show()
            val storageRef = storage!!.reference.child("Profiles").child(auth?.uid.toString())
            storageRef.putFile(selectedImage!!).addOnCompleteListener {
                val uid = auth!!.uid.toString()
                val phone = auth?.currentUser?.phoneNumber.toString()
                if (it.isSuccessful) {
                    storageRef.getDownloadUrl().addOnCompleteListener {
                        val imageurl = it.result.toString()
                        val users = Users(uid, name, phone, imageurl, "")
                        database?.reference?.child("users")?.child(uid)?.setValue(users)
                            ?.addOnCompleteListener {
                                dia.dismiss()
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            }
                    }
                } else {
                    val user = Users(uid, name, phone, "No Image", "")
                    database!!.reference
                        .child("users")
                        .child(uid)
                        .setValue(user)
                        .addOnSuccessListener {
                            dia.dismiss()
                            startActivity(Intent(this@SetupUserProfile, MainActivity::class.java))
                            finish()
                        }
                }
            }
        } else {
            Toast.makeText(this, "Image is empty", Toast.LENGTH_LONG).show()
        }
    }
}