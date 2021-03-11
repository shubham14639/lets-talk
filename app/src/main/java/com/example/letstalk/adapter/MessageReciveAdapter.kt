package com.example.letstalk.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.letstalk.R
import com.example.letstalk.model.Messages
import java.util.*


class MessageReciveAdapter(val context: Context, val messageList: ArrayList<Messages>) :
    RecyclerView.Adapter<MessageReciveAdapter.ReciveViewHolder>() {

    companion object {
        const val MESSAGE_SEND: Int = 1
        const val MESSAGE_RECIVE: Int = 2
    }

    inner class SendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(messages: Messages) {
            itemView.findViewById<TextView>(R.id.tv_msg_send).text = messages.message
        }
    }

    inner class ReciveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(messages: Messages) {
            itemView.findViewById<TextView>(R.id.tv_msg_recive).text = messages.message
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageReciveAdapter.ReciveViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.message_recived, parent, false)
        return ReciveViewHolder(view)
       /* if (viewType == MESSAGE_SEND) {
            val view = LayoutInflater.from(context).inflate(R.layout.message_sent, parent, false)
            return SendViewHolder(view)
        } else {
            val view = LayoutInflater.from(context).inflate(R.layout.message_recived, parent, false)
            return ReciveViewHolder(view)
        }*/
    }

/*    override fun getItemViewType(position: Int): Int {
        if (messageList.get(position).senderId==FirebaseAuth.getInstance().uid) {
            return MESSAGE_SEND
        } else
            return MESSAGE_RECIVE
    }*/

    override fun onBindViewHolder(holder: MessageReciveAdapter.ReciveViewHolder, position: Int) {

        holder.bind(messageList[position])

  /*      if (holder.javaClass==SendViewHolder::class.java) {
            (holder as (SendViewHolder)).bind(messageList[position])
        } else
            (holder as (ReciveViewHolder)).bind(messageList[position])*/
    }

    override fun getItemCount(): Int {
        return messageList.size
    }
}