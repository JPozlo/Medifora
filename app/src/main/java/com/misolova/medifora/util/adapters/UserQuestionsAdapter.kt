package com.misolova.medifora.util.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.misolova.medifora.R
import com.misolova.medifora.domain.Question
import com.misolova.medifora.util.inflate
import kotlinx.android.synthetic.main.fragment_user_questions_item.view.*
import timber.log.Timber

class UserQuestionsAdapter(private val userQuestionsArrayList: ArrayList<Question>): RecyclerView.Adapter<UserQuestionsAdapter.UserQuestionsViewHolder>() {

    inner class UserQuestionsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{
        private var view: View = itemView
        private var question: Question? = null

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            Timber.i("UserQuestions RecyclerView: CLICK!")
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserQuestionsViewHolder {
        val inflatedView = parent.inflate(R.layout.fragment_user_questions_item, false)
        return UserQuestionsViewHolder(inflatedView)
    }

    override fun getItemCount() = userQuestionsArrayList.size

    override fun onBindViewHolder(holder: UserQuestionsViewHolder, position: Int) {
        val question = userQuestionsArrayList[position]
        holder.itemView.tvUserQuestionsTitleItem.text = question.content
        holder.itemView.tvUserQuestionsTopAnswerPreviewItem.text = question.answer.subSequence(0, 5)
        holder.itemView.tvUserQuestionAnswersCountItem.text = question.totalAnswersNumber.toString()
    }
}