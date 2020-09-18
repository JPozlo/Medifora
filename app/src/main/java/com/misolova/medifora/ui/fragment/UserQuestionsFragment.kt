package com.misolova.medifora.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.misolova.medifora.R
import com.misolova.medifora.domain.Question
import com.misolova.medifora.util.TestData
import com.misolova.medifora.util.adapters.UserAnswersAdapter
import com.misolova.medifora.util.adapters.UserQuestionsAdapter
import kotlinx.android.synthetic.main.fragment_user_questions.*
import kotlinx.android.synthetic.main.fragment_user_questions_item.*
import timber.log.Timber

class UserQuestionsFragment : Fragment() {

    private lateinit var adapter: UserQuestionsAdapter
    private var questionsArrayList: ArrayList<Question> = TestData().questionsArrayList

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_user_questions, container, false)

        setupRecyclerview(rootView, listOf())

        adapter.notifyDataSetChanged()

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val questions = questionsArrayList.filter {
            it.author == "Collins"
        }

        setupRecyclerview(view, questions)
        adapter.notifyDataSetChanged()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            UserQuestionsFragment().apply {
               UserQuestionsFragment()
            }

        private const val TAG = "USER QUESTIONS FRAGMENT"
    }

    private fun setupRecyclerview(rootView: View?, quizList: List<Question>) {
        val recyclerView = rootView?.findViewById(R.id.recyclerViewUserQuestions) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = UserQuestionsAdapter(quizList, findNavController()){position ->
            val questionID = quizList[position].ID
            Timber.d("$TAG: Question ID is $questionID")
            val action = UserQuestionsFragmentDirections.actionUserQuestionsFragmentToListOfAnswersToQuestionFragment(questionID)
            findNavController().navigate(action)
        }
        recyclerView.adapter = adapter
    }
}