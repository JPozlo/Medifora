package com.misolova.medifora.util.adapters

import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.misolova.medifora.R
import com.misolova.medifora.domain.model.Answer
import com.misolova.medifora.util.inflate
import kotlinx.android.synthetic.main.fragment_user_answers_item.view.*
import timber.log.Timber

class UserAnswersAdapter(private val userAnswersArrayList: List<Answer>, private val navController: NavController,
                         private val itemClick: (Int) -> Unit): RecyclerView.Adapter<UserAnswersAdapter.UserAnswersViewHolder>() {

    inner class UserAnswersViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        init {
            itemView.setOnClickListener { itemClick(adapterPosition) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAnswersViewHolder {
        val inflatedView = parent.inflate(R.layout.fragment_user_answers_item, false)
        return UserAnswersViewHolder(inflatedView)
    }

    override fun getItemCount() = userAnswersArrayList.size

    override fun onBindViewHolder(holder: UserAnswersViewHolder, position: Int) {
        val answer = userAnswersArrayList[position]
        holder.itemView.tvUserAnswersQuestionTitleItem.text = answer.questionContent
        holder.itemView.tvUserAnswerPreviewItem.text = answer.content.subSequence(0, 5)
        holder.itemView.btnUserAnswerDetails.setOnClickListener {
            Snackbar.make(holder.itemView, "Details Button clicked for answer: ${answer.ID}", Snackbar.LENGTH_LONG)
                .show()
            navController.navigate(R.id.action_userAnswersFragment_to_listOfAnswersToQuestionFragment)
            Timber.i("Button clicked to go to details")
        }
    }
}