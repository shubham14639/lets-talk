package com.example.letstalk.Activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.letstalk.adapter.MessageAdapter
import com.example.letstalk.databinding.ActivityChatBinding
import com.example.letstalk.model.Messages
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ChatActivity : AppCompatActivity() {

    lateinit var binding: ActivityChatBinding
    lateinit var database: FirebaseDatabase
    lateinit var auth: FirebaseAuth
    lateinit var storage: FirebaseStorage
    lateinit var messageAdapter: MessageAdapter
    lateinit var messageList: ArrayList<Messages>
    lateinit var dialog:ProgressDialog
    lateinit var senderRoom: String
    lateinit var reciverRoom: String

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
        messageList = ArrayList()
        dialog= ProgressDialog(this)
        dialog.setMessage("Uploading Image")
        dialog.setCancelable(true)
        messageAdapter = MessageAdapter(this@ChatActivity, messageList)
        binding.chatRecylerview.let {
            it.layoutManager = LinearLayoutManager(this@ChatActivity)
            it.adapter = messageAdapter
        }
        val pos = binding.chatRecylerview.adapter!!.itemCount - 1

        val title = intent.getStringExtra("name")
        val reciverName = intent.getStringExtra("uid")
        val imageUrl = intent.getStringExtra("imageUrl")
        val senderName = auth.uid

        supportActionBar!!.title = title
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        senderRoom = "$senderName --->>> $reciverName"
        reciverRoom = "$reciverName <<<--- $senderName"


        binding.btnSend.setOnClickListener {
            val msgTxt = binding.etChatMsg.text.toString()
            if (msgTxt.isNotEmpty()) {
                sendMessages(senderName, msgTxt, reciverName, imageUrl)
            } else Toast.makeText(this, "type a message", Toast.LENGTH_LONG).show()
        }


        binding.ivAttach.setOnClickListener {
            val intent = Intent()
            intent.setAction(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            startActivityForResult(intent, 100)
        }
    }

    @SuppressLint("SimpleDateFormat")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        dialog.show()
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            val uri = data?.data
            val df = SimpleDateFormat("HH:mm:ss a")
            val currentTime: String = df.format(Calendar.getInstance().time)
            val storageReference = storage.getReference().child("chats").child(currentTime)
            storageReference.putFile(uri!!).addOnCompleteListener {
                if (it.isSuccessful) {
                    storageReference.downloadUrl.addOnCompleteListener {
                        dialog.dismiss()
                        val filePath = it.result
                    }
                }
            }
        }
    }

    override fun onStart() {
        listenMessages()
        super.onStart()
    }

    @SuppressLint("SimpleDateFormat")
    private fun sendMessages(
        senderName: String?,
        msgTxt: String,
        reciverName: String?,
        imageUrl: String?
    ) {
        val df = SimpleDateFormat("HH:mm:ss a")
        val currentTime: String = df.format(Calendar.getInstance().time)
        val message = Messages(senderName!!, msgTxt, currentTime, reciverName!!)

        val lastMsg: HashMap<String, String> = HashMap()
        lastMsg.put("lastMsg", message.message)
        lastMsg.put("lastMsgTime", currentTime)
        database.getReference().child("lastUpdate").updateChildren(lastMsg as Map<String, Any>)
        binding.etChatMsg.setText("")
        database.getReference().child("Messages")
            .push()
            .setValue(message).addOnCompleteListener {
            }
    }

    private fun listenMessages() {
        val ref = database.getReference().child("Messages")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                messageList.clear()
                for (snap in snapshot.children) {
                    val chat: Messages? = snap.getValue(Messages::class.java)
                    if (chat != null) {
                        if (chat.reciverId == FirebaseAuth.getInstance().uid) {
                            messageList.add(chat)
                        } else
                            messageList.add(chat)
                        messageAdapter.notifyDataSetChanged()
                        binding.chatRecylerview.smoothScrollToPosition(messageList.size)

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
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

/*        database.getReference()
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
            })*/