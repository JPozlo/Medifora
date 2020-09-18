package com.misolova.medifora.util.adapters

import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.misolova.medifora.R
import com.misolova.medifora.domain.Question
import com.misolova.medifora.util.inflate
import kotlinx.android.synthetic.main.fragment_user_questions_item.view.*

class UserQuestionsAdapter(
    private val userQuestionsArrayList: List<Question>,
    private val navController: NavController,
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

    override fun getItemCount() = userQuestionsArrayList.size

    override fun onBindViewHolder(holder: UserQuestionsViewHolder, position: Int) {
        val question = userQuestionsArrayList[position]
        holder.itemView.tvUserQuestionsTitleItem.text = question.content
        holder.itemView.tvUserQuestionsTopAnswerPreviewItem.text = "Placeholder Top Answer Preview"
        holder.itemView.tvUserQuestionAnswersCountItem.text = question.totalAnswersNumber.toString()
    }
}