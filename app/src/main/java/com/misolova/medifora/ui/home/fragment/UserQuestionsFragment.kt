package com.misolova.medifora.ui.home.fragment

import android.content.SharedPreferences
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.misolova.medifora.R
import com.misolova.medifora.domain.model.QuestionInfo
import com.misolova.medifora.ui.home.viewmodel.MainViewModel
import com.misolova.medifora.util.Constants.KEY_USER_ID
import com.misolova.medifora.util.SwipeController
import com.misolova.medifora.util.SwipeControllerActions
import com.misolova.medifora.util.adapters.UserQuestionsAdapter
import com.misolova.medifora.util.showSingleActionSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_user_questions.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber
import javax.inject.Inject
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
@AndroidEntryPoint
@ExperimentalTime
class UserQuestionsFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance() =
            UserQuestionsFragment().apply {
                UserQuestionsFragment()
            }

        private const val TAG = "USER QUESTIONS FRAGMENT"
    }

    private val viewModel: MainViewModel by viewModels()

    private lateinit var adapter: UserQuestionsAdapter
    private lateinit var userQuestionsAnswersList: List<QuestionInfo>
    private lateinit var swipeController: SwipeController

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_questions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBarUserQuestionsFragment?.visibility = View.VISIBLE

        viewModel.startFetchingUserQuestions(getUserId())

        viewModel.userQuestions.observe(viewLifecycleOwner, Observer {
            progressBarUserQuestionsFragment?.visibility = View.GONE
            userQuestionsAnswersList = it
            if(it.count() > 0){
                setupRecyclerViewData(userQuestionsAnswersList)
            } else {
                noRecyclerViewData()
            }
        })
    }

    private fun getUserId() = sharedPreferences.getString(KEY_USER_ID, "")!!

    private fun noRecyclerViewData(){
        notifyUser("You haven't asked any question yet!")
    }

    private fun setupRecyclerViewData(quizList: List<QuestionInfo>) {
        setupRecyclerview(view, quizList)
        adapter.notifyDataSetChanged()
    }

    private fun notifyUser(message: String ) = requireView().showSingleActionSnackbar(message)

    private fun setupRecyclerview(rootView: View?, userQuizList: List<QuestionInfo>) {
        swipeController = SwipeController(object: SwipeControllerActions(){
            override fun onRightClicked(position: Int){
                viewModel.deleteQuestion(userQuizList[position].questionId)
                adapter.notifyItemRemoved(position)
                adapter.notifyItemRangeChanged(position, adapter.itemCount)
            }
        })
        val itemTouchHelper = ItemTouchHelper(swipeController)
        val recyclerView = rootView?.findViewById(R.id.recyclerViewUserQuestions) as RecyclerView?
        recyclerView?.layoutManager = LinearLayoutManager(activity)
        adapter = UserQuestionsAdapter(userQuizList){position ->
            val questionID = userQuizList[position].questionId
            Timber.d("$TAG: Question ID is $questionID")
            val action = UserQuestionsFragmentDirections.actionUserQuestionsFragmentToListOfAnswersToQuestionFragment(questionID)
            findNavController().navigate(action)
        }
        recyclerView?.adapter = adapter
        itemTouchHelper.attachToRecyclerView(recyclerView)
        recyclerView?.addItemDecoration(object: RecyclerView.ItemDecoration(){
            override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                super.onDraw(c, parent, state)
                swipeController.onDraw(c)
            }
        })
    }
}