package com.misolova.medifora.ui.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.misolova.medifora.R
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_profile, container, false)

        val btnUserAnswers = rootView.findViewById(R.id.btnProfileUserAnswers) as MaterialButton?
        val btnUserQuestions = rootView.findViewById(R.id.btnProfileUserQuestions) as MaterialButton?
        val btnUserPendingAnswers = rootView.findViewById(R.id.btnProfileUserPendingAnswers) as MaterialButton?

        btnUserAnswers?.setOnClickListener {
            Timber.d("$TAG: User Answers Button clicked")
            findNavController().navigate(R.id.action_profileFragment_to_userAnswersFragment)
        }

        btnUserQuestions?.setOnClickListener {
            Timber.d("$TAG: User Questions Button clicked")
            findNavController().navigate(R.id.action_profileFragment_to_userQuestionsFragment)
        }

        btnUserPendingAnswers?.setOnClickListener {
            Timber.d("$TAG: User Pending Answers Button clicked")
            findNavController().navigate(R.id.action_profileFragment_to_pendingAnswersFragment)
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