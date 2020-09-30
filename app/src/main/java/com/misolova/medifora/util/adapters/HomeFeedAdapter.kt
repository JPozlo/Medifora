package com.misolova.medifora.util.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.misolova.medifora.R
import com.misolova.medifora.domain.model.QuestionInfo
import com.misolova.medifora.util.inflate
import kotlinx.android.synthetic.main.fragment_home_item.view.*
import kotlinx.coroutines.InternalCoroutinesApi
import kotlin.time.ExperimentalTime
import kotlin.time.milliseconds

@InternalCoroutinesApi
@ExperimentalTime
class HomeFeedAdapter(private val homeQuestionsList: List<QuestionInfo>,
                      private val itemClick: (Int) -> Unit):
    RecyclerView.Adapter<HomeFeedAdapter.HomeFeedViewHolder>() {

    inner class HomeFeedViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        init {
            itemView.setOnClickListener { itemClick(adapterPosition) }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeFeedAdapter.HomeFeedViewHolder {
        val inflatedView = parent.inflate(R.layout.fragment_home_item, false)
        return HomeFeedViewHolder(inflatedView)
    }

    override fun getItemCount() = homeQuestionsList.size

    override fun onBindViewHolder(holder: HomeFeedAdapter.HomeFeedViewHolder, position: Int) {
        val question = homeQuestionsList[position]
        val dateInMs =  question.questionCreatedAt.milliseconds
        holder.itemView.tvHomeFeedQuestionContentItem.text = question.questionContent
        holder.itemView.tvHomeFeedQuestionAuthorItem.text = question.questionAuthorID
        holder.itemView.tvHomeFeedQuestionCreationDateItem.text = dateInMs.toIsoString()
    }


}