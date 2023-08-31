package com.dartmedia.chatapp.presentation.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dartmedia.chatapp.R
import com.dartmedia.chatapp.data.ConversationsItemShowChatResponse
import com.dartmedia.chatapp.databinding.RvListConversationBinding

class ChatAdapter : RecyclerView.Adapter<ChatAdapter.ListViewHolder>() {
    private var listChat = ArrayList<ConversationsItemShowChatResponse?>()

    fun setData(newList: List<ConversationsItemShowChatResponse?>?) {
        if (newList == null) return
        listChat.clear()
        listChat.addAll(newList)
        notifyDataSetChanged()
    }

    fun addData(newData: ConversationsItemShowChatResponse?) {
        listChat.add(0, newData)
        notifyItemInserted(0)
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = RvListConversationBinding.bind(itemView)

        fun bind(data: ConversationsItemShowChatResponse) {
            with(binding) {
                if (data.isSender == true) {
                    cardOtherUser.visibility = View.GONE
                    cardUser.visibility = View.VISIBLE

                    tvUser.text = data.message
                } else {
                    cardOtherUser.visibility = View.VISIBLE
                    cardUser.visibility = View.GONE

                    tvOtherUser.text = data.message
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.rv_list_conversation, parent, false))
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = listChat[position]
        holder.bind(data!!)
    }

    override fun getItemCount(): Int {
        return listChat.size
    }
}