package com.misolova.medifora.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.misolova.medifora.R
import com.misolova.medifora.domain.Answer
import com.misolova.medifora.util.Constants.QUESTION_ID_BUNDLE_KEY_VALUE
import com.misolova.medifora.util.TestData
import com.misolova.medifora.util.adapters.AnswersListToQuestionAdapter
import kotlinx.android.synthetic.main.fragment_list_of_answers_to_question.*
import timber.log.Timber

class ListOfAnswersToQuestionFragment : Fragment() {


    private lateinit var adapter: AnswersListToQuestionAdapter
    private lateinit var answersToQuizArrayList: List<Answer>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_list_of_answers_to_question, container, false)

        setupRecyclerview(rootView, listOf())

        adapter.notifyDataSetChanged()

        var progressBar = rootView.findViewById(R.id.progressBarListOfAnswersToQuizFragment) as ProgressBar?

        progressBar?.visibility = View.VISIBLE
        progressBar?.isIndeterminate = true

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var progressBar = view.findViewById(R.id.progressBarListOfAnswersToQuizFragment) as ProgressBar?


        arguments?.apply {
            var questionID = ListOfAnswersToQuestionFragmentArgs.fromBundle(this).questionID
            tvListOfAnswersToQuestionTitle?.text = questionID.toString()
            answersToQuizArrayList = TestData().userAnswersArrayList.filter {
                it.questionID == questionID
            }
            progressBar?.visibility = View.GONE
            setupRecyclerview(view, answersToQuizArrayList)
            adapter.notifyDataSetChanged()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ListOfAnswersToQuestionFragment().apply {
               ListOfAnswersToQuestionFragment()
            }
    }

    private fun setupRecyclerview(rootView: View?, answersToQuizList: List<Answer>) {
        val recyclerView = rootView?.findViewById(R.id.recyclerViewListOfAnswersToQuestion) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = AnswersListToQuestionAdapter(answersToQuizList)
        recyclerView.adapter = adapter
    }
}