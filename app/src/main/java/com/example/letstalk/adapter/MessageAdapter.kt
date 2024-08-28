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

class MessageAdapter(
    private val context: Context,
    private val messageList: ArrayList<Messages>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val MESSAGE_SEND = 1
        private const val MESSAGE_RECEIVE = 2
    }

    inner class SendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(message: Messages) {
            val imageSend = itemView.findViewById<ImageView>(R.id.iv_pic_send)
            val userPic = itemView.findViewById<ImageView>(R.id.iv_userSend_profile)
            val msg = itemView.findViewById<TextView>(R.id.tv_msg_send)

            Glide.with(context).load(message.userProfile).into(userPic)

            if (message.message == "Picture") {
                imageSend.visibility = View.VISIBLE
                msg.visibility = View.GONE
                Glide.with(context)
                    .load(message.attachImage)
                    // Use a placeholder resource or method
                    .into(imageSend)
            } else {
                imageSend.visibility = View.GONE
                msg.visibility = View.VISIBLE
                msg.text = message.message
            }
        }
    }

    inner class ReceiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(message: Messages) {
            val imageReceive = itemView.findViewById<ImageView>(R.id.iv_pic_recive)
            val userPic = itemView.findViewById<ImageView>(R.id.iv_userRecive_profile)
            val msg = itemView.findViewById<TextView>(R.id.tv_msg_recive)

            Glide.with(context).load(message.userProfile).into(userPic)

            if (message.message == "Picture") {
                imageReceive.visibility = View.VISIBLE
                msg.visibility = View.GONE
                Glide.with(context)
                    .load(message.attachImage) // Use a placeholder resource or method
                    .into(imageReceive)
            } else {
                imageReceive.visibility = View.GONE
                msg.visibility = View.VISIBLE
                msg.text = message.message
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.d("MessageAdapter", "Creating ViewHolder for viewType $viewType")
        return when (viewType) {
            MESSAGE_SEND -> {
                Log.d("MessageAdapter", "Creating SendViewHolder")
                val view =
                    LayoutInflater.from(context).inflate(R.layout.message_sent, parent, false)
                SendViewHolder(view)
            }

            MESSAGE_RECEIVE -> {
                Log.d("MessageAdapter", "Creating ReceiveViewHolder")
                val view =
                    LayoutInflater.from(context).inflate(R.layout.message_recived, parent, false)
                ReceiveViewHolder(view)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        val isOutgoing = messageList[position].isOutgoing
        Log.d("MessageAdapter", "Item at position $position isOutgoing: $isOutgoing")
        return if (isOutgoing) MESSAGE_SEND else MESSAGE_RECEIVE
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messageList[position]
        Log.d("MessageAdapter", "Binding message at position $position")
        when (holder) {
            is SendViewHolder -> holder.bind(message)
            is ReceiveViewHolder -> holder.bind(message)
        }
    }

    override fun getItemCount(): Int = messageList.size

    fun addMessage(message: Messages) {
        messageList.add(message)
        notifyItemInserted(messageList.size - 1)
    }
}
