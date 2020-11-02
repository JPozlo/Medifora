package com.misolova.medifora.ui.home.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.misolova.medifora.R
import com.misolova.medifora.data.source.remote.FirebaseProfileService
import com.misolova.medifora.ui.home.viewmodel.MainViewModel
import com.misolova.medifora.util.Constants.KEY_USER_ID
import com.misolova.medifora.util.showSingleActionSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
@AndroidEntryPoint
@ExperimentalTime
class QuestionFormFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()
    @Inject
    lateinit var sharedPreferences: SharedPreferences

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
        val progressBarCreateQuestion = view.findViewById(R.id.progressBarCreateQuestion) as ProgressBar?

        var username = ""

        viewModel.getUserDetails().observe(viewLifecycleOwner, Observer {
            username = it.name
        })

        btnSubmitQuestion?.setOnClickListener {
            progressBarCreateQuestion?.visibility = View.VISIBLE
            val id = FirebaseProfileService.db.collection("users").document(getUserID()).collection("questions")
                .document().id
            val questionContent = questionEditText?.text.toString()
            viewModel.addQuestion(content = questionContent, questionId = id, userID = getUserID(), author = username)
                .addOnSuccessListener {
                    progressBarCreateQuestion?.visibility = View.GONE
                    notifyUser("Question successfully saved!")
                    val action =
                        QuestionFormFragmentDirections.actionQuestionFormFragmentToListOfAnswersToQuestionFragment(
                            questionID = id)
                    findNavController().navigate(action)
                }
                .addOnFailureListener {e ->
                    progressBarCreateQuestion?.visibility = View.GONE
                    notifyUser(e.localizedMessage!!)
                }

        }
    }

    private fun getUserID() = sharedPreferences.getString(KEY_USER_ID, "")!!

    private fun notifyUser(message: String) = requireView().showSingleActionSnackbar(message)

    companion object {
        @JvmStatic
        fun newInstance() =
            QuestionFormFragment().apply {
                QuestionFormFragment()
            }

        private const val TAG = "QUESTION FORM FRAGMENT"
    }
}