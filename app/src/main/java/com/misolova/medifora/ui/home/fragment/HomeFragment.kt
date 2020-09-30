package com.misolova.medifora.ui.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.misolova.medifora.R
import com.misolova.medifora.domain.model.QuestionInfo
import com.misolova.medifora.ui.auth.viewmodel.AuthViewModel
import com.misolova.medifora.ui.home.viewmodel.MainViewModel
import com.misolova.medifora.util.adapters.HomeFeedAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import timber.log.Timber
import kotlin.time.ExperimentalTime

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
@ExperimentalTime
@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()
    private val authViewModel: AuthViewModel by activityViewModels()

    private lateinit var adapter: HomeFeedAdapter
    private lateinit var questionsArrayList: List<QuestionInfo>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        val navController = findNavController()


        setupRecyclerViewData(listOf())

        val fabAddQuestion = rootView.findViewById(R.id.fabAddQuestion) as FloatingActionButton?

        fabAddQuestion?.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_questionFormFragment)
        }

        adapter.notifyDataSetChanged()

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val homeFeedQuestions = viewModel.homeFeedQuestions
        homeFeedQuestions.observe(viewLifecycleOwner, Observer {
            questionsArrayList = it
            if (it.count() > 0) {
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

    private fun noRecyclerViewData() {
        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                if (adapter.itemCount == 0) {
                    Toast.makeText(
                        activity,
                        "Nothing to be displayed as of now",
                        Toast.LENGTH_SHORT
                    ).show()
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

    private fun setupRecyclerview(rootView: View?, quizList: List<QuestionInfo>) {
        val recyclerView = rootView?.findViewById(R.id.recyclerViewHomeFeed) as RecyclerView?
        recyclerView?.layoutManager = LinearLayoutManager(activity)
        adapter = HomeFeedAdapter(quizList) { position ->
            val question = quizList[position]
            val questionId = question.questionId
            viewModel.setQuestionId(questionId)
            Timber.d("$TAG: Question ID is $questionId")
            val action = HomeFragmentDirections.actionHomeFragmentToListOfAnswersToQuestionFragment(
                questionID = questionId
            )
            findNavController().navigate(action)
        }
        recyclerView?.adapter = adapter
    }
}