package com.misolova.medifora.ui.home.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.misolova.medifora.R
import com.misolova.medifora.domain.model.AnswerInfo
import com.misolova.medifora.ui.home.viewmodel.MainViewModel
import com.misolova.medifora.util.adapters.AnswersListToQuestionAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_list_of_answers_to_question.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
@AndroidEntryPoint
@ExperimentalTime
class ListOfAnswersToQuestionFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance() =
            ListOfAnswersToQuestionFragment().apply {
                ListOfAnswersToQuestionFragment()
            }

        private const val TAG = "ANSWERSTOQUIZ FRAGMENT"
    }

    private val viewModel: MainViewModel by viewModels()

    private lateinit var adapter: AnswersListToQuestionAdapter
    private lateinit var answersToQuizArrayList: List<AnswerInfo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val navOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.homeFragment, true)
                    .build()
                findNavController().navigate(R.id.action_listOfAnswersToQuestionFragment_to_homeFragment, savedInstanceState, navOptions)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView =
            inflater.inflate(R.layout.fragment_list_of_answers_to_question, container, false)

        setupRecyclerview(rootView, listOf())

        adapter.notifyDataSetChanged()

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var progressBar =
            view.findViewById(R.id.progressBarListOfAnswersToQuizFragment) as ProgressBar?

        progressBar?.visibility = View.VISIBLE
        progressBar?.isIndeterminate = false

        arguments?.apply {
//            var questionID = ListOfAnswersToQuestionFragmentArgs.fromBundle(this).questionID

//            Log.d("$TAG", "The question ID is $questionID")

            viewModel.questionID.observe(viewLifecycleOwner, Observer {
                val questionID = it!!
                tvListOfAnswersToQuestionTitle.text = questionID
            })

            viewModel.answersToQuiz.observe(viewLifecycleOwner, Observer {
                Timber.d("$TAG: The answers are -> ${it[0]}")
                Log.d("$TAG", "The answers list is -> ${it[0]}")
                answersToQuizArrayList = it
                progressBar?.visibility = View.GONE
                setupRecyclerview(view, answersToQuizArrayList)
                adapter.notifyDataSetChanged()
            })
        }
    }

    private fun setupRecyclerview(rootView: View?, answersToQuiz: List<AnswerInfo>) {
        val recyclerView =
            rootView?.findViewById(R.id.recyclerViewListOfAnswersToQuestion) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = AnswersListToQuestionAdapter(answersToQuiz)
        recyclerView.adapter = adapter
    }
}