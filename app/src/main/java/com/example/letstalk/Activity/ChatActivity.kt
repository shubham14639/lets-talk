package com.example.letstalk.Activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.letstalk.adapter.MessageAdapter
import com.example.letstalk.adapter.MessageReciveAdapter
import com.example.letstalk.databinding.ActivityChatBinding
import com.example.letstalk.model.Messages
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ChatActivity : AppCompatActivity() {

    lateinit var binding: ActivityChatBinding
    lateinit var database: FirebaseDatabase
    lateinit var refrence: DatabaseReference
    lateinit var auth: FirebaseAuth
    lateinit var messageAdapter: MessageAdapter
    lateinit var messageReciveAdapter: MessageReciveAdapter
    lateinit var messageList: ArrayList<Messages>
    lateinit var messageReciveList: ArrayList<Messages>

    lateinit var senderRoom: String
    lateinit var reciverRoom: String

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        messageList = ArrayList()
        messageReciveList = ArrayList()

        messageAdapter = MessageAdapter(this@ChatActivity, messageList)
        binding.chatRecyclerView.let {
            it.layoutManager = LinearLayoutManager(this@ChatActivity)
            it.adapter = messageAdapter
        }
        messageReciveAdapter = MessageReciveAdapter(this, messageReciveList)

        binding.chatRecyclerViewRecive.let {
            it.layoutManager = LinearLayoutManager(this@ChatActivity)
            it.adapter = messageReciveAdapter
        }

        val title = intent.getStringExtra("name")
        val reciverName = intent.getStringExtra("uid")
        val senderName = auth.uid

        supportActionBar!!.title = title
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        senderRoom = "$senderName --->>> $reciverName"
        reciverRoom = "$reciverName <<<--- $senderName"

        database.getReference()
            .child("chats")
            .child(senderRoom)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for (snap: DataSnapshot in snapshot.children) {
                        Log.d("TESTLOG", snap.key!!)
                        val messages: Messages? = snap.getValue(Messages::class.java)
                        messageList.add(messages!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ChatActivity, error.message, Toast.LENGTH_LONG).show()
                }
            })

        database.getReference()
            .child("chats")
            .child("hFYQ8e9i8DVK2KhZV8kWNH71oKv2 <<<--- WVBokomtuYamDtOPGZ6hERPa99e2")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageReciveList.clear()
                    for (snap: DataSnapshot in snapshot.children) {
                        Log.d("TESTLOG", snap.key!!)
                        val messages: Messages? = snap.getValue(Messages::class.java)
                        messageReciveList.add(messages!!)
                    }
                    messageReciveAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ChatActivity, error.message, Toast.LENGTH_LONG).show()
                }
            })

        binding.btnSend.setOnClickListener {
            val msgTxt = binding.etChatMsg.text.toString()
            if (msgTxt.isNotEmpty()) {
                val df = SimpleDateFormat("HH:mm:ss a")
                val currentTime: String = df.format(Calendar.getInstance().time)
                val message = Messages(senderName!!, msgTxt, currentTime, reciverName!!)
                binding.etChatMsg.setText("")
                database.getReference().child("chats")
                    .child(senderRoom)
                    .push()
                    .setValue(message).addOnCompleteListener {
                        if (it.isSuccessful) {
                            database.getReference().child("chats")
                                .child(reciverRoom)
                                .push()
                                .setValue(message).addOnCompleteListener {
                                }
                        }
                    }
            } else Toast.makeText(this, "type a message", Toast.LENGTH_LONG).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}


// Code of set Background Image of layout using glide
/*     Glide.with(this).load(imageUrl).into(object :
         CustomTarget<Drawable>() {
         override fun onLoadCleared(placeholder: Drawable?) {
         Log.d("TESTLOG","onload cleard")
         }

         override fun onResourceReady(
             resource: Drawable,
             transition: Transition<in Drawable>?
         ) {
             binding.mainLayout.background = resource
         }

     })*/