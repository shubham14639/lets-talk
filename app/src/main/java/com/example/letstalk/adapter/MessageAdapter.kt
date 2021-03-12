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
import com.example.letstalk.R
import com.example.letstalk.model.Messages
import com.example.letstalk.model.Users
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class MessageAdapter(val context: Context, val messageList: ArrayList<Messages>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val MESSAGE_SEND: Int = 1
        const val MESSAGE_RECIVE: Int = 2
        val users = Users()
    }

    inner class SendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(messages: Messages) {
            itemView.findViewById<TextView>(R.id.tv_msg_send).text = messages.message
            val imageView = itemView.findViewById<ImageView>(R.id.imageViewSend)
            Glide.with(context).load(users.imageUrl).placeholder(R.drawable.avatar).into(imageView)
        }
    }

    inner class ReciveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(messages: Messages) {
            itemView.findViewById<TextView>(R.id.tv_msg_recive).text = messages.message
            val imageView = itemView.findViewById<ImageView>(R.id.imageViewRecive)
            Glide.with(context).load(users.imageUrl).placeholder(R.drawable.avatar).into(imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == MESSAGE_SEND) {
            val view = LayoutInflater.from(context).inflate(R.layout.message_sent, parent, false)
            return SendViewHolder(view)
        } else {
            val view = LayoutInflater.from(context).inflate(R.layout.message_recived, parent, false)
            return ReciveViewHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (messageList.get(position).senderId == FirebaseAuth.getInstance().uid) {
            return MESSAGE_SEND
        } else
            return MESSAGE_RECIVE
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.javaClass == SendViewHolder::class.java) {
            (holder as (SendViewHolder)).bind(messageList[position])
        } else
            (holder as (ReciveViewHolder)).bind(messageList[position])
    }

    override fun getItemCount(): Int {
        return messageList.size
    }
}