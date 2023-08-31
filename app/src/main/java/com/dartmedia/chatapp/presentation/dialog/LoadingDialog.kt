package com.dartmedia.chatapp.presentation.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.dartmedia.chatapp.databinding.DialogLoadingBinding

class LoadingDialog(mContext: Context) : Dialog(mContext) {

    private val binding: DialogLoadingBinding by lazy { DialogLoadingBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCanceledOnTouchOutside(false)
    }

}