package com.example.letstalk.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.letstalk.R
import com.example.letstalk.model.Messages

class MessageAdapterTest(val context: Context, val messageList: ArrayList<Messages>) :
    RecyclerView.Adapter<MessageAdapterTest.TestViewHolder>() {

    inner class TestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(messages: Messages) {
            itemView.findViewById<TextView>(R.id.tv_msg_send).text=messages.message
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.message_sent, parent, false)
        return TestViewHolder(view)
    }

    override fun onBindViewHolder(holder: TestViewHolder, position: Int) {
        holder.bind(messageList.get(position))
    }

    override fun getItemCount(): Int {
       return messageList.size
    }

}