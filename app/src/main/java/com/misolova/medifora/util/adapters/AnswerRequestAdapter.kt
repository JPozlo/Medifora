package com.misolova.medifora.util.adapters

import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.misolova.medifora.R
import com.misolova.medifora.domain.model.Question
import com.misolova.medifora.ui.home.fragment.AnswerRequestFragmentDirections
import com.misolova.medifora.util.inflate
import kotlinx.android.synthetic.main.fragment_answer_request_item.view.*
import timber.log.Timber

class AnswerRequestAdapter(private val questionsAnswerRequest: List<Question>, private val navController: NavController) :
    RecyclerView.Adapter<AnswerRequestAdapter.AnswerRequestViewHolder>() {

    companion object {
        private const val TAG = "ANSWER REQUEST ADAPTER"
    }

    inner class AnswerRequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerRequestViewHolder {
        val inflatedView = parent.inflate(R.layout.fragment_answer_request_item, false)
        return AnswerRequestViewHolder(inflatedView)
    }

    override fun getItemCount() = questionsAnswerRequest.size

    override fun onBindViewHolder(holder: AnswerRequestViewHolder, position: Int) {
        val question = questionsAnswerRequest[position]
        holder.itemView.tvAnswerRequestQuestionTitle.text = question.questionInfo.questionContent
        val questionID = question.questionInfo.questionId
        holder.itemView.btnAnswerRequestConfirm.setOnClickListener {
            Timber.d("$TAG: Adapter Question ID: $questionID")
            val action = AnswerRequestFragmentDirections.actionAnswerRequestFragmentToAnswerFormFragment(questionID!!)
            navController.navigate(action)
        }
    }
}