package com.misolova.medifora.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.misolova.medifora.R
import com.misolova.medifora.domain.Question
import com.misolova.medifora.util.adapters.AnswerRequestAdapter
import com.misolova.medifora.util.adapters.HomeFeedAdapter
import timber.log.Timber

class AnswerRequestFragment : Fragment() {

    private lateinit var adapter: AnswerRequestAdapter
    private lateinit var questionsArrayList: List<Question>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_answer_request, container, false)

        setupRecyclerview(rootView)

        adapter.notifyDataSetChanged()
        return rootView
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            AnswerRequestFragment().apply {
                AnswerRequestFragment()
            }
    }

    private fun setupRecyclerview(rootView: View?) {
        val recyclerView = rootView?.findViewById(R.id.recyclerViewAnswerRequest) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = AnswerRequestAdapter(questionsArrayList, navController = findNavController())
        recyclerView.adapter = adapter
    }
}