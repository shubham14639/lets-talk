package com.example.letstalk.Activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.letstalk.R
import com.example.letstalk.adapter.MessageAdapter
import com.example.letstalk.databinding.ActivityChatBinding
import com.example.letstalk.model.Messages
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList

class ChatActivity : AppCompatActivity() {

    lateinit var binding: ActivityChatBinding
    lateinit var databaseReference: DatabaseReference
    lateinit var database: FirebaseDatabase
    lateinit var auth: FirebaseAuth
    lateinit var messageAdapter: MessageAdapter
    lateinit var messageList: ArrayList<Messages>
    lateinit var senderRoom: String
    lateinit var reciverRoom: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        messageList = ArrayList()
        messageAdapter = MessageAdapter(this, messageList)

        val name = intent.getStringExtra("name")
        val reciverUid = intent.getStringExtra("uid")
        val senderUid = auth.uid

        senderRoom = "$senderUid to $reciverUid"
        reciverRoom = "$reciverUid from $senderUid"

        supportActionBar!!.title = name
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        database.getReference().child("chats")
            .child(senderRoom).child("Messages").addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        messageList.clear()
                        for (snap: DataSnapshot in snapshot.children) {
                            val msg = snap.child("message").value.toString()
                            val timeStamp = snap.child("timeStamp").value.toString()
                            val messages = Messages(senderUid!!, msg, timeStamp)
                            messageList.add(messages)
                            messageAdapter = MessageAdapter(this@ChatActivity, messageList)
                            binding.chatRecyclerView.let {
                                it.layoutManager = LinearLayoutManager(this@ChatActivity)
                                it.adapter = messageAdapter
                            }
                            messageAdapter.notifyDataSetChanged()
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@ChatActivity, error.message, Toast.LENGTH_LONG).show()
                    }
                })

        binding.btnSend.setOnClickListener {
            val msgTxt = binding.etChatMsg.text.toString()
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)
            val messages =
                Messages(
                    senderId = senderUid.toString(),
                    message = msgTxt,
                    timeStamp = "$hour" + ":" + "$minute"
                )
            binding.etChatMsg.setText("")
            database.getReference().child("chats")
                .child(senderRoom)
                .child("Messages")
                .push()
                .setValue(messages).addOnCompleteListener {
                    database.getReference().child("chats")
                        .child(reciverRoom)
                        .child("Messages")
                        .push()
                        .setValue(messages).addOnCompleteListener {
                        }
                }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

}