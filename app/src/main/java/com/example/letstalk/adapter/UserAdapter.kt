package com.example.letstalk.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.letstalk.MainActivity
import com.example.letstalk.R
import com.example.letstalk.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.security.spec.AlgorithmParameterSpec
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class UserAdapter(mainActivity: MainActivity, users: ArrayList<Users>) : RecyclerView.Adapter<UserAdapter.userViewHolder>() {
    var context: Context? = mainActivity
    var users: ArrayList<Users>? = users
    inner class userViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivImage = itemView.findViewById<ImageView>(R.id.iv_userImage)
        val tvName = itemView.findViewById<TextView>(R.id.tv_name)
        val tvlastMsg = itemView.findViewById<TextView>(R.id.tv_lastMsg)
        val tvlastTime = itemView.findViewById<TextView>(R.id.tv_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): userViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_chat, parent, false)
        return userViewHolder(view)
    }

    override fun onBindViewHolder(holder: userViewHolder, position: Int) {
        val user = users?.get(position)
        Log.d("TESTLOG : ",""+users)
        val senderId = FirebaseAuth.getInstance().uid
        val senderRoom = senderId + user!!.uid
        FirebaseDatabase.getInstance().reference
            .child("chats")
            .child(senderRoom)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        Log.d("TESTLOG : ", "on Data Changed ")
                        val lastMsg = snapshot.child("lastMsg").getValue(String::class.java)
                        val time = snapshot.child("lastMsgTime").getValue(Long::class.java)!!!!
                        val dateFormat = SimpleDateFormat("hh:mm a")
                        holder.tvlastMsg.text = lastMsg
                        holder.tvlastTime.text = dateFormat.format(Date(time))
                    } else {
                        holder.tvlastMsg.text = "Tap to Chat"
                        Log.d("TESTLOG : ", "Tap to Chat")
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
        holder.tvName.text = user.name
        Glide.with(context!!).load(user.imageUrl).into(holder.ivImage)
    }
    override fun getItemCount(): Int {
        return users!!.size
    }
}