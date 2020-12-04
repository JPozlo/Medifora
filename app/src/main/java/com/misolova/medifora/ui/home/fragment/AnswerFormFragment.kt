package com.misolova.medifora.ui.home.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.firebase.Timestamp
import com.misolova.medifora.R
import com.misolova.medifora.data.source.remote.FirebaseProfileService
import com.misolova.medifora.domain.model.AnswerInfo
import com.misolova.medifora.ui.home.viewmodel.MainViewModel
import com.misolova.medifora.util.AnswerStatus
import com.misolova.medifora.util.Constants
import com.misolova.medifora.util.Constants.KEY_USER_ID
import com.misolova.medifora.util.showSingleActionSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_answer_form.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
@AndroidEntryPoint
@ExperimentalTime
class AnswerFormFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var sharedPreferences: SharedPreferences

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

            val name =   sharedPreferences.getString(Constants.KEY_USER_NAME, "")!!

            btnSubmitAnswer?.setOnClickListener {
                progressBarCreateAnswer?.visibility = View.VISIBLE
                val id = FirebaseProfileService.db.collection("users").document(getUserID()).collection("questions")
                    .document(quizID).collection("answers").document().id
                val answerContent = answerEditText?.text.toString()
                val userID = getUserID()
                val answer = AnswerInfo(answerId = id, answerContent = answerContent, answerAuthorID = userID, answerAuthor = name, status = AnswerStatus.PENDING.toString(), answerCreatedAt = Timestamp.now(), questionID = quizID, votes = 0)
                viewModel.addAnswer(answer = answer)
                    .addOnSuccessListener {
                        val action =
                            AnswerFormFragmentDirections.actionAnswerFormFragmentToListOfAnswersToQuestionFragment(
                                questionID = quizID
                            )
                        findNavController().navigate(action)
                        progressBarCreateAnswer?.visibility = View.GONE
                        notifyUser("Answer successfully saved!")
                    }
                    .addOnFailureListener {e ->
                        progressBarCreateAnswer?.visibility = View.GONE
                        notifyUser(e.localizedMessage!!)
                    }
            }
        }
    }

    private fun getUserID() = sharedPreferences.getString(KEY_USER_ID, "")!!

    private fun notifyUser(message: String) = requireView().showSingleActionSnackbar(message)

    companion object {
        @JvmStatic
        fun newInstance() =
            AnswerFormFragment().apply {
                AnswerFormFragment()
            }

        private const val TAG = "ANSWER FORM FRAGMENT"
    }
}