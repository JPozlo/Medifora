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

class QuestionFormFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_question_form, container, false)

        val btnSubmitQuestion = rootView.findViewById(R.id.btnSubmitQuestionForm) as MaterialButton?

        btnSubmitQuestion?.setOnClickListener {
            Snackbar.make(rootView, "Question saved to DB", Snackbar.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_questionFormFragment_to_listOfAnswersToQuestionFragment)
        }

        return rootView
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            QuestionFormFragment().apply {
                QuestionFormFragment()
            }

        private const val TAG = "QUESTION FORM FRAGMENT"
    }
}