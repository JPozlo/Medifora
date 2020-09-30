package com.misolova.medifora.util.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.misolova.medifora.R
import com.misolova.medifora.domain.model.QuestionInfo
import com.misolova.medifora.util.inflate
import kotlinx.android.synthetic.main.fragment_user_questions_item.view.*
import kotlin.time.ExperimentalTime
import kotlin.time.milliseconds

@ExperimentalTime
class UserQuestionsAdapter(
    private val userQuestionsList: List<QuestionInfo>,
    private val itemClick: (Int) -> Unit
) : RecyclerView.Adapter<UserQuestionsAdapter.UserQuestionsViewHolder>() {

    inner class UserQuestionsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private var view: View = itemView

        init {
            itemView.setOnClickListener { itemClick(adapterPosition) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserQuestionsViewHolder {
        val inflatedView = parent.inflate(R.layout.fragment_user_questions_item, false)
        return UserQuestionsViewHolder(inflatedView)
    }

    override fun getItemCount() = userQuestionsList.size

    override fun onBindViewHolder(holder: UserQuestionsViewHolder, position: Int) {
        val userQuestion = userQuestionsList[position]
        holder.itemView.tvUserQuestionsTitleItem.text = userQuestion.questionContent
        holder.itemView.tvUserQuestionsCreatedAtItem.text = userQuestion.questionCreatedAt.milliseconds.toIsoString()
        holder.itemView.tvUserQuestionAnswersCountItem.text = userQuestion.totalNumberOfAnswers.toString()
    }
}