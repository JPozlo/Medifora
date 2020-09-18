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


class AnswerFormFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_answer_form, container, false)

        val btnSubmitAnswer = rootView.findViewById(R.id.btnSubmitAnswerForm) as MaterialButton?

        btnSubmitAnswer?.setOnClickListener {
            Snackbar.make(rootView, "Answer saved to DB", Snackbar.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_answerFormFragment_to_listOfAnswersToQuestionFragment)
        }

        return rootView
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            AnswerFormFragment().apply {
                AnswerFormFragment()
            }

        private const val TAG = "ANSWER FORM FRAGMENT"
    }
}