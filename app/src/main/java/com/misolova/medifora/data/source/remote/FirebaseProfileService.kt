package com.misolova.medifora.data.source.remote

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.misolova.medifora.domain.model.AnswerInfo
import com.misolova.medifora.domain.model.AnswerInfo.Companion.toAnswerInfo
import com.misolova.medifora.domain.model.QuestionInfo
import com.misolova.medifora.domain.model.QuestionInfo.Companion.toQuestionInfo
import com.misolova.medifora.domain.model.UserInfo
import com.misolova.medifora.domain.model.UserInfo.Companion.toUserInfo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalCoroutinesApi
object FirebaseProfileService {
    private const val TAG = "FirebaseProfileService"

    val db = FirebaseFirestore.getInstance()

    suspend fun getUserQuestions(userId: String): Flow<List<QuestionInfo>?> {
        return callbackFlow {
            val listenerRegistration = db.collection("questions")
                .whereEqualTo("questionAuthorID", userId)
                .addSnapshotListener { querySnapshot: QuerySnapshot?, firebaseFirestoreException: FirebaseFirestoreException? ->
                    if (firebaseFirestoreException != null) {
                        cancel(
                            message = "Error fetching user questions",
                            cause = firebaseFirestoreException
                        )
                        return@addSnapshotListener
                    }
                    val map = querySnapshot?.documents?.mapNotNull { it.toQuestionInfo() }
                    offer(map)
                }
            awaitClose {
                Timber.d("$TAG: Cancelling User Questions listener")
                listenerRegistration.remove()
            }
        }
    }

    suspend fun getUserAnswers(userId: String): Flow<List<AnswerInfo>?> {
        return callbackFlow {
            val listenerRegistration = db.collection("answers")
                .whereEqualTo("answerAuthorID", userId)
                .addSnapshotListener { value: QuerySnapshot?, error: FirebaseFirestoreException? ->
                    if (error != null) {
                        cancel(message = "Error fetching user answers", cause = error)
                        return@addSnapshotListener
                    }
                    val map = value?.documents?.mapNotNull { it.toAnswerInfo() }
                    offer(map)
                }
            awaitClose {
                Timber.d("$TAG: Cancelling User Answers listener")
                listenerRegistration.remove()
            }
        }
    }

    suspend fun getQuestions(): Flow<List<QuestionInfo>?> {
        return callbackFlow {
            val listenerRegistration = db.collection("questions")
                .addSnapshotListener { querySnapshot: QuerySnapshot?, firebaseFirestoreException: FirebaseFirestoreException? ->
                    if (firebaseFirestoreException != null) {
                        cancel(
                            message = "Error fetching questions",
                            cause = firebaseFirestoreException
                        )
                        return@addSnapshotListener
                    }
                    val map = querySnapshot?.documents?.mapNotNull { it.toQuestionInfo() }
                    offer(map)
                }
            awaitClose {
                Timber.d("$TAG: Cancelling All Questions listener")
                listenerRegistration.remove()
            }
        }
    }

    suspend fun getQuestionsWithZeroAnswers(): Flow<List<QuestionInfo>?>{
        return callbackFlow {
            val listenerRegistration = db.collection("questions").whereEqualTo("totalNumberOfAnswers", 0)
                .addSnapshotListener { querySnapshot: QuerySnapshot?, firebaseFirestoreException: FirebaseFirestoreException? ->
                    if (firebaseFirestoreException != null) {
                        cancel(
                            message = "Error fetching questions",
                            cause = firebaseFirestoreException
                        )
                        return@addSnapshotListener
                    }
                    val map = querySnapshot?.documents?.mapNotNull { it.toQuestionInfo() }
                    offer(map)
                }
            awaitClose {
                Timber.d("$TAG: Cancelling All Questions listener")
                listenerRegistration.remove()
            }
        }
    }

    suspend fun getQuestionsSortByDate(): Flow<List<QuestionInfo>?>{
        return callbackFlow {
            val listenerRegistration = db.collection("questions").orderBy("questionCreatedAt", Query.Direction.DESCENDING)
                .addSnapshotListener { querySnapshot: QuerySnapshot?, firebaseFirestoreException: FirebaseFirestoreException? ->
                    if (firebaseFirestoreException != null) {
                        cancel(
                            message = "Error fetching questions",
                            cause = firebaseFirestoreException
                        )
                        return@addSnapshotListener
                    }
                    val map = querySnapshot?.documents?.mapNotNull { it.toQuestionInfo() }
                    offer(map)
                }
            awaitClose {
                Timber.d("$TAG: Cancelling All Questions listener")
                listenerRegistration.remove()
            }
        }
    }

    suspend fun getQuestionById(questionId: String): Flow<QuestionInfo?>{
        return callbackFlow {
            val listenerRegistration = db.collection("questions").whereEqualTo("questionId", questionId)
                .addSnapshotListener { value, error ->
                    if(error != null){
                        cancel(message = "Error fetching question", cause = error)
                        return@addSnapshotListener
                    }
                    val map = value?.documents?.mapNotNull { it.toQuestionInfo() }
                    val quiz = map?.first()
                    Timber.d("$TAG: THe question is -> $quiz")
                    offer(quiz)
                }
            awaitClose {
                Timber.d("$TAG: Cancelling question listener")
                listenerRegistration.remove()
            }
        }
    }

    suspend fun getAnswersToQuestion(questionId: String): Flow<List<AnswerInfo>?> {
        return callbackFlow {
            val listenerRegistration = db.collection("answers").whereEqualTo("questionID", questionId)
                .addSnapshotListener { value: QuerySnapshot?, error: FirebaseFirestoreException? ->
                    if (error != null) {
                        cancel(message = "Error fetching answers to question", cause = error)
                        return@addSnapshotListener
                    }
                    val map = value?.documents?.mapNotNull { it.toAnswerInfo() }
                    offer(map)
                }
            awaitClose {
                Timber.d("$TAG: Cancelling answers to question listener")
                listenerRegistration.remove()
            }
        }
    }

    suspend fun getUserById(id: String): Flow<UserInfo?>{
        return callbackFlow {
            val listenerRegistration = db.collection("users").whereEqualTo("userId", id).get().addOnCompleteListener {task: Task<QuerySnapshot> ->
                if(!task.isSuccessful){
                    return@addOnCompleteListener
                }
                val map = task.result?.documents?.mapNotNull { it.toUserInfo() }
                if(map?.isEmpty()!!){
                    return@addOnCompleteListener
                }
                val user = map?.first()
                Timber.d("$TAG: The user is -> $user")
                offer(user)
            }
            awaitClose {
                Timber.d("$TAG: Cancelling user information listener")
            }
//            .addSnapshotListener { value, error ->
//                if(error != null){
//                    cancel(message = "Error fetching user", cause = error)
//                    return@addSnapshotListener
//                }
//                val map = value?.documents?.mapNotNull { it.toUserInfo() }
//                val user = map?.first()
//                Timber.d("$TAG: THe question is -> $user")
//                offer(user)
//            }
        }
    }

    fun deleteQuestion(id: String): Task<Void> {
       return db.collection("questions").document(id).delete()
    }
}