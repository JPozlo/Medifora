package com.misolova.medifora.util.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.misolova.medifora.R
import com.misolova.medifora.domain.Answer
import com.misolova.medifora.domain.Question
import com.misolova.medifora.util.inflate
import kotlinx.android.synthetic.main.fragment_answer_request_item.view.*
import kotlinx.android.synthetic.main.fragment_user_answers_item.view.*
import timber.log.Timber

class AnswerRequestAdapter(private val questionsAnswerRequest: ArrayList<Question>) :
    RecyclerView.Adapter<AnswerRequestAdapter.AnswerRequestViewHolder>() {

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
    }
}