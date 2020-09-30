package com.misolova.medifora.ui.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.misolova.medifora.R
import com.misolova.medifora.domain.model.QuestionInfo
import com.misolova.medifora.ui.home.viewmodel.MainViewModel
import com.misolova.medifora.util.adapters.UserQuestionsAdapter
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import kotlin.time.ExperimentalTime

@AndroidEntryPoint
@ExperimentalTime
class UserQuestionsFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var adapter: UserQuestionsAdapter
    private lateinit var userQuestionsAnswersList: List<QuestionInfo>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_questions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userQuestions = viewModel.userQuestions
        userQuestions.observe(viewLifecycleOwner, Observer {
            userQuestionsAnswersList = it
            Timber.d("$TAG: The count for adapter items -> ${it.count()}")
            if(it.count() > 0){
                setupRecyclerViewData(userQuestionsAnswersList)
            } else {
                noRecyclerViewData()
            }
        })

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

    private fun setupRecyclerViewData(quizList: List<QuestionInfo>) {
        setupRecyclerview(view, quizList)
        adapter.notifyDataSetChanged()
    }

    private fun setupRecyclerview(rootView: View?, userQuizList: List<QuestionInfo>) {
        val recyclerView = rootView?.findViewById(R.id.recyclerViewUserQuestions) as RecyclerView?
        recyclerView?.layoutManager = LinearLayoutManager(activity)
        adapter = UserQuestionsAdapter(userQuizList){position ->
            val questionID = userQuizList[position].questionId
            Timber.d("$TAG: Question ID is $questionID")
            val action = UserQuestionsFragmentDirections.actionUserQuestionsFragmentToListOfAnswersToQuestionFragment(questionID)
            findNavController().navigate(action)
        }
        recyclerView?.adapter = adapter
    }
}