package com.misolova.medifora.ui.fragment

import android.os.Bundle
import android.util.Log
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
import timber.log.Timber

class HomeFragment : Fragment() {

    private lateinit var adapter: HomeFeedAdapter
    private val questionsArrayList: List<Question> = TestData().questionsArrayList.toList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        setupRecyclerview(rootView)

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

        private const val TAG = "HOME FRAGMENT"
    }

    private fun setupRecyclerview(rootView: View?) {
        val recyclerView = rootView?.findViewById(R.id.recyclerViewHomeFeed) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = HomeFeedAdapter(questionsArrayList){position ->
           val questionID = questionsArrayList[position].ID
            Log.d( "$TAG ","Question ID is $questionID")
            val action = HomeFragmentDirections.actionHomeFragmentToListOfAnswersToQuestionFragment(
                questionID
            )
            findNavController().navigate(action)
        }
        recyclerView.adapter = adapter
    }
}