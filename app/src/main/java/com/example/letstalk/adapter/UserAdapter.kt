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
import com.example.letstalk.R
import com.example.letstalk.Uitil.place
import com.example.letstalk.model.Users
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserAdapter(val context: Context,val  users: ArrayList<Users>) :
    RecyclerView.Adapter<UserAdapter.userViewHolder>() {

    inner class userViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(users: Users, lMsg: String, lTime: String) {
            val ivImage = itemView.findViewById<ImageView>(R.id.iv_userImage)
            itemView.findViewById<TextView>(R.id.tv_name).text = users.name
            itemView.findViewById<TextView>(R.id.tv_lastMsg).text = lMsg
            itemView.findViewById<TextView>(R.id.tv_time).text = lTime
            Glide.with(context).load(users.userProfile).placeholder(place(context)).into(ivImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): userViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_chat, parent, false)
        return userViewHolder(view)
    }

    override fun onBindViewHolder(holder: userViewHolder, position: Int) {
        val user = users[position]
        val ref = FirebaseDatabase.getInstance().getReference("lastUpdate")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lMsg = snapshot.child("lastMsg").value.toString()
                val lTime = snapshot.child("lastMsgTime").value.toString()
                holder.bind(user, lMsg, lTime)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_LONG).show()
            }
        })
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("name", user.name)
            intent.putExtra("imageUrl", user.userProfile)
            intent.putExtra("uid", user.uid)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }
}