package com.misolova.medifora.util.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.misolova.medifora.R
import com.misolova.medifora.domain.model.AnswerInfo
import com.misolova.medifora.util.DateConversion
import com.misolova.medifora.util.inflate
import kotlinx.android.synthetic.main.fragment_user_answers_item.view.*

class UserAnswersAdapter(private val userAnswersArrayList: List<AnswerInfo>,
                         private val itemClick: (Int) -> Unit,
private val itemLongClick: (Int) -> Unit): RecyclerView.Adapter<UserAnswersAdapter.UserAnswersViewHolder>() {

    inner class UserAnswersViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        init {
            itemView.setOnClickListener { itemClick(adapterPosition) }
            itemView.setOnLongClickListener {
                itemLongClick(adapterPosition)
                true
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAnswersViewHolder {
        val inflatedView = parent.inflate(R.layout.fragment_user_answers_item, false)
        return UserAnswersViewHolder(inflatedView)
    }

    override fun getItemCount() = userAnswersArrayList.size

    override fun onBindViewHolder(holder: UserAnswersViewHolder, position: Int) {
        val userAnswer = userAnswersArrayList[position]
        val date = DateConversion().convertDate(userAnswer.answerCreatedAt)
        holder.itemView.tvUserAnswerCreationDate.text = date
        holder.itemView.tvAnswerStatus.text = userAnswer.status
        holder.itemView.tvUserAnswerPreviewItem.text = userAnswer.answerContent
    }
}