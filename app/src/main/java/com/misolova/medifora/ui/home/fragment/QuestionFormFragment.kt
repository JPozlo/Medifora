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
import com.misolova.medifora.data.source.remote.FirebaseProfileService
import com.misolova.medifora.domain.model.QuestionInfo
import com.misolova.medifora.ui.home.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_question_form.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
@AndroidEntryPoint
@ExperimentalTime
class QuestionFormFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_question_form, container, false)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnSubmitQuestion = view.findViewById(R.id.btnSubmitQuestionForm) as MaterialButton?
        val questionEditText = view.findViewById(R.id.etQuestionContentForm) as EditText?

        val id = FirebaseProfileService.db.collection("users").document(FirebaseProfileService.fireUser?.uid!!).collection("questions")
            .document().id

        btnSubmitQuestion?.setOnClickListener {
            if(viewModel.user == null){
                Timber.d("$TAG: Error as user is null")
                Snackbar.make(view, "Error as user is null", Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val questionContent = questionEditText?.text.toString()
            val question = QuestionInfo(id, questionContent = questionContent, questionAuthorID = viewModel.user?.uid!!, questionCreatedAt = System.currentTimeMillis(), totalNumberOfAnswers = 0)
            val addingQuestion = viewModel.addQuestion(question)
            while (addingQuestion.isActive) progressBarCreateQuestion.visibility = View.VISIBLE
            if (addingQuestion.isCompleted)  progressBarCreateQuestion.visibility = View.GONE
            Snackbar.make(view, "Question saved to DB", Snackbar.LENGTH_LONG).show()
            val action =
                QuestionFormFragmentDirections.actionQuestionFormFragmentToListOfAnswersToQuestionFragment(
                    questionID = question.questionId
                )
            findNavController().navigate(action)
        }
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