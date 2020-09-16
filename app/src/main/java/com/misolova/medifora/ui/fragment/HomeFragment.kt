package com.misolova.medifora.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.misolova.medifora.R
import com.misolova.medifora.domain.Question
import com.misolova.medifora.util.adapters.HomeFeedAdapter

class HomeFragment : Fragment() {

    private lateinit var adapter: HomeFeedAdapter
    private val questionsArrayList: ArrayList<Question> =
        arrayListOf(
            Question("111", "Fake content", "First answer", 3,"Collins", "12/08/2010"),
            Question("222", "Testing content", "Second answer", 2,"John", "20/03/2020")
        )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        setupRecyclerview(inflater, container, rootView)

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

    private fun setupRecyclerview(inflater: LayoutInflater, container: ViewGroup?, rootView: View?) {
        val recyclerView = rootView?.findViewById(R.id.recyclerViewHomeFeed) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = HomeFeedAdapter(questionsArrayList)
        recyclerView.adapter = adapter
    }
}