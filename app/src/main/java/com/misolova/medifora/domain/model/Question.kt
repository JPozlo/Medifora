package com.misolova.medifora.domain.model

import android.os.Parcelable
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.parcel.Parcelize
import timber.log.Timber
import kotlin.time.ExperimentalTime
import kotlin.time.milliseconds


data class Question(
    val questionInfo: QuestionInfo,
    val answers: List<Answer>? = null
)

@Parcelize
data class QuestionInfo(
    val questionId: String,
    val questionContent: String,
    val totalNumberOfAnswers: Int,
    val questionAuthorID: String,
    val questionCreatedAt: Long
): Parcelable {
    @ExperimentalTime
    companion object {
        @ExperimentalTime
        fun DocumentSnapshot.toQuestionInfo(): QuestionInfo?{
            try {
//                var answer: List<MutableCollection<Any>>
                val content = getString("questionContent")!!
                val totalNumberOfAnswers = getLong("totalNumberOfAnswers")!!.toInt()
                val authorID = getString("questionAuthorID")!!
                val createdAt = getTimestamp("questionCreatedAt")?.seconds?.milliseconds?.toLongMilliseconds()!!
//                this.getDocumentReference(id)?.collection("answers")?.get()
//                    ?.addOnCompleteListener {
//                        if(it.isSuccessful){
//                            for(doc in it.result!!){
//                                answer = listOf(doc.data.values)
//                            }
//                        }
//                    }
                return QuestionInfo(questionId = id, questionContent = content, totalNumberOfAnswers = totalNumberOfAnswers, questionAuthorID = authorID, questionCreatedAt = createdAt)
            } catch (e: Exception) {
                Timber.d("Error converting question profile: $e")
                FirebaseCrashlytics.getInstance().log("Error converting question profile")
                FirebaseCrashlytics.getInstance().setCustomKey("questionID", id)
                FirebaseCrashlytics.getInstance().recordException(e)
                return null
            }
        }
        fun DocumentSnapshot.toQuestion(): Question?{
            try {
//                var answer: List<MutableCollection<Any>>
                val content = getString("questionContent")!!
                val totalNumberOfAnswers = getLong("totalNumberOfAnswers")!!.toInt()
                val authorID = getString("questionAuthorID")!!
                val createdAt = getTimestamp("questionCreatedAt")?.seconds?.milliseconds?.toLongMilliseconds()!!
//                this.getDocumentReference(id)?.collection("answers")?.get()
//                    ?.addOnCompleteListener {
//                        if(it.isSuccessful){
//                            for(doc in it.result!!){
//                                answer = listOf(doc.data.values)
//                            }
//                        }
//                    }
                return Question(QuestionInfo(questionId = id, questionContent = content, totalNumberOfAnswers = totalNumberOfAnswers, questionAuthorID = authorID, questionCreatedAt = createdAt))
            } catch (e: Exception) {
                Timber.d("Error converting question profile: $e")
                FirebaseCrashlytics.getInstance().log("Error converting question profile")
                FirebaseCrashlytics.getInstance().setCustomKey("questionID", id)
                FirebaseCrashlytics.getInstance().recordException(e)
                return null
            }
        }
        private const val TAG = "User"
    }
}