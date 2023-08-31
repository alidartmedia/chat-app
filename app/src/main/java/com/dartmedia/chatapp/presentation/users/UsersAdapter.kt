package com.dartmedia.chatapp.presentation.users

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dartmedia.chatapp.R
import com.dartmedia.chatapp.data.UsersItemListUserResponse
import com.dartmedia.chatapp.databinding.RvListUserBinding

class UsersAdapter : RecyclerView.Adapter<UsersAdapter.ListViewHolder>() {
    private var listUser = ArrayList<UsersItemListUserResponse?>()
    var onItemClick: ((UsersItemListUserResponse) -> Unit)? = null

    fun setData(newList: List<UsersItemListUserResponse?>?) {
        if (newList == null) return
        listUser.clear()
        listUser.addAll(newList)
        notifyDataSetChanged()
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = RvListUserBinding.bind(itemView)

        fun bind(data: UsersItemListUserResponse) {
            with(binding) {
                tvNameUsers.text = data.nameUser
            }
        }

        init {
            binding.ivMessage.setOnClickListener {
                onItemClick?.invoke(listUser[adapterPosition]!!)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.rv_list_user, parent, false))
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = listUser[position]
        holder.bind(data!!)
    }

    override fun getItemCount(): Int {
        return listUser.size
    }
}