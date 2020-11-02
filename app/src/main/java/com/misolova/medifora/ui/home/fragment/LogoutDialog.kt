package com.misolova.medifora.ui.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.misolova.medifora.R

class LogoutDialog: DialogFragment() {

    companion object{
        const val TAG = "LOGOUT_DIALOG"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootview = inflater.inflate(R.layout.fragment_logout_dialog, container, false)
        return rootview
    }

}