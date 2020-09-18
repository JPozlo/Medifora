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
import com.misolova.medifora.util.adapters.UserAnswersAdapter
import com.misolova.medifora.util.adapters.UserQuestionsAdapter
import kotlinx.android.synthetic.main.fragment_user_questions.*
import kotlinx.android.synthetic.main.fragment_user_questions_item.*

class UserQuestionsFragment : Fragment() {

    private lateinit var adapter: UserQuestionsAdapter
    private lateinit var questionsArrayList: List<Question>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_user_questions, container, false)

        setupRecyclerview(rootView)

        adapter.notifyDataSetChanged()

        return rootView
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            UserQuestionsFragment().apply {
               UserQuestionsFragment()
            }
    }

    private fun setupRecyclerview(rootView: View?) {
        val recyclerView = rootView?.findViewById(R.id.recyclerViewUserQuestions) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = UserQuestionsAdapter(questionsArrayList, findNavController())
        recyclerView.adapter = adapter
    }
}