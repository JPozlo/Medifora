package com.misolova.medifora.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.misolova.medifora.R
import com.misolova.medifora.data.source.local.entities.QuestionEntity
import com.misolova.medifora.domain.model.Question
import com.misolova.medifora.ui.viewmodel.MainViewModel
import java.sql.Date
import java.util.*
import kotlin.random.Random

class QuestionFormFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var questionDetails: String
    private lateinit var question: QuestionEntity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_question_form, container, false)

        val btnSubmitQuestion = rootView.findViewById(R.id.btnSubmitQuestionForm) as MaterialButton?
        val questionContent = rootView.findViewById(R.id.etQuestionContentForm) as EditText?

        questionDetails = questionContent?.toString().toString()
        viewModel.getUserInfo().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            val user = it
            question =
                QuestionEntity(questionDetails, user.userID!!, Date(System.currentTimeMillis()))

            val action =
                QuestionFormFragmentDirections.actionQuestionFormFragmentToListOfAnswersToQuestionFragment(
                    questionID = question.questionID!!
                )

            btnSubmitQuestion?.setOnClickListener {
                viewModel.addQuestion(question)
                Snackbar.make(rootView, "Question saved to DB", Snackbar.LENGTH_LONG).show()
                findNavController().navigate(action)
            }
        })
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