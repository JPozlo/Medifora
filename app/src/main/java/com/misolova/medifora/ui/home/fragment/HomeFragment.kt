package com.misolova.medifora.ui.home.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.misolova.medifora.R
import com.misolova.medifora.domain.model.QuestionInfo
import com.misolova.medifora.ui.auth.viewmodel.AuthViewModel
import com.misolova.medifora.ui.home.viewmodel.MainViewModel
import com.misolova.medifora.util.Constants.KEY_USER_ID
import com.misolova.medifora.util.Constants.KEY_USER_NAME
import com.misolova.medifora.util.adapters.HomeFeedAdapter
import com.misolova.medifora.util.showSingleActionSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import timber.log.Timber
import javax.inject.Inject
import kotlin.time.ExperimentalTime

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
@ExperimentalTime
@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()
    private val authViewModel: AuthViewModel by activityViewModels()

    private lateinit var adapter: HomeFeedAdapter
    private lateinit var questionsArrayList: List<QuestionInfo>
    @Inject
    lateinit var sharedPreferences: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        val navController = findNavController()

        setupRecyclerViewData(listOf())

        val fabAddQuestion = rootView.findViewById(R.id.fabAddQuestion) as FloatingActionButton?

        fabAddQuestion?.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_questionFormFragment)
        }

        adapter.notifyDataSetChanged()

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBarHomeFeed?.visibility = View.VISIBLE

        val id = sharedPreferences.getString(KEY_USER_ID, "")!!

        viewModel.fetchUserById(id)

        viewModel.startFetchingHomeQuestions()

        viewModel.getUserDetails().observe(viewLifecycleOwner, Observer {
            progressBarHomeFeed?.visibility = View.GONE
            Timber.e("$TAG: User Details -> ${it}")
            sharedPreferences.edit().putString(KEY_USER_NAME, it.name).apply()
            tvHomeFeedTitle.text = "Hello, ${it.name}"
        })

        val homeFeedQuestions = viewModel.questionsByCreationDate
        homeFeedQuestions.observe(viewLifecycleOwner, Observer {
            questionsArrayList = it
            if (it.count() > 0) {
                setupRecyclerViewData(questionsArrayList)
            } else {
                noRecyclerViewData()
            }
        })

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            HomeFragment().apply {
                HomeFragment()
            }

        private const val TAG = "HOME FRAGMENT"
    }

    private fun noRecyclerViewData() {
        notifyUser("No data to be displayed as of now.")
    }

    private fun notifyUser(message: String) = requireView().showSingleActionSnackbar(message)

    private fun setupRecyclerViewData(quizList: List<QuestionInfo>) {
        setupRecyclerview(view, quizList)
        adapter.notifyDataSetChanged()
    }

    private fun setupRecyclerview(rootView: View?, quizList: List<QuestionInfo>) {
        val recyclerView = rootView?.findViewById(R.id.recyclerViewHomeFeed) as RecyclerView?
        recyclerView?.layoutManager = LinearLayoutManager(activity)
        adapter = HomeFeedAdapter(quizList) { position ->
            val questionId = quizList[position].questionId
            Timber.d("$TAG: Question ID is $questionId")
            val action = HomeFragmentDirections.actionHomeFragmentToListOfAnswersToQuestionFragment(
                questionID = questionId
            )
            findNavController().navigate(action)
        }
        recyclerView?.adapter = adapter
    }
}