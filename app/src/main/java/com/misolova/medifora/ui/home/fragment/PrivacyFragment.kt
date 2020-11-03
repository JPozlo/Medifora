package com.misolova.medifora.ui.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.misolova.medifora.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_privacy.*

@AndroidEntryPoint
class PrivacyFragment : Fragment() {
    companion object {
        @JvmStatic
        fun newInstance() =
            PrivacyFragment().apply {
                PrivacyFragment()
            }

        private const val TAG = "PRIVACY FRAGMENT"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_privacy, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvLegalTerms?.setOnClickListener {
            findNavController().navigate(R.id.termsFragment)
        }

        tvPrivacyPolicy?.setOnClickListener {
            findNavController().navigate(R.id.privacyPolicyFragment)
        }
    }


}