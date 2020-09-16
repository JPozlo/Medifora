package com.misolova.medifora.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.misolova.medifora.R
import com.misolova.medifora.domain.Answer
import com.misolova.medifora.util.adapters.AnswersListToQuestionAdapter
import com.misolova.medifora.util.adapters.HomeFeedAdapter
import kotlinx.android.synthetic.main.fragment_list_of_answers_to_question.*

class ListOfAnswersToQuestionFragment : Fragment() {

    private lateinit var adapter: AnswersListToQuestionAdapter
    private var answersToQuizArrayList: ArrayList<Answer> =
        arrayListOf(
            Answer("111", "Fake content", "First question", "Collins", "12/08/2010"),
            Answer("222", "Testing content", "Second question", "John", "20/03/2020")
        )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_list_of_answers_to_question, container, false)

        setupRecyclerview(inflater, container, rootView)

        adapter.notifyDataSetChanged()

        return rootView
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ListOfAnswersToQuestionFragment().apply {
               ListOfAnswersToQuestionFragment()
            }
    }

    private fun setupRecyclerview(inflater: LayoutInflater, container: ViewGroup?, rootView: View?) {
        val recyclerView = rootView?.findViewById(R.id.recyclerViewListOfAnswersToQuestion) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = AnswersListToQuestionAdapter(answersToQuizArrayList)
        recyclerView.adapter = adapter
    }
}