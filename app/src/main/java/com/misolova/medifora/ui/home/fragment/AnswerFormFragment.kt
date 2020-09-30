package com.misolova.medifora.ui.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.misolova.medifora.R
import com.misolova.medifora.domain.model.AnswerInfo
import com.misolova.medifora.ui.home.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_answer_form.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
@AndroidEntryPoint
@ExperimentalTime
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

        arguments?.apply {
            val quizID = AnswerFormFragmentArgs.fromBundle(this).questionID

            btnSubmitAnswer?.setOnClickListener {
                val answerContent = answerEditText?.text.toString()
                val answer = AnswerInfo("", answerContent = answerContent, answerAuthorID = viewModel.user?.uid!!, answerCreatedAt = System.currentTimeMillis(), questionID = quizID, votes = 0)
                val addingAnswer = viewModel.addAnswer(answer, quizID)
                while (addingAnswer.isActive) progressBarCreateAnswer.visibility = View.VISIBLE
                if (addingAnswer.isCompleted)  progressBarCreateAnswer.visibility = View.GONE
                Snackbar.make(view, "Answer saved to DB", Snackbar.LENGTH_LONG).show()
                val action =
                    AnswerFormFragmentDirections.actionAnswerFormFragmentToListOfAnswersToQuestionFragment(
                        questionID = quizID
                    )
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