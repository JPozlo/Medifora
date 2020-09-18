package com.misolova.medifora.util.adapters

import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.misolova.medifora.R
import com.misolova.medifora.domain.Question
import com.misolova.medifora.util.inflate
import kotlinx.android.synthetic.main.fragment_answer_request_item.view.*
import timber.log.Timber

class AnswerRequestAdapter(private val questionsAnswerRequest: List<Question>, private val navController: NavController) :
    RecyclerView.Adapter<AnswerRequestAdapter.AnswerRequestViewHolder>() {

    companion object {
        private const val TAG = "ANSWER REQUEST ADAPTER"
    }

    inner class AnswerRequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private var view: View = itemView

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerRequestViewHolder {
        val inflatedView = parent.inflate(R.layout.fragment_answer_request_item, false)
        return AnswerRequestViewHolder(inflatedView)
    }

    override fun getItemCount() = questionsAnswerRequest.size

    override fun onBindViewHolder(holder: AnswerRequestViewHolder, position: Int) {
        val question = questionsAnswerRequest[position]
        holder.itemView.tvAnswerRequestQuestionTitle.text = question.content
        holder.itemView.btnAnswerRequestConfirm.setOnClickListener {
            Timber.d("$TAG: Button for answer quiz confirmation clicked")
            navController.navigate(R.id.action_answerRequestFragment_to_answerFormFragment)
        }
    }
}