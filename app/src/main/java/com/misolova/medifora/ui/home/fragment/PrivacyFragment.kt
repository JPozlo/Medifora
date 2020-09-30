package com.misolova.medifora.ui.home.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.misolova.medifora.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PrivacyFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_privacy, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            PrivacyFragment().apply {
                PrivacyFragment()
            }

        private const val TAG = "PRIVACY FRAGMENT"
    }
}