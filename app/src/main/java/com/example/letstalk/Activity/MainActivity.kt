package com.example.letstalk.Activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.letstalk.R
import com.example.letstalk.adapter.UserAdapter
import com.example.letstalk.databinding.ActivityMainBinding
import com.example.letstalk.model.Users
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var databaseReference: DatabaseReference
    lateinit var userAdapter: UserAdapter
    lateinit var userList: ArrayList<Users>
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userList = ArrayList()

        getAllUsers()
    }

    private fun getAllUsers() {
        databaseReference = FirebaseDatabase.getInstance().getReference("users")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (snap: DataSnapshot in snapshot.children) {
                    val name = snap.child("name").value
                    val phone = snap.child("phone").value
                    val imageUrl = snap.child("imageUrl").value
                    val uid = snap.child("uid").value


                    Glide.with(this@MainActivity).load(imageUrl).into(binding.testImage)

                    val users = Users(
                        uid.toString(),
                        name.toString(),
                        phone.toString(),
                        imageUrl.toString()
                    )
                    userList.add(users)
                }
                userAdapter = UserAdapter(this@MainActivity, userList)
                binding.recyclerView.let {
                    it.layoutManager = LinearLayoutManager(this@MainActivity)
                    it.adapter = userAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}