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
import com.google.android.material.snackbar.Snackbar
import com.misolova.medifora.R
import com.misolova.medifora.domain.model.AnswerInfo
import com.misolova.medifora.ui.home.viewmodel.MainViewModel
import com.misolova.medifora.util.adapters.UserAnswersAdapter
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import kotlin.time.ExperimentalTime

@AndroidEntryPoint
@ExperimentalTime
class UserAnswersFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var adapter: UserAnswersAdapter
    private lateinit var userAnswersArrayList: List<AnswerInfo>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_user_answers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.userAnswers.observe(viewLifecycleOwner, Observer {
            userAnswersArrayList = it
            Timber.d("$TAG: The count for adapter items -> ${it.count()}")
            if(it.count() > 0){
                setupRecyclerViewData(userAnswersArrayList)
            } else{
                noRecyclerViewDataTest()
            }
        })

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            UserAnswersFragment().apply {
                UserAnswersFragment()
            }

        private const val TAG = "USER ANSWERS FRAGMENT"
    }

//    private fun noRecyclerViewData(){
//        return adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver(){
//            override fun onChanged() {
//                super.onChanged()
//                if(adapter.itemCount == 0){
//                    Toast.makeText(activity, "You haven't answered any question yet", Toast.LENGTH_SHORT).show()
//                    setupRecyclerview(view, listOf())
//                    adapter.notifyDataSetChanged()
//                }
//            }
//        })
//    }

    private fun noRecyclerViewDataTest(){
        Snackbar.make(requireView(), "You haven't answered any question yet", Snackbar.LENGTH_LONG).show()
        setupRecyclerview(view, listOf())
        adapter.notifyDataSetChanged()
    }

    private fun setupRecyclerViewData(quizList: List<AnswerInfo>) {
        setupRecyclerview(view, quizList)
        adapter.notifyDataSetChanged()
    }

    private fun setupRecyclerview(rootView: View?, userAnswers: List<AnswerInfo>) {
        val recyclerView = rootView?.findViewById(R.id.recyclerViewUserAnswers) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = UserAnswersAdapter(findNavController(), userAnswers){ position ->
            val userAnswer = userAnswers[position]
            Timber.d("$TAG: Question ID is ${userAnswer.questionID}")
            val action = UserAnswersFragmentDirections.actionUserAnswersFragmentToListOfAnswersToQuestionFragment(userAnswer.questionID)
            findNavController().navigate(action)
        }
        recyclerView.adapter = adapter
    }
}