package com.misolova.medifora.data.source.remote

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.*
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
    val fireUser = FirebaseAuth.getInstance().currentUser

    fun createUser(name: String, email: String, photo: String?, userID: String): Flow<DocumentReference> {
        return callbackFlow {
            val user = UserInfo(userId = userID, name = name, email = email, accountCreatedAt = Timestamp.now(), photo = photo)
            val listenerRegistration = db.collection("users").document(userID)
                .set(user)
                .addOnSuccessListener {
                    Timber.d("$TAG: Successfully added user")
                }
                .addOnFailureListener {error ->
                    cancel(message = "Error adding user", cause = error)
                    Timber.e("$TAG: Exception caused by -> ${error.message}")
                    return@addOnFailureListener
                }
            awaitClose {
                Timber.d("$TAG: Cancelling create user listener")
            }
        }
    }

    suspend fun createAnswer(answer: AnswerInfo, questionId: String, userId: String, answerId: String): Flow<DocumentReference> {
        return callbackFlow {

            val listenerRegistration = db.collection("users/${userId}/questions/${questionId}/answers")
                .document(answerId).set(answer)
                .addOnSuccessListener { docRef ->
                    Timber.d("$TAG: Answer added successfully -> $docRef")
                }
                .addOnFailureListener { error ->
                    cancel(message = "Error adding answer", cause = error)
                    Timber.e("$TAG: Exception caused by -> ${error.message}")
                    return@addOnFailureListener
                }
            awaitClose {
                Timber.d("$TAG: Cancelling question listener")
            }
        }
    }


    suspend fun createQuestion(
        questionId: String,
        content: String,
        userId: String
    ): Flow<DocumentReference> {
        return callbackFlow {
            val question = QuestionInfo(
                questionId = questionId,
                questionContent = content,
                questionCreatedAt = Timestamp.now(),
                questionAuthorID = userId,
                totalNumberOfAnswers = 0
            )
            val listenerRegistration = db.collection("users")
                .document(userId).collection("questions").document(questionId).set(question)
                .addOnSuccessListener {
                    Timber.d("$TAG: Successfully added question")
                }
                .addOnFailureListener { error ->
                    cancel(message = "Error adding answer", cause = error)
                    Timber.e("$TAG: Exception caused by -> ${error.message}")
                    return@addOnFailureListener
                }
            awaitClose {
                Timber.d("$TAG: Cancelling question listener")
            }
        }
    }

    suspend fun getUserDetails(id: String): Flow<UserInfo?> {
        return callbackFlow {
            val listenerRegistration = db.collection("users").document(id)
                .get()
                .addOnSuccessListener {
                    Timber.d("$TAG: Successfully fetched user")
                }
                .addOnFailureListener { error ->
                    cancel(message = "Error fetching user", cause = error)
                    Timber.e("$TAG: Exception caused by -> ${error.message}")
                    return@addOnFailureListener
                }
                .addOnCompleteListener {
                    val map = it.result?.toUserInfo()
                    offer(map)
                }

            awaitClose {
                Timber.d("$TAG: Cancelling user details fetching listener")
            }
        }
    }

    fun getProfileData(userId: String): UserInfo? {
        return try {
            db.collection("users")
                .document(userId).get().result?.toUserInfo()
        } catch (e: Exception) {
            Timber.d("$TAG: Error getting user details -> $e")
            FirebaseCrashlytics.getInstance().log("Error getting user details")
            FirebaseCrashlytics.getInstance().setCustomKey("user id", userId)
            FirebaseCrashlytics.getInstance().recordException(e)
            null
        }
    }

    suspend fun getUserQuestions(userId: String): Flow<List<QuestionInfo>?> {
        return callbackFlow {
            val listenerRegistration = db.collection("users")
                .document(userId)
                .collection("questions")
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
            val listenerRegistration = db.collectionGroup("answers")
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
            val listenerRegistration = db.collectionGroup("questions")
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
            val listenerRegistration = db.collectionGroup("questions").whereEqualTo("totalNumberOfAnswers", 0)
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
            val listenerRegistration = db.collectionGroup("questions").orderBy("questionCreatedAt", Query.Direction.DESCENDING)
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

//    suspend fun getQuestionById(questionId: String, userId: String): Flow<QuestionInfo?>{
//        return callbackFlow {
//            val listenerRegistration = db.collection("users").document(userId)
//                .collection("questions").document(questionId)
//                .addSnapshotListener { value, error ->
//                    if(error != null){
//                        cancel(message = "Error fetching question", cause = error)
//                        return@addSnapshotListener
//                    }
//                    val map = value?.data?.to(QuestionInfo("", "", 0, "", 0001-01-01T00:00:00Z))
//                    val second = map?.second
//                    offer(second)
//                }
//            awaitClose {
//                Timber.d("$TAG: Cancelling question listener")
//                listenerRegistration.remove()
//            }
//        }
//    }

    suspend fun getAnswersToQuestion(questionId: String, userId: String): Flow<List<AnswerInfo>?> {
        return callbackFlow {
            val listenerRegistration = db.collection("users").document(userId)
                .collection("questions").document(questionId).collection("answers")
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

//    suspend fun getAnswersToQuestion(questionID: String): CollectionReference {
//        return db.collection("users").document(FirebaseAuth.getInstance().currentUser!!.uid)
//            .collection("questions").document(questionID).collection("answers")
//    }
}