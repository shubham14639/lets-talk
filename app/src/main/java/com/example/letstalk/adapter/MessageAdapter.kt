package com.example.letstalk.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.letstalk.R
import com.example.letstalk.model.Messages
import com.google.firebase.auth.FirebaseAuth

private const val ITEM_SEND: Int = 1
private const val ITEM_RECIVE: Int = 2

class MessageAdapter(var context: Context, var message: ArrayList<Messages>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val auth = FirebaseAuth.getInstance()

    class SendItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(messages: Messages) {
            itemView.findViewById<TextView>(R.id.tv_msg_send).text = messages.message
        }
    }

    class ReciveItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(messages: Messages) {
            itemView.findViewById<TextView>(R.id.tv_msg_recive).text = messages.message
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == ITEM_SEND) {
            val view = LayoutInflater.from(context).inflate(R.layout.message_sent, parent, false)
            return SendItemViewHolder(view)
        } else {
            val view = LayoutInflater.from(context).inflate(R.layout.message_recived, parent, false)
            return ReciveItemViewHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {


        val messages = message.get(position)
        if (auth.uid == messages.senderId) {
            Log.d("TESTLOG : itemview", ITEM_SEND.toString())
            return ITEM_SEND
        } else {
            Log.d("TESTLOG : itemview", ITEM_SEND.toString())
            return ITEM_RECIVE
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (getItemViewType(position) == ITEM_SEND) {
            (holder as SendItemViewHolder).bind(message[position])
        } else {
            (holder as ReciveItemViewHolder).bind(message[position])
        }
    }

    override fun getItemCount(): Int {
        return message.size
    }
}