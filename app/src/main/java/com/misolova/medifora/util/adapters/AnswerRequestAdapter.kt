package com.misolova.medifora.util.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.misolova.medifora.R
import com.misolova.medifora.domain.model.QuestionInfo
import com.misolova.medifora.util.inflate
import kotlinx.android.synthetic.main.fragment_answer_request_item.view.*

class AnswerRequestAdapter(private val questionsAnswerRequest: List<QuestionInfo>,
                           private val itemClick: (Int) -> Unit) :
    RecyclerView.Adapter<AnswerRequestAdapter.AnswerRequestViewHolder>() {

    companion object {
        private const val TAG = "ANSWER REQUEST ADAPTER"
    }

    inner class AnswerRequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        init {
            itemView.setOnClickListener { itemClick(adapterPosition) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerRequestViewHolder {
        val inflatedView = parent.inflate(R.layout.fragment_answer_request_item, false)
        return AnswerRequestViewHolder(inflatedView)
    }

    override fun getItemCount() = questionsAnswerRequest.size

    override fun onBindViewHolder(holder: AnswerRequestViewHolder, position: Int) {
        val question = questionsAnswerRequest[position]
        holder.itemView.tvAnswerRequestQuestionTitle.text = question.questionContent
    }
}