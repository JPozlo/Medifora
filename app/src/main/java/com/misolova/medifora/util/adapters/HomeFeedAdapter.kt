package com.misolova.medifora.util.adapters

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.misolova.medifora.R
import com.misolova.medifora.domain.Question
import com.misolova.medifora.util.inflate
import kotlinx.android.synthetic.main.fragment_home_item.view.*
import timber.log.Timber

class HomeFeedAdapter(private val homeQuestionsArrayList: List<Question>,
                      private val bundle: Bundle,
                      private val resID: Int,
                      private val navController: NavController):
    RecyclerView.Adapter<HomeFeedAdapter.HomeFeedViewHolder>() {

    inner class HomeFeedViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{
        private var view: View = itemView
        private var question: Question? = null

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            Timber.i("HomeFeed RecyclerView: CLICK!")
            val context = itemView.context
            Snackbar.make(itemView, "Card item clicked by author: ${itemView.tvHomeFeedAnswerAuthorItem.text}", Snackbar.LENGTH_LONG).show()
            navController.navigate(resID, bundle)
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeFeedAdapter.HomeFeedViewHolder {
        val inflatedView = parent.inflate(R.layout.fragment_home_item, false)
        return HomeFeedViewHolder(inflatedView)
    }

    override fun getItemCount() = homeQuestionsArrayList.size

    override fun onBindViewHolder(holder: HomeFeedAdapter.HomeFeedViewHolder, position: Int) {
        val question = homeQuestionsArrayList[position]
        holder.itemView.tvHomeFeedQuestionItem.text = question.content
        holder.itemView.tvHomeFeedAnswerAuthorItem.text = question.author
        holder.itemView.tvHomeFeedAnswersItem.text = "Placeholder Answer Content"
    }
}