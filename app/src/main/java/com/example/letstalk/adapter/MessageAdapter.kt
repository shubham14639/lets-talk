package com.example.letstalk.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.letstalk.R
import com.example.letstalk.Uitil.place
import com.example.letstalk.model.Messages

class MessageAdapter(val context: Context, val messageList: ArrayList<Messages>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val MESSAGE_SEND: Int = 1
        const val MESSAGE_RECIVE: Int = 2
    }

    inner class SendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(messages: Messages) {
            val imageSend = itemView.findViewById<ImageView>(R.id.iv_pic_send)
            val userPic = itemView.findViewById<ImageView>(R.id.iv_userSend_profile)
            val msg = itemView.findViewById<TextView>(R.id.tv_msg_send)
            Glide.with(context).load(messages.userProfile).into(userPic)
            if (messages.message.equals("Picture")) {
                imageSend.visibility = View.VISIBLE
                msg.visibility = View.GONE
                Glide.with(context.applicationContext).load(messages.attachImage).placeholder(
                    place(context, 10f, 40f)
                )
                    .into(imageSend)
            } else {
                imageSend.visibility = View.GONE
                msg.visibility = View.VISIBLE
                msg.text = messages.message
            }
        }
    }

    inner class ReciveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(messages: Messages) {
            val imageRecive = itemView.findViewById<ImageView>(R.id.iv_pic_recive)
            val userPic = itemView.findViewById<ImageView>(R.id.iv_userRecive_profile)
            val msg = itemView.findViewById<TextView>(R.id.tv_msg_recive)
            Glide.with(context).load(messages.userProfile).into(userPic)
            if (messages.message.equals("Picture")) {
                imageRecive.visibility = View.VISIBLE
                msg.visibility = View.GONE
                Glide.with(context.applicationContext).load(messages.attachImage).placeholder(
                    place(context)
                )
                    .into(imageRecive)
            } else {
                imageRecive.visibility = View.GONE
                msg.visibility = View.VISIBLE
                msg.text = messages.message
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == MESSAGE_SEND) {
            val view = LayoutInflater.from(context).inflate(R.layout.message_sent, parent, false)
            SendViewHolder(view)
        } else {
            val view = LayoutInflater.from(context).inflate(R.layout.message_recived, parent, false)
            ReciveViewHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (messageList.get(position).isOutgoing) {
            MESSAGE_SEND
        } else
            MESSAGE_RECIVE
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val msg = messageList[position]
        if (holder is SendViewHolder) {
            holder.bind(msg)
        } else if (holder is ReciveViewHolder) {
            holder.bind(msg)
        }

        /*
if (holder.javaClass == SendViewHolder::class.java) {
    (holder as (SendViewHolder)).bind(messageList[position])
} else
    (holder as (ReciveViewHolder)).bind(messageList[position])*/
    }

    override fun getItemCount(): Int {
        return messageList.size
    }
}