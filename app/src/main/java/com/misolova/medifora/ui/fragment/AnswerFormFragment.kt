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
        return inflater.inflate(R.layout.fragment_answer_form, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.apply {
            val quizID = AnswerFormFragmentArgs.fromBundle(this).questionID
            val btnSubmitAnswer = view.findViewById(R.id.btnSubmitAnswerForm) as MaterialButton?

            btnSubmitAnswer?.setOnClickListener {
                Snackbar.make(view, "Answer saved to DB", Snackbar.LENGTH_LONG).show()
                val action = AnswerFormFragmentDirections.actionAnswerFormFragmentToListOfAnswersToQuestionFragment(quizID)
                findNavController().navigate(action)
            }
        }
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