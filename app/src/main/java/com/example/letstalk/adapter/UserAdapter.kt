package com.example.letstalk.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.letstalk.Activity.ChatActivity
import com.example.letstalk.Activity.MainActivity
import com.example.letstalk.R
import com.example.letstalk.model.Users


class UserAdapter(mainActivity: MainActivity, users: ArrayList<Users>) :
    RecyclerView.Adapter<UserAdapter.userViewHolder>() {
    var cont: Context = mainActivity
    var users: ArrayList<Users>? = users

    inner class userViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(users: Users) {
            val ivImage = itemView.findViewById<ImageView>(R.id.iv_userImage)
            itemView.findViewById<TextView>(R.id.tv_name).text = users.name
            Glide.with(cont).load(users.imageUrl).into(ivImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): userViewHolder {
        val view = LayoutInflater.from(cont).inflate(R.layout.row_chat, parent, false)
        return userViewHolder(view)
    }

    override fun onBindViewHolder(holder: userViewHolder, position: Int) {
        val user = users!!.get(position)
        holder.bind(user)
        holder.itemView.setOnClickListener {
            val intent = Intent(cont, ChatActivity::class.java)
            intent.putExtra("name", user.name)
            intent.putExtra("imageUrl", user.imageUrl)
            intent.putExtra("uid", user.uid)
            cont.startActivity(intent)
        }
    }
    override fun getItemCount(): Int {
        return users!!.size
    }
}