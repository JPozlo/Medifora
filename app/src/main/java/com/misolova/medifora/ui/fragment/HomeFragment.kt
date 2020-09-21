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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.misolova.medifora.R
import com.misolova.medifora.data.source.local.entities.QuestionAnswerEntity
import com.misolova.medifora.data.source.local.entities.UserQuestionAnswersEntity
import com.misolova.medifora.ui.viewmodel.MainViewModel
import com.misolova.medifora.util.adapters.HomeFeedAdapter
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()

    private val myUserID = 0

    private lateinit var adapter: HomeFeedAdapter
    private lateinit var questionsArrayList: List<UserQuestionAnswersEntity>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        setupRecyclerViewData(listOf())

        val fabAddQuestion = rootView.findViewById(R.id.fabAddQuestion) as FloatingActionButton?

        fabAddQuestion?.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_questionFormFragment)
        }

        adapter.notifyDataSetChanged()

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val homeFeedQuestions = viewModel.userWithQuestionsAndAnswers

        homeFeedQuestions.observe(viewLifecycleOwner, Observer {
            questionsArrayList = it
            if(it.count() > 0){
                setupRecyclerViewData(questionsArrayList)
            } else {
                noRecyclerViewData()
            }

        })
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            HomeFragment().apply {
                HomeFragment()
            }

        private const val TAG = "HOME FRAGMENT"
    }

    private fun noRecyclerViewData(){
        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver(){
            override fun onChanged() {
                super.onChanged()
                if(adapter.itemCount == 0){
                    Toast.makeText(activity, "Nothing to be displayed as of now", Toast.LENGTH_SHORT).show()
                    setupRecyclerview(view, listOf(), userID = myUserID)
                    adapter.notifyDataSetChanged()
                }
            }
        })
    }

    private fun setupRecyclerViewData(quizList: List<UserQuestionAnswersEntity>) {
        setupRecyclerview(view, quizList, myUserID)
        adapter.notifyDataSetChanged()
    }

    private fun setupRecyclerview(rootView: View?, quizList: List<UserQuestionAnswersEntity>, userID: Int) {
        val recyclerView = rootView?.findViewById(R.id.recyclerViewHomeFeed) as RecyclerView?
        recyclerView?.layoutManager = LinearLayoutManager(activity)
        adapter = HomeFeedAdapter(userID, quizList) { position ->
            val userQuestionAnswersEntity = quizList.find {
                it.user.userID == userID
            }
            val quizID = userQuestionAnswersEntity?.questions?.get(position)?.question?.questionID
            Timber.d("$TAG: Question ID is $quizID")
            val action = HomeFragmentDirections.actionHomeFragmentToListOfAnswersToQuestionFragment(
                quizID!!
            )
            findNavController().navigate(action)
        }
        recyclerView?.adapter = adapter
    }
}