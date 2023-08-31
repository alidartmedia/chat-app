package com.dartmedia.chatapp.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.dartmedia.chatapp.R
import com.dartmedia.chatapp.databinding.ActivityMainBinding
import com.dartmedia.chatapp.presentation.dialog.LoadingDialog
import com.dartmedia.chatapp.presentation.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val mainViewModel: MainViewModel by viewModels()
    private val loadingDialog: LoadingDialog by lazy { LoadingDialog(this@MainActivity) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        actionLogin()
        observeViewModel()
    }

    private fun actionLogin() {
        binding.btnLogin.setOnClickListener {
            val telp = binding.etTelp.text.toString()

            if (telp.isEmpty()) {
                Toast.makeText(this@MainActivity, "No. Telp cannot be empty", Toast.LENGTH_SHORT).show()
            }  else {
                mainViewModel.login(telp)
            }
        }
    }

    private fun observeViewModel() {
        with(mainViewModel) {
            result.observe(this@MainActivity) {
                val i = Intent(this@MainActivity, UsersActivity::class.java)
                i.putExtra(UsersActivity.TOKEN, it.data?.accessToken)
                startActivity(i)
            }

            error.observe(this@MainActivity) {
                Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
            }

            isLoading.observe(this@MainActivity) {
                if (it) {
                    if (!loadingDialog.isShowing) {
                        loadingDialog.show()
                    }
                } else {
                    loadingDialog.dismiss()
                }
            }
        }
    }
}