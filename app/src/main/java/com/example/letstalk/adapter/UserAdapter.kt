package com.example.letstalk.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.letstalk.Activity.ChatActivity
import com.example.letstalk.Activity.MainActivity
import com.example.letstalk.R
import com.example.letstalk.model.Users
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserAdapter(mainActivity: MainActivity, users: ArrayList<Users>) :
    RecyclerView.Adapter<UserAdapter.userViewHolder>() {
    var cont: Context = mainActivity
    var users: ArrayList<Users>? = users

    inner class userViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(users: Users, lMsg: String, lTime: String) {
            val ivImage = itemView.findViewById<ImageView>(R.id.iv_userImage)
            itemView.findViewById<TextView>(R.id.tv_name).text = users.name
            itemView.findViewById<TextView>(R.id.tv_lastMsg).text = lMsg
            itemView.findViewById<TextView>(R.id.tv_time).text = lTime
            Glide.with(cont).load(users.imageUrl).placeholder(R.drawable.avatar).into(ivImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): userViewHolder {
        val view = LayoutInflater.from(cont).inflate(R.layout.row_chat, parent, false)
        return userViewHolder(view)
    }

    override fun onBindViewHolder(holder: userViewHolder, position: Int) {
        val user = users!![position]
        val ref = FirebaseDatabase.getInstance().getReference("lastUpdate")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lMsg = snapshot.child("lastMsg").value.toString()
                val lTime = snapshot.child("lastMsgTime").value.toString()
                holder.bind(user, lMsg, lTime)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(cont, error.message, Toast.LENGTH_LONG).show()
            }
        })
        holder.itemView.setOnClickListener {
            val intent = Intent(cont, ChatActivity::class.java)
            intent.putExtra("name", user.name)
            intent.putExtra("imageUrl", user.imageUrl)
            intent.putExtra("uid", user.uid)
            intent.putExtra("currentUser", user.currentUser)
            cont.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return users!!.size
    }
}