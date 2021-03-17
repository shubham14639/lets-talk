package com.example.letstalk.Fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.letstalk.Uitil.*
import com.example.letstalk.adapter.MessageAdapter
import com.example.letstalk.databinding.FragmentChatBinding
import com.example.letstalk.model.Messages
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.Map


class ChatFragment : Fragment() {
    private var _binding: FragmentChatBinding? = null
    lateinit var database: FirebaseDatabase
    lateinit var auth: FirebaseAuth
    lateinit var storage: FirebaseStorage
    lateinit var messageAdapter: MessageAdapter
    lateinit var messageList: ArrayList<Messages>
    lateinit var senderRoom: String
    lateinit var reciverRoom: String
    lateinit var reciverName: String
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
        messageList = ArrayList()
        progresDialog(requireContext(), "Uploading Image")
        messageAdapter = MessageAdapter(requireActivity(), messageList)
        binding.chatRecylerview.let {
            it.layoutManager = LinearLayoutManager(requireActivity())
            it.adapter = messageAdapter
        }

        val intent = Intent()
        val title = intent.getStringExtra("name")
        reciverName = intent.getStringExtra("uid")!!
        val imageUrl = intent.getStringExtra("imageUrl")
        val senderName = auth.uid

        (activity as AppCompatActivity?)!!.supportActionBar?.title = title
        (activity as AppCompatActivity?)!!.onSupportNavigateUp()

        senderRoom = "$senderName --->>> $reciverName"
        reciverRoom = "$reciverName <<<--- $senderName"

        binding.btnSend.setOnClickListener {
            val msgTxt = binding.etChatMsg.text.toString()
            if (msgTxt.isNotEmpty()) {
                sendMessages(senderName, msgTxt, reciverName, imageUrl)
            } else makeToast(requireContext(), "type a message")
        }
        binding.ivAttach.setOnClickListener {
            val intent = Intent()
            intent.setAction(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            startActivityForResult(intent, 100)
        }
    }

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
        database.getReference().child("Messages")
            .push()
            .setValue(message).addOnCompleteListener {
            }
    }

    @SuppressLint("SimpleDateFormat")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == AppCompatActivity.RESULT_OK) {
            val uri = data?.data
            val storageReference =
                storage.getReference().child("chats").child(DateUitil.currentTime)
            storageReference.putFile(uri!!).addOnCompleteListener {
                if (it.isSuccessful) {
                    storageReference.downloadUrl.addOnCompleteListener {

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

}