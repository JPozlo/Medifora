package com.misolova.medifora.util.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.misolova.medifora.R
import com.misolova.medifora.domain.model.Answer
import com.misolova.medifora.domain.model.AnswerInfo
import com.misolova.medifora.util.DateConversion
import com.misolova.medifora.util.inflate
import kotlinx.android.synthetic.main.fragment_list_of_answers_to_question_item.view.*
import timber.log.Timber
import kotlin.time.ExperimentalTime

@ExperimentalTime
class AnswersListToQuestionAdapter(private val answersToQuizArrayList: List<AnswerInfo>): RecyclerView.Adapter<AnswersListToQuestionAdapter.AnswersListToQuestionAdapterViewHolder>() {
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
        val date = DateConversion().convertDate(answer.answerCreatedAt)
        holder.itemView.tvListOfAnswersToQuizAnswerDateCreatedItem.text = date
        holder.itemView.tvListOfAnswersToQuizAnswerAuthorItem.text = answer.answerAuthorID
        holder.itemView.tvListOfAnswersToQuizAnswerContentItem.text = answer.answerContent
    }
}