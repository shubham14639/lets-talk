package com.example.letstalk

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.letstalk.adapter.UserAdapter
import com.example.letstalk.databinding.ActivityMainBinding
import com.example.letstalk.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var database: FirebaseDatabase
    lateinit var userAdapter: UserAdapter
    lateinit var userList: ArrayList<Users>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = FirebaseDatabase.getInstance()
        userList = ArrayList()
        database.reference.child("users").child(FirebaseAuth.getInstance().uid.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                          for (snap: DataSnapshot in snapshot.children) {
                              val uid = snap.child("uid").getValue()
                              val name = snap.child("name").getValue()
                              val phone = snap.child("phone").getValue()
                              val url = snap.child("imageUrl").getValue()
                              val u = Users(uid.toString(), name.toString(), phone.toString(), url.toString())
                              Log.d("TESTLOG : ", u.toString())
                              userList.add(u)
                          }
                    } else {
                        Log.d("TESTLOG : ", "Snap Does Not Exists")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("TESTLOG : ", error.message)
                }
            })
        userAdapter = UserAdapter(this, userList)
        binding.recyclerView.let {
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = userAdapter
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}