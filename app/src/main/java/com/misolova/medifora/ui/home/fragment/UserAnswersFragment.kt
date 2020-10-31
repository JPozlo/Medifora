package com.misolova.medifora.ui.home.fragment

import android.content.SharedPreferences
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
import com.misolova.medifora.util.Constants.KEY_USER_ID
import com.misolova.medifora.util.adapters.UserAnswersAdapter
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject
import kotlin.time.ExperimentalTime

@AndroidEntryPoint
@ExperimentalTime
class UserAnswersFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var adapter: UserAnswersAdapter
    private lateinit var userAnswersArrayList: List<AnswerInfo>

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_user_answers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.startFetchingUserAnswers(getUserId())

        val userAnswers = viewModel.userAnswers
        userAnswers.observe(viewLifecycleOwner, Observer {
            userAnswersArrayList = it
            Timber.d("$TAG: The count for adapter items -> ${it.count()}")
            if(it.count() > 0){
                setupRecyclerViewData(userAnswersArrayList)
            } else{
                Snackbar.make(requireView(), "You haven't answered any question yet", Snackbar.LENGTH_LONG).show()
                setupRecyclerview(view, listOf())
                adapter.notifyDataSetChanged()
            }
        })
    }

    private fun getUserId() = sharedPreferences.getString(KEY_USER_ID, "")!!


    companion object {
        @JvmStatic
        fun newInstance() =
            UserAnswersFragment().apply {
                UserAnswersFragment()
            }

        private const val TAG = "USER ANSWERS FRAGMENT"
    }

    private fun setupRecyclerViewData(quizList: List<AnswerInfo>) {
        setupRecyclerview(view, quizList)
        adapter.notifyDataSetChanged()
    }

    private fun setupRecyclerview(rootView: View?, userAnswers: List<AnswerInfo>) {
        val recyclerView = rootView?.findViewById(R.id.recyclerViewUserAnswers) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = UserAnswersAdapter(userAnswers){ position ->
            val userAnswer = userAnswers[position]
            val quizId = userAnswer.questionID
            Timber.d("$TAG: Question ID is $quizId")
            val action = UserAnswersFragmentDirections.actionUserAnswersFragmentToListOfAnswersToQuestionFragment(quizId)
            findNavController().navigate(action)
        }
        recyclerView.adapter = adapter
    }
}