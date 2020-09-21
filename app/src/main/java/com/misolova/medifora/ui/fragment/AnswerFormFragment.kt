package com.misolova.medifora.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.misolova.medifora.R
import com.misolova.medifora.data.source.local.entities.AnswerEntity
import com.misolova.medifora.data.source.local.entities.QuestionEntity
import com.misolova.medifora.ui.viewmodel.MainViewModel
import java.sql.Date


class AnswerFormFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_answer_form, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnSubmitAnswer = view.findViewById(R.id.btnSubmitAnswerForm) as MaterialButton?
        val answerEditText = view.findViewById(R.id.etAnswerContentForm) as EditText?
        val answerContent = answerEditText?.text.toString()

        arguments?.apply {
            var question: QuestionEntity
            val quizID = AnswerFormFragmentArgs.fromBundle(this).questionID
            viewModel.getQuestion(quizID).observe(viewLifecycleOwner, Observer {
                question = it
                viewModel.getUserInfo().observe(viewLifecycleOwner, Observer {
                    val user = it
                    val answer = AnswerEntity(answerContent, quizID, user.userID!!, Date(System.currentTimeMillis()))
                    btnSubmitAnswer?.setOnClickListener {
                        viewModel.addAnswer(answer)
                        Snackbar.make(view, "Answer saved to DB", Snackbar.LENGTH_LONG).show()
                        val action = AnswerFormFragmentDirections.actionAnswerFormFragmentToListOfAnswersToQuestionFragment(quizID)
                        findNavController().navigate(action)
                    }
                })
            })
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