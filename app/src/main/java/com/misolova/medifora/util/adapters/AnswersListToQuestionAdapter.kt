package com.misolova.medifora.util.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.misolova.medifora.R
import com.misolova.medifora.domain.Answer
import com.misolova.medifora.util.inflate
import kotlinx.android.synthetic.main.fragment_list_of_answers_to_question_item.view.*
import timber.log.Timber

class AnswersListToQuestionAdapter(private val answersToQuizArrayList: List<Answer>): RecyclerView.Adapter<AnswersListToQuestionAdapter.AnswersListToQuestionAdapterViewHolder>() {
    inner class AnswersListToQuestionAdapterViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{
        private var view: View = itemView
        private var answer: Answer? = null

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            Timber.i("AnswersToQuiz RecyclerView: CLICK!")
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AnswersListToQuestionAdapter.AnswersListToQuestionAdapterViewHolder {
        val inflatedView = parent.inflate(R.layout.fragment_list_of_answers_to_question_item, false)
        return AnswersListToQuestionAdapterViewHolder(inflatedView)
    }

    override fun getItemCount() = answersToQuizArrayList.size

    override fun onBindViewHolder(holder: AnswersListToQuestionAdapterViewHolder, position: Int) {
        val answer = answersToQuizArrayList[position]
        holder.itemView.tvListOfAnswersToQuizAnswerDateCreatedItem.text = answer.createdAt
        holder.itemView.tvListOfAnswersToQuizAnswerAuthorItem.text = answer.author
        holder.itemView.tvListOfAnswersToQuizAnswerContentItem.text = answer.content
    }
}