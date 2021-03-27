package com.example.letstalk.Activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.letstalk.R
import com.example.letstalk.Uitil.AppLog
import com.example.letstalk.Uitil.makeToast
import com.example.letstalk.Uitil.progresDialog
import com.example.letstalk.adapter.UserAdapter
import com.example.letstalk.databinding.ActivityMainBinding
import com.example.letstalk.model.Users
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var databaseReference: DatabaseReference
    lateinit var auth: FirebaseAuth
    lateinit var userAdapter: UserAdapter
    lateinit var userList: ArrayList<Users>
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userList = ArrayList()
        auth = FirebaseAuth.getInstance()

        getAllUsers()

        binding.bottomNavigationView.setOnNavigationItemSelectedListener(object :
            BottomNavigationView.OnNavigationItemReselectedListener,
            BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.ic_profile -> {
                        startActivity(Intent(this@MainActivity, ProfileActivity::class.java))
                    }
                    R.id.ic_calls -> {
                        makeToast(this@MainActivity, "Calls Clicks")
                    }
                    R.id.ic_chat -> {
                        // startActivity(Intent(this@MainActivity, ChatFragment::class.java))
                    }
                }
                return true
            }

            override fun onNavigationItemReselected(item: MenuItem) {

            }
        })

    }

    private fun getAllUsers() {
        val dialog = progresDialog(this, "Loading Please Wait...")
        databaseReference = FirebaseDatabase.getInstance().getReference("users")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (snap: DataSnapshot in snapshot.children) {
                    val user: Users? = snap.getValue(Users::class.java)
                    if (!user!!.uid.equals(FirebaseAuth.getInstance().uid))
                        userList.add(user)
                    userAdapter = UserAdapter(this@MainActivity, userList)
                    binding.recyclerView.let {
                        it.layoutManager = LinearLayoutManager(this@MainActivity)
                        it.adapter = userAdapter
                        dialog.dismiss()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                AppLog.logger("onMainActivity :${error.message}")
                dialog.dismiss()
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                auth = FirebaseAuth.getInstance()
                auth.signOut()
                startActivity(Intent(this, PhoneAuthActivity::class.java))
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}