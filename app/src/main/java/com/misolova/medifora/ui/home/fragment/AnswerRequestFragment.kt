package com.misolova.medifora.ui.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.misolova.medifora.R
import com.misolova.medifora.domain.model.QuestionInfo
import com.misolova.medifora.ui.home.viewmodel.MainViewModel
import com.misolova.medifora.util.adapters.AnswerRequestAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
@ExperimentalTime
@AndroidEntryPoint
class AnswerRequestFragment : Fragment() {

    private lateinit var adapter: AnswerRequestAdapter
    private val viewModel: MainViewModel by viewModels()

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

        viewModel.startFetchingQuestionsWithoutAnswers()

        viewModel.questionsWithoutAnswers.observe(viewLifecycleOwner, Observer {
            setupRecyclerview(view, it)
            adapter.notifyDataSetChanged()
        })

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            AnswerRequestFragment().apply {
                AnswerRequestFragment()
            }

        private const val TAG  = "ANSWER REQUEST FRAGMENT"
    }

    private fun setupRecyclerview(rootView: View?, quizList: List<QuestionInfo>) {
        val recyclerView = rootView?.findViewById(R.id.recyclerViewAnswerRequest) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = AnswerRequestAdapter(quizList) { position ->
            val questionID = quizList[position].questionId
            Timber.d("${TAG}: Answer Request Question ID: $questionID")
            val action = AnswerRequestFragmentDirections.actionAnswerRequestFragmentToAnswerFormFragment(questionID)
            findNavController().navigate(action)
        }
        recyclerView.adapter = adapter
    }
}