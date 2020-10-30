package com.misolova.medifora.domain.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.parcel.Parcelize
import timber.log.Timber
import kotlin.time.ExperimentalTime

data class Answer(
    val answerInfo: AnswerInfo,
    val question: Question? = null,
    val user: User? = null
)

@Parcelize
data class AnswerInfo(
    val answerId: String,
    val answerContent: String,
    val questionID: String,
    val answerAuthorID: String,
    val answerAuthor: String,
    val votes: Int,
    val answerCreatedAt: Timestamp
) : Parcelable {

    @ExperimentalTime
    companion object {
        fun DocumentSnapshot.toAnswerInfo(): AnswerInfo? {
            try {
                val content = getString("answerContent")!!
                val questionID = getString("questionID")!!
                val authorID = getString("answerAuthorID")!!
                val author = getString("answerAuthor")!!
                val votes = getLong("votes")?.toInt()!!
                val createdAt =
                    getTimestamp("answerCreatedAt")!!
                return AnswerInfo(
                    answerId = id,
                    answerContent = content,
                    answerCreatedAt = createdAt,
                    votes = votes,
                    answerAuthorID = authorID,
                    answerAuthor = author,
                    questionID = questionID
                )
            } catch (e: Exception) {
                Timber.d("Error converting answer profile: $e")
                FirebaseCrashlytics.getInstance().log("Error converting answer profile")
                FirebaseCrashlytics.getInstance().setCustomKey("answerID", id)
                FirebaseCrashlytics.getInstance().recordException(e)
                return null
            }
        }


        fun DocumentSnapshot.toAnswer(): Answer? {
            try {
                val content = getString("answerContent")!!
                val questionID = getString("questionID")!!
                val authorID = getString("answerAuthorID")!!
                val author = getString("answerAuthor")!!
                val votes = getLong("votes")?.toInt()
                val createdAt =
                    getTimestamp("answerCreatedAt")!!
                return Answer(
                    AnswerInfo(
                        answerId = id,
                        answerContent = content,
                        answerCreatedAt = createdAt,
                        votes = votes!!,
                        answerAuthorID = authorID,
                        answerAuthor = author,
                        questionID = questionID
                    )
                )
            } catch (e: Exception) {
                Timber.d("Error converting answer profile: $e")
                FirebaseCrashlytics.getInstance().log("Error converting answer profile")
                FirebaseCrashlytics.getInstance().setCustomKey("answerID", id)
                FirebaseCrashlytics.getInstance().recordException(e)
                return null
            }
        }

        private const val TAG = "User"
    }
}