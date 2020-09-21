package com.misolova.medifora.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.misolova.medifora.R
import com.misolova.medifora.data.source.local.entities.UserAnswerEntity
import com.misolova.medifora.ui.viewmodel.MainViewModel
import com.misolova.medifora.util.adapters.UserAnswersAdapter
import timber.log.Timber

class UserAnswersFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var adapter: UserAnswersAdapter
    private lateinit var userAnswersArrayList: List<UserAnswerEntity>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_user_answers, container, false)

        setupRecyclerViewData(listOf())


        viewModel.userWithAnswers.observe(viewLifecycleOwner, Observer {
            userAnswersArrayList = it
            if(it.count() > 0){
                setupRecyclerViewData(userAnswersArrayList)
            } else{
                noRecyclerViewData()
            }
        })

        val btnUserAnswerDetails = rootView.findViewById(R.id.btnUserAnswerDetails) as MaterialButton?

        btnUserAnswerDetails?.setOnClickListener {
            findNavController().navigate(R.id.action_userAnswersFragment_to_listOfAnswersToQuestionFragment)
        }

        adapter.notifyDataSetChanged()

        return rootView
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            UserAnswersFragment().apply {
                UserAnswersFragment()
            }

        private const val TAG = "USER ANSWERS FRAGMENT"
    }

    private fun noRecyclerViewData(){
        return adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver(){
            override fun onChanged() {
                super.onChanged()
                if(adapter.itemCount == 0){
                    Toast.makeText(activity, "You haven't answered any question yet", Toast.LENGTH_SHORT).show()
                    setupRecyclerview(view, listOf())
                    adapter.notifyDataSetChanged()
                }
            }
        })
    }

    private fun setupRecyclerViewData(quizList: List<UserAnswerEntity>) {
        setupRecyclerview(view, quizList)
        adapter.notifyDataSetChanged()
    }

    private fun setupRecyclerview(rootView: View?, userAnswers: List<UserAnswerEntity>) {
        val recyclerView = rootView?.findViewById(R.id.recyclerViewUserAnswers) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        val filteredUserAnswer = userAnswers.find {
            it.user.userID == 0
        }
        val quizID = filteredUserAnswer?.answers?.get(0)?.answerQuestionID
        adapter = UserAnswersAdapter(userAnswers, quizID, findNavController()){ position ->
            val userAnswerEntity = userAnswers[position]
            Timber.d("$TAG: Question ID is $quizID")
            val action = UserAnswersFragmentDirections.actionUserAnswersFragmentToListOfAnswersToQuestionFragment(quizID!!)
            findNavController().navigate(action)
        }
        recyclerView.adapter = adapter
    }
}