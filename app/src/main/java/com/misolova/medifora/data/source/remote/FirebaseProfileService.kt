package com.misolova.medifora.data.source.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.misolova.medifora.domain.model.AnswerInfo
import com.misolova.medifora.domain.model.AnswerInfo.Companion.toAnswerInfo
import com.misolova.medifora.domain.model.QuestionInfo
import com.misolova.medifora.domain.model.QuestionInfo.Companion.toQuestionInfo
import com.misolova.medifora.domain.model.User
import com.misolova.medifora.domain.model.UserInfo
import com.misolova.medifora.domain.model.UserInfo.Companion.toUser
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

    fun createUser(user: UserInfo){
        try {
           db.collection("users")
               .add(user)
                .addOnSuccessListener {documentReference ->
                    Timber.d("$TAG: The document id is -> ${documentReference.toString()}")
                }.addOnFailureListener{e ->
                    Timber.e("$TAG: Failure adding document -> $e")
                }
        } catch (e: Exception){
            Timber.e("$TAG: Error adding  document -> $e")
        }
    }

    suspend fun createAnswer(answer: AnswerInfo, questionId: String): Flow<DocumentReference> {
        return callbackFlow {
            val listenerRegistration = db.collection("users")
                .document(fireUser?.uid!!).collection("questions")
                .document(questionId).collection("answers").document().set(answer)
                .addOnSuccessListener { docRef ->
                    Timber.d("$TAG: Answer added successfully -> $docRef")
                }
                .addOnFailureListener {error ->
                    cancel(message = "Error adding answer", cause = error)
                    Timber.e("$TAG: Exception caused by -> ${error.message}")
                    return@addOnFailureListener
                }
            awaitClose {
                Timber.d("$TAG: Cancelling question listener")
            }
        }
    }

    suspend fun createQuestion(question: QuestionInfo): Flow<DocumentReference> {
        return callbackFlow {
            val id = db.collection("users").document(fireUser?.uid!!).collection("questions")
                .document().id
            val listenerRegistration = db.collection("users")
                .document(fireUser?.uid!!).collection("questions").document(id).set(question)
                .addOnSuccessListener {
                    Timber.d("$TAG: Successfully added question -> ${it.toString()}")
                }
                .addOnFailureListener {error ->
                    cancel(message = "Error adding answer", cause = error)
                    Timber.e("$TAG: Exception caused by -> ${error.message}")
                    return@addOnFailureListener
                }
            awaitClose {
                Timber.d("$TAG: Cancelling question listener")
            }
        }
    }

    suspend fun getProfileData(userId: String): User? {
        return try {
            db.collection("users")
                .document(userId).get().result?.toUser()
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
                        cancel(message = "Error fetching user questions",
                            cause = firebaseFirestoreException)
                        return@addSnapshotListener
                    }
                    val map = querySnapshot?.documents?.mapNotNull { it.toQuestionInfo() }
                    offer(map)
                }
            awaitClose {
                Timber.d( "$TAG: Cancelling User Questions listener")
                listenerRegistration.remove()
            }
        }
    }

    suspend fun getUserAnswers(userId: String): Flow<List<AnswerInfo>?> {
        return callbackFlow {
            val listenerRegistration = db.collection("users")
                .document(userId)
                .collection("answers")
                .addSnapshotListener{value: QuerySnapshot?, error: FirebaseFirestoreException? ->
                    if(error != null){
                        cancel(message = "Error fetching user answers", cause = error)
                        return@addSnapshotListener
                    }
                    val map = value?.documents?.mapNotNull { it.toAnswerInfo() }
                    offer(map)
                }
            awaitClose{
                Timber.d("$TAG: Cancelling User Answers listener")
                listenerRegistration.remove()
            }
        }
    }

    suspend fun getQuestions(): Flow<List<QuestionInfo>?>{
        return callbackFlow {
            val listenerRegistration = db.collectionGroup("questions")
                .addSnapshotListener { querySnapshot: QuerySnapshot?, firebaseFirestoreException: FirebaseFirestoreException? ->
                    if (firebaseFirestoreException != null) {
                        cancel(message = "Error fetching questions",
                            cause = firebaseFirestoreException)
                        return@addSnapshotListener
                    }
                    val map = querySnapshot?.documents?.mapNotNull { it.toQuestionInfo() }
                    offer(map)
                }
            awaitClose {
                Timber.d( "$TAG: Cancelling User Questions listener")
                listenerRegistration.remove()
            }
        }
    }

    suspend fun getQuestionById(questionId: String): Flow<QuestionInfo?>{
        return callbackFlow {
            val listenerRegistration = db.collection("users").document(fireUser?.uid!!)
                .collection("questions").document(questionId)
                .addSnapshotListener { value, error ->
                    if(error != null){
                        cancel(message = "Error fetching question", cause = error)
                        return@addSnapshotListener
                    }
                    val map = value?.data?.to(QuestionInfo("", "", 0, "", 0L))
                    val second = map?.second
                    offer(second)
                }
            awaitClose {
                Timber.d("$TAG: Cancelling question listener")
                listenerRegistration.remove()
            }
        }
    }

    suspend fun getAnswersToQuestion(questionId: String): Flow<List<AnswerInfo>?>{
        return callbackFlow {
            val listenerRegistration = db.collection("users").document(fireUser?.uid!!)
            .collection("questions").document(questionId).collection("answers")
                .addSnapshotListener{ value: QuerySnapshot?, error: FirebaseFirestoreException? ->
                    if(error != null){
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