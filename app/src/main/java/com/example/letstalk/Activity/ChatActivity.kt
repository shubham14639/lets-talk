package com.example.letstalk.Activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.letstalk.Uitil.*
import com.example.letstalk.adapter.MessageAdapter
import com.example.letstalk.databinding.ActivityChatBinding
import com.example.letstalk.model.Messages
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.ArrayList


class ChatActivity : AppCompatActivity() {

    lateinit var binding: ActivityChatBinding
    lateinit var database: FirebaseDatabase
    lateinit var auth: FirebaseAuth
    lateinit var storage: FirebaseStorage
    lateinit var reference: DatabaseReference
    lateinit var messageAdapter: MessageAdapter
    lateinit var messageList: ArrayList<Messages>
    lateinit var senderRoom: String
    lateinit var reciverRoom: String
    lateinit var reciverName: String

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        reference = database.getReference()
        storage = FirebaseStorage.getInstance()
        messageList = ArrayList()
        messageAdapter = MessageAdapter(this@ChatActivity, messageList)
        binding.chatRecylerview.let {
            it.layoutManager = LinearLayoutManager(this@ChatActivity)
            it.adapter = messageAdapter
        }

        val title = intent.getStringExtra("name")
        reciverName = intent.getStringExtra("uid")!!
        val imageUrl = intent.getStringExtra("imageUrl")
        val senderName = auth.uid

        supportActionBar!!.title = title
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        senderRoom = "$senderName --->>> $reciverName"
        reciverRoom = "$reciverName <<<--- $senderName"
        listenMessages()

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
        val dialog = progresDialog(this, "Uploading Image")
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            val uri = data?.data
            val storageReference =
                storage.getReference().child("chats").child(DateUitil.currentTime)
            storageReference.putFile(uri!!).addOnCompleteListener {
                if (it.isSuccessful) {
                    storageReference.downloadUrl.addOnCompleteListener {
                        dialog.dismiss()
                        val filePath = it.result.toString()
                        val senderName = auth.uid
                        val msgTxt = "Images"
                        sendMessages(senderName, msgTxt, reciverName, filePath)
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
        val message = Messages(senderName!!, msgTxt, DateUitil.currentTime, reciverName!!)
        message.imageUrl = imageUrl!!
        val lastMsg: HashMap<String, String> = HashMap()
        lastMsg.put("lastMsg", message.message)
        lastMsg.put("lastMsgTime", DateUitil.currentTime)
        AppLog.logger("Currend Date is ${DateUitil.currentTime}")
        database.getReference().child("lastUpdate").updateChildren(lastMsg as Map<String, Any>)
        binding.etChatMsg.setText("")
        reference.child("chats").child(senderRoom).child("Messages")
            .push()
            .setValue(message).addOnCompleteListener {
                reference.child("chats").child(reciverRoom).child("Messages")
                    .push()
                    .setValue(message).addOnCompleteListener {

                    }
            }
    }

    private fun listenMessages() {
        AppLog.logger("On listen message called$senderRoom")
        val ref = reference.child("chats").child(senderRoom).child("Messages")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                messageList.clear()
                for (snap in snapshot.children) {
                    AppLog.logger("snap value ${snap.value}")
                    val chat: Messages? = snap.getValue(Messages::class.java)
                    if (chat != null) {
                        if (chat.reciverId != chat.senderId) {
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
