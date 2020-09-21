package com.misolova.medifora.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.misolova.medifora.R
import com.misolova.medifora.data.source.local.entities.AnswerEntity
import com.misolova.medifora.ui.viewmodel.MainViewModel
import com.misolova.medifora.util.adapters.AnswersListToQuestionAdapter
import kotlinx.android.synthetic.main.fragment_list_of_answers_to_question.*

class ListOfAnswersToQuestionFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var adapter: AnswersListToQuestionAdapter
    private lateinit var answersToQuizArrayList: List<AnswerEntity>

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

        var progressBar =
            rootView.findViewById(R.id.progressBarListOfAnswersToQuizFragment) as ProgressBar?

        progressBar?.visibility = View.VISIBLE
        progressBar?.isIndeterminate = true

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var progressBar =
            view.findViewById(R.id.progressBarListOfAnswersToQuizFragment) as ProgressBar?

        arguments?.apply {
            var questionID = ListOfAnswersToQuestionFragmentArgs.fromBundle(this).questionID
            tvListOfAnswersToQuestionTitle?.text = questionID.toString()
            viewModel.getAnswersToQuestion(questionID).observe(viewLifecycleOwner, Observer {
                answersToQuizArrayList = it
                progressBar?.visibility = View.GONE
                setupRecyclerview(view, answersToQuizArrayList)
                adapter.notifyDataSetChanged()
            })
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ListOfAnswersToQuestionFragment().apply {
                ListOfAnswersToQuestionFragment()
            }

        private const val TAG = "LIST OF ANSWERS TO QUESTION FRAGMENT"
    }

    private fun setupRecyclerview(rootView: View?, answersToQuiz: List<AnswerEntity>) {
        val recyclerView =
            rootView?.findViewById(R.id.recyclerViewListOfAnswersToQuestion) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = AnswersListToQuestionAdapter(answersToQuiz)
        recyclerView.adapter = adapter
    }
}