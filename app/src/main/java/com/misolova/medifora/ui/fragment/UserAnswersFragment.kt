package com.misolova.medifora.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.misolova.medifora.R
import com.misolova.medifora.domain.Answer
import com.misolova.medifora.util.TestData
import com.misolova.medifora.util.adapters.AnswersListToQuestionAdapter
import com.misolova.medifora.util.adapters.UserAnswersAdapter
import kotlinx.android.synthetic.main.fragment_user_answers.*
import kotlinx.android.synthetic.main.fragment_user_answers_item.*
import timber.log.Timber

class UserAnswersFragment : Fragment() {

    private lateinit var adapter: UserAnswersAdapter
    private var userQuestionsArrayList: ArrayList<Answer> = TestData().userAnswersArrayList

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_user_answers, container, false)

        setupRecyclerview(rootView, listOf())

        val btnUserAnswerDetails = rootView.findViewById(R.id.btnUserAnswerDetails) as MaterialButton?

        btnUserAnswerDetails?.setOnClickListener {
            findNavController().navigate(R.id.action_userAnswersFragment_to_listOfAnswersToQuestionFragment)
        }

        adapter.notifyDataSetChanged()

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val answers = userQuestionsArrayList.filter {
            it.author == "Collins"
        }

        setupRecyclerview(view, answers)
        adapter.notifyDataSetChanged()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            UserAnswersFragment().apply {
                UserAnswersFragment()
            }

        private const val TAG = "USER ANSWERS FRAGMENT"
    }

    private fun setupRecyclerview(rootView: View?, answers: List<Answer>) {
        val recyclerView = rootView?.findViewById(R.id.recyclerViewUserAnswers) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = UserAnswersAdapter(answers, findNavController()){position ->
            val quizID = answers[position].questionID
            Timber.d("$TAG: Question ID is $quizID")
            val action = UserAnswersFragmentDirections.actionUserAnswersFragmentToListOfAnswersToQuestionFragment(quizID)
            findNavController().navigate(action)
        }
        recyclerView.adapter = adapter
    }
}