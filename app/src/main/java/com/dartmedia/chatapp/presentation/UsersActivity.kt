package com.dartmedia.chatapp.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dartmedia.chatapp.databinding.ActivityUsersBinding
import com.dartmedia.chatapp.presentation.dialog.LoadingDialog
import com.dartmedia.chatapp.presentation.users.UsersAdapter
import com.dartmedia.chatapp.presentation.users.UsersViewModel

class UsersActivity : AppCompatActivity() {

    companion object {
        const val TOKEN = "token"
    }

    private val binding: ActivityUsersBinding by lazy { ActivityUsersBinding.inflate(layoutInflater) }
    private val usersViewModel: UsersViewModel by viewModels()
    private val usersAdapter: UsersAdapter by lazy { UsersAdapter() }
    private val loadingDialog: LoadingDialog by lazy { LoadingDialog(this@UsersActivity) }

    private var idSender: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setActionBar()
        initRecyclerView()
        observeViewModel()
        onClickItem()
    }

    override fun onResume() {
        super.onResume()
        val token = intent.getStringExtra(TOKEN)
        if (token != null) {
            usersViewModel.getListUsers(token)
        }
    }

    private fun setActionBar() {
        supportActionBar?.title = "Chat App"
    }

    private fun initRecyclerView() {
        binding.rvUsers.apply {
            layoutManager = LinearLayoutManager(this@UsersActivity, LinearLayoutManager.VERTICAL, false)
            adapter = usersAdapter
            setHasFixedSize(true)
        }
    }

    private fun observeViewModel() {
        with(usersViewModel) {
            result.observe(this@UsersActivity) {
                binding.rvUsers.visibility = View.VISIBLE
                usersAdapter.setData(it.data?.users)
                idSender = it.idUser
            }

            error.observe(this@UsersActivity) {
                binding.rvUsers.visibility = View.GONE
                Toast.makeText(this@UsersActivity, it, Toast.LENGTH_SHORT).show()
            }

            isLoading.observe(this@UsersActivity) {
                if (it) {
                    if (!loadingDialog.isShowing) {
                        loadingDialog.show()
                    }
                    binding.rvUsers.visibility = View.GONE
                } else {
                    loadingDialog.dismiss()
                }
            }
        }
    }

    private fun onClickItem() {
        usersAdapter.onItemClick = {
            val i = Intent(this@UsersActivity, ChatActivity::class.java)
            i.putExtra(ChatActivity.TOKEN, intent.getStringExtra(TOKEN))
            i.putExtra(ChatActivity.ID_RECEIVER, it.idUser)
            i.putExtra(ChatActivity.NAME_RECEIVER, it.nameUser)
            i.putExtra(ChatActivity.ID_SENDER, idSender)
            startActivity(i)
        }
    }
}