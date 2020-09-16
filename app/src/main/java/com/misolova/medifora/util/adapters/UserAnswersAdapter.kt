package com.misolova.medifora.util.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.misolova.medifora.R
import com.misolova.medifora.domain.Answer
import com.misolova.medifora.util.inflate
import kotlinx.android.synthetic.main.fragment_user_answers_item.view.*
import timber.log.Timber

class UserAnswersAdapter(private val userAnswersArrayList: ArrayList<Answer>): RecyclerView.Adapter<UserAnswersAdapter.UserAnswersViewHolder>() {

    inner class UserAnswersViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{
        private var view: View = itemView

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            Timber.i("UserAnswers RecyclerView: CLICK!")
            itemView.btnUserAnswerDetails.setOnClickListener {
                Snackbar.make(itemView, "Details Button clicked for question", Snackbar.LENGTH_LONG)
                    .show()
                Timber.i("Button clicked to go to details")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAnswersViewHolder {
        val inflatedView = parent.inflate(R.layout.fragment_user_answers_item, false)
        return UserAnswersViewHolder(inflatedView)
    }

    override fun getItemCount() = userAnswersArrayList.size

    override fun onBindViewHolder(holder: UserAnswersViewHolder, position: Int) {
        val answer = userAnswersArrayList[position]
        holder.itemView.tvUserAnswersQuestionTitleItem.text = answer.question
        holder.itemView.tvUserAnswerPreviewItem.text = answer.content.subSequence(0, 5)
    }
}