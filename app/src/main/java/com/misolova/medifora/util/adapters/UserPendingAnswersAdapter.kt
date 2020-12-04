package com.misolova.medifora.util.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.misolova.medifora.R
import com.misolova.medifora.domain.model.AnswerInfo
import com.misolova.medifora.util.DateConversion
import com.misolova.medifora.util.inflate
import kotlinx.android.synthetic.main.fragment_pending_answers_item.view.*

class UserPendingAnswersAdapter(
    private val userAnswersArrayList: List<AnswerInfo>,
    private val itemClick: (Int) -> Unit,
    private val itemLongClick: (Int) -> Unit
): RecyclerView.Adapter<UserPendingAnswersAdapter.UserPendingAnswersViewHolder>() {

    inner class UserPendingAnswersViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        init {
            itemView.setOnClickListener { itemClick(adapterPosition) }
            itemView.setOnLongClickListener {
                itemLongClick(adapterPosition)
                true
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserPendingAnswersViewHolder {
        val inflatedView = parent.inflate(R.layout.fragment_pending_answers_item, false)
        return UserPendingAnswersViewHolder(inflatedView)
    }

    override fun getItemCount() = userAnswersArrayList.size

    override fun onBindViewHolder(holder: UserPendingAnswersViewHolder, position: Int) {
        val userAnswer = userAnswersArrayList[position]
        val date = DateConversion().convertDate(userAnswer.answerCreatedAt)
        holder.itemView.tvUserAnswerPendingCreationDate.text = date
        holder.itemView.tvAnswerPendingStatus.text = userAnswer.status
        holder.itemView.tvUserAnswerPendingPreviewItem.text = userAnswer.answerContent
    }
}