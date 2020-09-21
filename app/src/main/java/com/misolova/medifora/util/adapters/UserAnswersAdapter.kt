package com.misolova.medifora.util.adapters

import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.misolova.medifora.R
import com.misolova.medifora.data.source.local.entities.UserAnswerEntity
import com.misolova.medifora.util.inflate
import kotlinx.android.synthetic.main.fragment_user_answers_item.view.*
import timber.log.Timber

class UserAnswersAdapter(private val userAnswersArrayList: List<UserAnswerEntity>,
                         private val questionID: Int?,
                         private val navController: NavController,
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
        val userAnswerEntity = userAnswersArrayList[position]
        val filteredAnswer = userAnswerEntity.answers.filter {
            it.answerQuestionID == questionID
        }
        holder.itemView.tvUserAnswersQuestionTitleItem.text = filteredAnswer[0].answerQuestionID.toString()
        holder.itemView.tvUserAnswerPreviewItem.text = userAnswerEntity.answers[position].content.subSequence(0, 5)
        holder.itemView.btnUserAnswerDetails.setOnClickListener {
            Snackbar.make(holder.itemView, "Details Button clicked for answer: ${userAnswerEntity.answers[position].answerID}", Snackbar.LENGTH_LONG)
                .show()
            navController.navigate(R.id.action_userAnswersFragment_to_listOfAnswersToQuestionFragment)
            Timber.i("Button clicked to go to details")
        }
    }
}