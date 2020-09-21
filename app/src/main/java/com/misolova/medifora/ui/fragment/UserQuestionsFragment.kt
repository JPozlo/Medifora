package com.misolova.medifora.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.misolova.medifora.R
import com.misolova.medifora.data.source.local.entities.QuestionAnswerEntity
import com.misolova.medifora.data.source.local.entities.UserQuestionAnswersEntity
import com.misolova.medifora.domain.model.Question
import com.misolova.medifora.ui.viewmodel.MainViewModel
import com.misolova.medifora.util.adapters.UserQuestionsAdapter
import timber.log.Timber

class UserQuestionsFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var adapter: UserQuestionsAdapter
    private lateinit var userQuestionsAnswersList: List<UserQuestionAnswersEntity>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_user_questions, container, false)

        setupRecyclerViewData(listOf())

        val userQuestions = viewModel.userWithQuestionsAndAnswers
        userQuestions.observe(viewLifecycleOwner, Observer {
            userQuestionsAnswersList = it
            if(it.count() > 0){
                setupRecyclerViewData(userQuestionsAnswersList)
            } else {
                noRecyclerViewData()
            }
        })

        return rootView
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            UserQuestionsFragment().apply {
               UserQuestionsFragment()
            }

        private const val TAG = "USER QUESTIONS FRAGMENT"
    }

    private fun noRecyclerViewData(){
        return adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver(){
            override fun onChanged() {
                super.onChanged()
                if(adapter.itemCount == 0){
                    Toast.makeText(activity, "You don't have any questions yet", Toast.LENGTH_SHORT).show()
                    setupRecyclerview(view, listOf())
                    adapter.notifyDataSetChanged()
                }
            }
        })
    }

    private fun setupRecyclerViewData(quizList: List<UserQuestionAnswersEntity>) {
        setupRecyclerview(view, quizList)
        adapter.notifyDataSetChanged()
    }

    private fun setupRecyclerview(rootView: View?, userQuizList: List<UserQuestionAnswersEntity>) {
        val recyclerView = rootView?.findViewById(R.id.recyclerViewUserQuestions) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        val userID = 0
        adapter = UserQuestionsAdapter(userID, userQuizList){position ->
            val questionID = userQuizList[position].questions[0].question.questionID
            Timber.d("$TAG: Question ID is $questionID")
            val action = UserQuestionsFragmentDirections.actionUserQuestionsFragmentToListOfAnswersToQuestionFragment(questionID!!)
            findNavController().navigate(action)
        }
        recyclerView.adapter = adapter
    }
}