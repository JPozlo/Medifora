package com.misolova.medifora.data.repo

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.misolova.medifora.data.source.local.entities.*
import com.misolova.medifora.domain.model.AnswerInfo

interface MediforaRepositoryInterface {

    /**
     * Functions for AnswerEntity
     * */

    suspend fun insertAnswer(answerEntity: AnswerEntity)

    fun createAnswer(answer: AnswerInfo, answerId: String): Task<Void>

    suspend fun deleteAnswer(answerEntity: AnswerEntity)

    fun deleteAnswer(id: String): Task<Void>

    fun getAnswer(id: Int): LiveData<AnswerEntity>

    fun getAnswersToQuestion(questionID: Int): LiveData<List<AnswerEntity>>

    fun getUserAnswers(userID: Int): LiveData<List<AnswerEntity>>

    fun getAllAnswers(): LiveData<List<AnswerEntity>>

    fun getAnswersSortByVotes(): LiveData<List<AnswerEntity>>

    fun getAnswersSortByDateCreated(): LiveData<List<AnswerEntity>>

//    fun getAnswerVotes(answerID: Int): LiveData<Int>

    /**
     * Functions for QuestionEntity
     * */

    suspend fun insertQuestion(questionEntity: QuestionEntity)

    fun createQuestion(     questionId: String,
                            content: String,
                            userId: String,
                            author: String): Task<Void>

    suspend fun deleteQuestion(questionEntity: QuestionEntity)

    fun deleteQuestion(id: String): Task<Void>

    fun getQuestion(id: Int): LiveData<QuestionEntity>

    fun getQuestionsWithZeroAnswers(): LiveData<List<QuestionEntity>>

    fun getUserQuestions(userID: Int): LiveData<List<QuestionEntity>>

    fun getAllQuestions(): LiveData<List<QuestionEntity>>

    fun getTotalNumberOfAnswers(questionID: Int): LiveData<Int>

    fun getQuestionsSortByNumberOfAnswers(): LiveData<List<QuestionEntity>>

    fun getQuestionsSortByDateCreated(): LiveData<List<QuestionEntity>>

    /**
     * Functions for UserEntity
     * */

    suspend fun insertUser(userEntity: UserEntity)

    fun createUser(id: String, name: String, email: String): Task<Void>

    suspend fun deleteUser(userEntity: UserEntity)

    fun getCurrentUser(): FirebaseUser?

    fun updateEmail(email: String, id: String): Task<Void>

    fun updateName(name: String, id: String): Task<Void>

    fun deleteAccount(id: String): Task<Void>?

    fun getUserDetails(userID: Int): LiveData<UserEntity>

    /**
     * Functions for Nested Relationship
     * */

    fun getUserWithQuestionsAnswers(): LiveData<List<UserQuestionAnswersEntity>>

//    fun getQuestionWithAnswers(): LiveData<List<QuestionAnswerEntity>>

    fun getUserWithAnswers(): LiveData<List<UserAnswerEntity>>

    fun getAllUsers(): LiveData<List<String>>

    /**
     * Functions for Nested Relationship
     * */

    fun login(email: String, password: String): Task<AuthResult>

    fun register(email: String, password: String): Task<AuthResult>

    fun currentUser(): FirebaseUser?

    fun logout()


}