package com.misolova.medifora.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.misolova.medifora.R
import com.misolova.medifora.domain.Question
import kotlin.jvm.internal.Intrinsics
import kotlin.random.Random
import kotlin.random.asJavaRandom

class QuestionFormFragment : Fragment() {

    private lateinit var questionDetails: String
    private lateinit var question: Question

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_question_form, container, false)

        val btnSubmitQuestion = rootView.findViewById(R.id.btnSubmitQuestionForm) as MaterialButton?
        val questionContent = rootView.findViewById(R.id.etQuestionContentForm) as EditText?

        questionDetails = questionContent?.toString().toString()

        val rand  = Random

        question = Question(rand.nextLong(1, 500).toInt(), questionDetails, arrayListOf(), 0, "Current user", "12/09/2020")


        val action = QuestionFormFragmentDirections.actionQuestionFormFragmentToListOfAnswersToQuestionFragment(questionID = question.ID)

        btnSubmitQuestion?.setOnClickListener {
            Snackbar.make(rootView, "Question saved to DB", Snackbar.LENGTH_LONG).show()
            findNavController().navigate(action)
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