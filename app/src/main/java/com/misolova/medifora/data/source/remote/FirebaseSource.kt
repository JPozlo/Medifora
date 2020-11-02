package com.misolova.medifora.data.source.remote

import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.misolova.medifora.domain.model.AnswerInfo
import com.misolova.medifora.domain.model.QuestionInfo
import timber.log.Timber

class FirebaseSource {

    companion object{
        private const val TAG = "FIREBASE SOURCE"
    }

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val db = FirebaseFirestore.getInstance()

    fun login(email: String, password: String) =
        firebaseAuth.signInWithEmailAndPassword(email, password)

    fun register(email: String, password: String) =
        firebaseAuth.createUserWithEmailAndPassword(email, password)

    fun logout() = firebaseAuth.signOut()

    fun currentUser() = firebaseAuth.currentUser

    fun createUser(id: String, name: String, email: String): Task<Void> {
        val user = com.misolova.medifora.domain.model.UserInfo(
            userId = id,
            name = name,
            email = email,
            accountCreatedAt = Timestamp.now()
        )
        return db.collection("users").document(id).set(user)
    }

    fun createQuestion(
        questionId: String,
        content: String,
        userId: String,
        author: String
    ): Task<Void> {
        val question = QuestionInfo(
            questionId = questionId,
            questionContent = content,
            questionCreatedAt = Timestamp.now(),
            questionAuthorID = userId,
            questionAuthor = author,
            totalNumberOfAnswers = 0
        )
        return db.collection("questions").document(userId).set(question)
    }

    fun createAnswer(answer: AnswerInfo, answerId: String): Task<Void> {
        return db.collection("answers").document(answerId).set(answer)
    }

    fun deleteQuestion(id: String): Task<Void> {
        return db.collection("questions").document(id).delete()
    }

    fun deleteAnswer(id: String): Task<Void> {
        return db.collection("answers").document(id).delete()
    }

    fun deleteAccount(id: String): Task<Void>? {
        return currentUser()?.delete()?.addOnSuccessListener {
            db.collection("users").document(id).delete()
        }?.addOnFailureListener { e ->
            Timber.e("$TAG: User could not be deleted")
        }
    }


}