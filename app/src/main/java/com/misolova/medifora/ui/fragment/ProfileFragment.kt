package com.misolova.medifora.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.misolova.medifora.R
import timber.log.Timber

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_profile, container, false)

        val btnUserAnswers = rootView.findViewById(R.id.btnProfileUserAnswers) as MaterialButton?
        val btnUserQuestions = rootView.findViewById(R.id.btnProfileUserQuestions) as MaterialButton?

        btnUserAnswers?.setOnClickListener {
            Timber.d("$TAG: User Answers Button clicked")
            findNavController().navigate(R.id.action_profileFragment_to_userAnswersFragment)
        }

        btnUserQuestions?.setOnClickListener {
            Timber.d("$TAG: User Questions Button clicked")
            findNavController().navigate(R.id.action_profileFragment_to_userQuestionsFragment)
        }

        return rootView
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ProfileFragment().apply {
                ProfileFragment()
            }

        private const val TAG = "PROFILE FRAGMENT"
    }
}