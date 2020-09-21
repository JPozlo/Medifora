package com.misolova.medifora.util.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.misolova.medifora.R
import com.misolova.medifora.data.source.local.entities.QuestionAnswerEntity
import com.misolova.medifora.data.source.local.entities.QuestionEntity
import com.misolova.medifora.data.source.local.entities.UserQuestionAnswersEntity
import com.misolova.medifora.domain.model.Question
import com.misolova.medifora.util.inflate
import kotlinx.android.synthetic.main.fragment_home_item.view.*

class HomeFeedAdapter(private val userID: Int,
    private val homeQuestionsArrayList: List<UserQuestionAnswersEntity>,
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

    override fun getItemCount() = homeQuestionsArrayList.size

    override fun onBindViewHolder(holder: HomeFeedAdapter.HomeFeedViewHolder, position: Int) {
        val userQuestionAnswersEntity = homeQuestionsArrayList.find{
            it.user.userID == userID
        }
        val questionAnswersEntity = userQuestionAnswersEntity?.questions?.get(position)
        holder.itemView.tvHomeFeedQuestionItem.text = questionAnswersEntity?.question?.questionContent
        holder.itemView.tvHomeFeedAnswerAuthorItem.text = questionAnswersEntity?.question?.questionAuthorID.toString()
        holder.itemView.tvHomeFeedAnswersItem.text = questionAnswersEntity?.answers?.random()?.content
    }

}