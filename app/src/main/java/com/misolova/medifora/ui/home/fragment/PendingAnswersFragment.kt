package com.misolova.medifora.ui.home.fragment

import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.misolova.medifora.R
import com.misolova.medifora.domain.model.AnswerInfo
import com.misolova.medifora.ui.home.viewmodel.MainViewModel
import com.misolova.medifora.util.Constants
import com.misolova.medifora.util.adapters.UserPendingAnswersAdapter
import com.misolova.medifora.util.showAlert
import com.misolova.medifora.util.showSingleActionSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_pending_answers.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
@ExperimentalTime
@AndroidEntryPoint
class PendingAnswersFragment : Fragment() {

    companion object {
        private const val TAG = "PENDING_ANSWERS_FRAGMENT"
    }
    private val viewModel: MainViewModel by viewModels()

    private lateinit var adapter: UserPendingAnswersAdapter
    private lateinit var userAnswersArrayList: List<AnswerInfo>

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_pending_answers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBarUserPendingAnswersFragment?.visibility = View.VISIBLE

        viewModel.startFetchingUserPendingAnswers(getUserId())

        val userPendingAnswers = viewModel.userPendingAnswers
        userPendingAnswers.observe(viewLifecycleOwner, Observer {
            progressBarUserPendingAnswersFragment?.visibility = View.GONE
            userAnswersArrayList = it
            if(it.count() > 0){
                setupRecyclerViewData(userAnswersArrayList)
            } else{
                notifyUser("You don't have any pending answer!")
            }
        })
    }

    private fun getUserId() = sharedPreferences.getString(Constants.KEY_USER_ID, "")!!

    private fun notifyUser(message: String ) = requireView().showSingleActionSnackbar(message)

    private fun setupRecyclerViewData(quizList: List<AnswerInfo>) {
        setupRecyclerview(view, quizList)
        adapter.notifyDataSetChanged()
    }

    private fun setupRecyclerview(rootView: View?, userAnswers: List<AnswerInfo>) {
        val recyclerView = rootView?.findViewById(R.id.recyclerViewUserPendingAnswers) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = UserPendingAnswersAdapter(userAnswers, { position ->
            notifyUser("Answers is still waiting approval.")
        },
            {position ->
                val answerID = userAnswers[position].answerId
                requireContext().showAlert("Delete Answer", "Go ahead with deletion?")
                    ?.setNegativeButton("Cancel") { dialog: DialogInterface?, which: Int ->
                        dialog?.cancel()
                    }
                    ?.setPositiveButton("Confirm") { dialog, which ->
                        viewModel.deleteAnswer(answerID).addOnSuccessListener {
                            adapter.notifyItemRemoved(position)
                            dialog.dismiss()
                            notifyUser("Answer deleted successfully")
                        }.addOnFailureListener { e ->
                            dialog.dismiss()
                            notifyUser(e.localizedMessage!!)
                        }
                    }?.create()?.show()
            })
        recyclerView.adapter = adapter
    }

}