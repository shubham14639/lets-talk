package com.example.letstalk.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.letstalk.R
import com.example.letstalk.Uitil.place
import com.example.letstalk.model.Messages

class MessageAdapter(val context: Context, val messageList: ArrayList<Messages>) :
    RecyclerView.Adapter<MessageAdapter.SendViewHolder>() {

    companion object {
        const val MESSAGE_SEND: Int = 1
        const val MESSAGE_RECIVE: Int = 2
    }

    inner class SendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imageSend: ImageView = itemView.findViewById<ImageView>(R.id.iv_pic_send)
        val userPicSend: ImageView = itemView.findViewById<ImageView>(R.id.iv_userSend_profile)
        val sendMsg: TextView = itemView.findViewById<TextView>(R.id.tv_msg_send)
        val llSendLayout: LinearLayout = itemView.findViewById<LinearLayout>(R.id.llSendLayout)

        val imageRecive: ImageView = itemView.findViewById<ImageView>(R.id.iv_pic_recive)
        val userPicRecive: ImageView = itemView.findViewById<ImageView>(R.id.iv_userRecive_profile)
        val reciveMsg: TextView = itemView.findViewById<TextView>(R.id.tv_msg_recive)
        val llReciveLayout: LinearLayout =
            itemView.findViewById<LinearLayout>(R.id.ll_recivedLayout)


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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SendViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.message_sent, parent, false)
        return SendViewHolder(view)
    }

    override fun onBindViewHolder(holder: SendViewHolder, position: Int) {
        val msg = messageList.get(position)
        if (msg.isOutgoing) {
            holder.llReciveLayout.visibility=View.INVISIBLE
            holder.sendMsg.text = msg.message
            Glide.with(context).load(msg.userProfile).into(holder.userPicSend)

        } else {
            holder.llSendLayout.visibility=View.INVISIBLE
            holder.reciveMsg.text = msg.message
            Glide.with(context).load(msg.userProfile).into(holder.userPicRecive)
        }
    }

    /*
if (holder.javaClass == SendViewHolder::class.java) {
 (holder as (SendViewHolder)).bind(messageList[position])
} else
 (holder as (ReciveViewHolder)).bind(messageList[position])*/

    override fun getItemCount(): Int {
        return messageList.size
    }
}