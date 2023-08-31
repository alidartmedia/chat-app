package com.dartmedia.chatapp.presentation

import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import com.dartmedia.chatapp.data.ChatMQTTResponse
import com.dartmedia.chatapp.data.ConversationsItemShowChatResponse
import com.dartmedia.chatapp.databinding.ActivityChatBinding
import com.dartmedia.chatapp.presentation.chat.ChatAdapter
import com.dartmedia.chatapp.presentation.chat.ChatViewModel
import com.dartmedia.chatapp.presentation.dialog.LoadingDialog
import com.google.gson.Gson
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence


class ChatActivity : AppCompatActivity() {

    companion object {
        const val TOKEN = "token"
        const val ID_RECEIVER = "id receiver"
        const val NAME_RECEIVER = "name receiver"
        const val ID_SENDER = "id sender"

        private const val BROKER_URL = "tcp://192.168.1.103:1883"
    }

    private val binding: ActivityChatBinding by lazy { ActivityChatBinding.inflate(layoutInflater) }
    private val chatViewModel: ChatViewModel by viewModels()
    private val loadingDialog: LoadingDialog by lazy { LoadingDialog(this@ChatActivity) }
    private val chatAdapter: ChatAdapter by lazy { ChatAdapter() }
    private val memoryPersistence: MemoryPersistence by lazy { MemoryPersistence() }
    private val mqttClient: MqttClient by lazy { MqttClient(BROKER_URL, Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID), memoryPersistence) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        chatViewModel.showChat(intent.getStringExtra(TOKEN)!!, intent.getStringExtra(ID_RECEIVER)!!)

        setUpActionBar()
        connectMQTTBroker()
        initRecyclerView()
        sendChat()
        subscribeTopicMQTT()
        observeViewModel()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        disconnectMQTTBroker()
    }

    private fun setUpActionBar() {
        supportActionBar?.title = intent.getStringExtra(NAME_RECEIVER)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun connectMQTTBroker() {
        try {
            val mqttOptions = MqttConnectOptions()
            mqttOptions.isCleanSession = true
            mqttClient.connect(mqttOptions)
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    private fun disconnectMQTTBroker() {
        try {
            mqttClient.disconnect()
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    private fun initRecyclerView() {
        val mManager = LinearLayoutManager(this@ChatActivity, LinearLayoutManager.VERTICAL, false)
        mManager.reverseLayout = true

        chatAdapter.registerAdapterDataObserver(object : AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                binding.rvConversations.smoothScrollToPosition(0)
            }
        })

        binding.rvConversations.apply {
            layoutManager = mManager
            adapter = chatAdapter
            setHasFixedSize(true)
        }
    }

    private fun sendChat() {
        binding.btnSend.setOnClickListener {
            val msg = binding.etMessage.text.toString()

            if (msg.isNotEmpty()) {
                val json = "{\"idSender\": \"${intent.getStringExtra(ID_SENDER)}\", \"idReceiver\": \"${intent.getStringExtra(
                    ID_RECEIVER)}\", \"message\": \"$msg\"}"

                mqttClient.publish("chat/${intent.getStringExtra(ID_SENDER)}/${intent.getStringExtra(
                    ID_RECEIVER)}", MqttMessage(json.encodeToByteArray()))

                with(binding.etMessage) {
                    clearFocus()
                    setText("")
                }
            }
        }
    }

    private fun subscribeTopicMQTT() {
        mqttClient.subscribe("chat/${intent.getStringExtra(ID_SENDER)}/${intent.getStringExtra(
            ID_RECEIVER)}", 0) { _, message ->

            val data = Gson().fromJson(message.payload.decodeToString(), ChatMQTTResponse::class.java)
            val chat = ConversationsItemShowChatResponse(
                "waktu",
                true,
                data.message
            )
            chatAdapter.addData(chat)

        }

        mqttClient.subscribe("chat/${intent.getStringExtra(ID_RECEIVER)}/${intent.getStringExtra(
            ID_SENDER)}", 0) { _, message ->

            val data = Gson().fromJson(message.payload.decodeToString(), ChatMQTTResponse::class.java)
            val chat = ConversationsItemShowChatResponse(
                "waktu",
                false,
                data.message
            )
            chatAdapter.addData(chat)

        }
    }

    private fun observeViewModel() {
        with(chatViewModel) {
            result.observe(this@ChatActivity) {
                if (it.data?.conversations?.isEmpty() == true) {
                    binding.rvConversations.visibility = View.GONE
                } else {
                    binding.rvConversations.visibility = View.VISIBLE
                    chatAdapter.setData(it.data?.conversations)
                }
            }

            resultSendChat.observe(this@ChatActivity) {
                with(binding.etMessage) {
                    clearFocus()
                    setText("")
                }
            }

            error.observe(this@ChatActivity) {
                binding.rvConversations.visibility = View.GONE
                Toast.makeText(this@ChatActivity, it, Toast.LENGTH_SHORT).show()
            }

            isLoading.observe(this@ChatActivity) {
                if (it) {
                    if (!loadingDialog.isShowing) {
                        loadingDialog.show()
                    }
                    binding.rvConversations.visibility = View.GONE
                } else {
                    loadingDialog.dismiss()
                }
            }
        }
    }
}