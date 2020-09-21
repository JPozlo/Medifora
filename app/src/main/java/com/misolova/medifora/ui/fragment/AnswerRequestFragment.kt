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
import com.misolova.medifora.domain.model.Question
import com.misolova.medifora.util.adapters.AnswerRequestAdapter

class AnswerRequestFragment : Fragment() {

    private lateinit var adapter: AnswerRequestAdapter
    private lateinit var questionsArrayList: ArrayList<Question>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_answer_request, container, false)

        setupRecyclerview(rootView, listOf())
        adapter.notifyDataSetChanged()

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val questions = questionsArrayList.filter {
            it.totalNumberOfAnswers <= 0
        }

        setupRecyclerview(view, questions)
        adapter.notifyDataSetChanged()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            AnswerRequestFragment().apply {
                AnswerRequestFragment()
            }

        private const val TAG  = "ANSWER REQUEST FRAGMENT"
    }

    private fun setupRecyclerview(rootView: View?, quizList: List<Question>) {
        val recyclerView = rootView?.findViewById(R.id.recyclerViewAnswerRequest) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = AnswerRequestAdapter(quizList, findNavController())
        recyclerView.adapter = adapter
    }
}