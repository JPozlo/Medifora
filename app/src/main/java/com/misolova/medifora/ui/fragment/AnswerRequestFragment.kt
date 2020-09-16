package com.misolova.medifora.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    private val questionsArrayList: ArrayList<Question> =
        arrayListOf(
            Question("111", "Fake content", "First answer", 3,"Collins", "12/08/2010"),
            Question("222", "Testing content", "Second answer", 2,"John", "20/03/2020")
        )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_answer_request, container, false)

        val btnAnswerRequestConfirm = rootView.findViewById(R.id.btnAnswerRequestConfirm) as MaterialButton?

//        btnAnswerRequestConfirm?.setOnClickListener {
//            Snackbar.make(rootView, "Details Button clicked for question", Snackbar.LENGTH_LONG)
//                .show()
//            Timber.i("Button clicked to go to details")
//        }

        setupRecyclerview(inflater, container, rootView)

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

    private fun setupRecyclerview(inflater: LayoutInflater, container: ViewGroup?, rootView: View?) {
        val recyclerView = rootView?.findViewById(R.id.recyclerViewAnswerRequest) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = AnswerRequestAdapter(questionsArrayList)
        recyclerView.adapter = adapter
    }
}