package com.misolova.medifora.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.misolova.medifora.R
import com.misolova.medifora.domain.Question
import com.misolova.medifora.util.Constants.QUESTION_ID_BUNDLE_KEY_VALUE
import com.misolova.medifora.util.TestData
import com.misolova.medifora.util.adapters.HomeFeedAdapter

class HomeFragment : Fragment() {

    private lateinit var adapter: HomeFeedAdapter
    private val questionsArrayList: List<Question> = TestData().questionsArrayList.toList()

    private val questionID: Int = questionsArrayList[1].ID

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        val bundle = bundleOf(QUESTION_ID_BUNDLE_KEY_VALUE to questionID)

        val action = HomeFragmentDirections.actionHomeFragmentToListOfAnswersToQuestionFragment(questionID)

        setupRecyclerview(rootView, bundle, R.id.action_homeFragment_to_listOfAnswersToQuestionFragment)

        val fabAddQuestion = rootView.findViewById(R.id.fabAddQuestion) as FloatingActionButton?

        fabAddQuestion?.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_questionFormFragment)
        }

        adapter.notifyDataSetChanged()

        return rootView
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            HomeFragment().apply {
                HomeFragment()
            }
    }

    private fun setupRecyclerview(rootView: View?, bundle: Bundle, resID: Int) {
        val recyclerView = rootView?.findViewById(R.id.recyclerViewHomeFeed) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = HomeFeedAdapter(questionsArrayList, bundle, resID, findNavController())
        recyclerView.adapter = adapter
    }
}