package com.example.letstalk.Activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.letstalk.Services.FirebaseService
import com.example.letstalk.Services.RetrofitInstance
import com.example.letstalk.Uitil.*
import com.example.letstalk.adapter.MessageAdapter
import com.example.letstalk.databinding.ActivityChatBinding
import com.example.letstalk.model.Messages
import com.example.letstalk.model.NotificationData
import com.example.letstalk.model.PushNotification
import com.example.letstalk.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


const val TOPIC = "/topics/myTopic2"

class ChatActivity : AppCompatActivity() {

    lateinit var binding: ActivityChatBinding
    lateinit var database: FirebaseDatabase
    lateinit var auth: FirebaseAuth
    lateinit var storage: FirebaseStorage
    lateinit var messageAdapter: MessageAdapter
    lateinit var messageList: ArrayList<Messages>
    lateinit var senderRoom: String
    lateinit var reciverRoom: String
    lateinit var reciverName: String
    lateinit var filePath: String
    lateinit var users: Users

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
        messageList = ArrayList()
        messageAdapter = MessageAdapter(this@ChatActivity, messageList)
        binding.chatRecylerview.let {
            it.layoutManager = LinearLayoutManager(this@ChatActivity)
            it.adapter = messageAdapter
        }
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        FirebaseService.sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            FirebaseService.token = it.token
            AppLog.logger("Token : ${it.token}")
        }
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)

        val title = intent.getStringExtra("name")
        reciverName = intent.getStringExtra("uid")!!
        val senderName = auth.uid

        supportActionBar!!.title = title
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        senderRoom = "$senderName --->>> $reciverName"
        reciverRoom = "$reciverName <<<--- $senderName"

        DataClass.userDetails {
            users = it
        }

        binding.btnSend.setOnClickListener {
            val msgTxt = binding.etChatMsg.text.toString()
            if (msgTxt.isNotEmpty()) {
                sendMessages(
                    senderName = senderName,
                    msgTxt = msgTxt,
                    reciverName = reciverName,
                    filePath = "File doest not Attached"
                )
                PushNotification(
                    NotificationData(users.name, msgTxt),
                    TOPIC
                ).also { sendNotificaton(it) }

            } else Toast.makeText(this, "type a message", Toast.LENGTH_LONG).show()
        }

        binding.ivAttach.setOnClickListener {
            val intent = Intent()
            intent.setAction(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            startActivityForResult(intent, 100)
        }
    }

    private fun sendNotificaton(notification: PushNotification) =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.postNotification(notification)
                if (response.isSuccessful) {
                    AppLog.logger("Response Success:${response.body()}")
                } else {
                    AppLog.logger("Response Error:${response.errorBody().toString()}")
                }
            } catch (e: Exception) {
                AppLog.logger("Notification Exception :${e.message}")
            }
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val dialog = progresDialog(this, "Uploading Image")
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            val uri = data?.data
            if (uri != null) {
                val storageReference =
                    storage.getReference().child("chats").child(DateUitil.currentTime)
                storageReference.putFile(uri).addOnCompleteListener {
                    if (it.isSuccessful) {
                        storageReference.downloadUrl.addOnCompleteListener {
                            dialog.dismiss()
                            filePath = it.result.toString()
                            sendMessages(
                                senderName = auth.uid,
                                msgTxt = "Picture",
                                reciverName = reciverName,
                                filePath = filePath
                            )
                        }
                    }
                }
            } else makeToast(this, "file does not attached")
        }
    }

    override fun onStart() {
        listenMessages()
        super.onStart()
    }

    override fun onResume() {
        listenMessages()
        super.onResume()
    }

    override fun onStop() {
        listenMessages()
        super.onStop()
    }

    private fun sendMessages(
        senderName: String?,
        msgTxt: String,
        reciverName: String?,
        filePath: String
    ) {
        val message = Messages(
            senderId = senderName!!,
            message = msgTxt,
            timeStamp = DateUitil.currentTime,
            reciverId = reciverName!!,
            attachImage = filePath,
            userProfile = users.userProfile,
            isOutgoing = true
        )
        binding.etChatMsg.setText("")
        val lastMsg: HashMap<String, String> = HashMap()
        lastMsg.put("lastMsg", message.message)
        lastMsg.put("lastMsgTime", DateUitil.currentTime)
        database.getReference().child("users").child(reciverName).updateChildren(lastMsg as Map<String, Any>)
        messageList.add(message)

        database.getReference().child("Messages").child(reciverName)
            .push()
            .setValue(message).addOnCompleteListener {
            }
    }

    private fun listenMessages() {
        val ref = database.getReference().child("Messages").child(auth.uid.toString())
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("MYTAG", "onDataChange: before clear $messageList")
                messageList.clear()
                for (snap in snapshot.children) {
                    val chat: Messages? = snap.getValue(Messages::class.java)
                    if (chat != null) {
                        Log.d("MYTAG", "chat reciverid  ${chat.reciverId}   uid ${FirebaseAuth.getInstance().uid}")
                        if (chat.reciverId == FirebaseAuth.getInstance().uid) {
                            messageList.add(chat)
                        } else
                            messageList.add(chat)
                        messageList.forEach{
                            Log.d("MYTAG", "message list for each: ${it.message}")
                        }
                        Log.d("MYTAG", "message list after if else  $messageList")
                        messageAdapter = MessageAdapter(this@ChatActivity, messageList)

                        binding.chatRecylerview.let {
                            it.layoutManager = LinearLayoutManager(this@ChatActivity)
                            it.adapter = messageAdapter
                        }
                        messageAdapter.notifyDataSetChanged()
                        binding.chatRecylerview.smoothScrollToPosition(messageList.size)
                    }else{
                        Log.d("MYTAG", "chat is null  $messageList")

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    applicationContext,
                    "Error in Database ${error.message}",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}


fun send(
    database: FirebaseDatabase,
    senderRoom: String,
    messageList: ArrayList<Messages>,
    messageAdapter: MessageAdapter,
    context: Context
) {
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
                Toast.makeText(context, error.message, Toast.LENGTH_LONG).show()
            }
        })
}