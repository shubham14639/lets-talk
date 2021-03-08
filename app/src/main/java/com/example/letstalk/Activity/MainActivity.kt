package com.example.letstalk.Activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.letstalk.R
import com.example.letstalk.adapter.UserAdapter
import com.example.letstalk.databinding.ActivityMainBinding
import com.example.letstalk.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var databaseReference: DatabaseReference
    lateinit var auth: FirebaseAuth
    lateinit var userAdapter: UserAdapter
    lateinit var userList: ArrayList<Users>
    lateinit var dialoge: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userList = ArrayList()
        auth = FirebaseAuth.getInstance()
        dialoge = ProgressDialog(this)
        dialoge.setMessage("Loading User Profiles")
        dialoge.setCancelable(false)

        getAllUsers()
        /* val storageReference = FirebaseStorage.getInstance()
                .getReferenceFromUrl("gs://let-s-talk-b968e.appspot.com/Profiles/4LDZNTj8KQSbOOB92ytcS9Okcbb2")
            storageReference.downloadUrl.addOnCompleteListener {
                imageUrl = it.result.toString()
                getAllUsers()
            }*/

    }

    private fun getAllUsers() {
        var imageUrl: String = ""
        dialoge.show()
        databaseReference = FirebaseDatabase.getInstance().getReference("users")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (snap: DataSnapshot in snapshot.children) {
                    val name = snap.child("name").value.toString()
                    val phone = snap.child("phone").value.toString()
                    val uid = snap.child("uid").value.toString()
                    val storageReference = FirebaseStorage.getInstance()
                        .getReferenceFromUrl("gs://let-s-talk-b968e.appspot.com/Profiles/")
                    storageReference.child(snap.child("uid").value.toString()).downloadUrl.addOnCompleteListener {
                        imageUrl = it.result.toString()
                        //   Log.d("TESTLOG : ", "final image url $imageUrl")
                        val users = Users(uid, name, phone, imageUrl)
                        userList.add(users)
                        userAdapter = UserAdapter(this@MainActivity, userList)
                        binding.recyclerView.let {
                            it.layoutManager = LinearLayoutManager(this@MainActivity)
                            it.adapter = userAdapter
                        }
                        Log.d("TESTLOG : ", users.toString())
                        dialoge.dismiss()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                auth = FirebaseAuth.getInstance()
                auth.currentUser.delete()
                startActivity(Intent(this, PhoneAuthActivity::class.java))
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}