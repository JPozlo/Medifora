package com.misolova.medifora.data.repo

import androidx.lifecycle.LiveData
import com.misolova.medifora.data.source.local.entities.AnswerEntity
import com.misolova.medifora.data.source.local.entities.QuestionEntity
import com.misolova.medifora.data.source.local.entities.UserEntity

interface MediforaRepositoryInterface {

    /**
     * Functions for AnswerEntity
     * */

    suspend fun insertAnswer(answerEntity: AnswerEntity)

    suspend fun deleteAnswer(answerEntity: AnswerEntity)

    fun getAnswersToQuestion(questionID: Int): LiveData<List<AnswerEntity>>

    fun getUserAnswers(userID: Int): LiveData<List<AnswerEntity>>

    fun getAllAnswers(): LiveData<List<AnswerEntity>>

    fun getAnswersSortByVotes(): LiveData<List<AnswerEntity>>

    fun getAnswersSortByDateCreated(): LiveData<List<AnswerEntity>>

    fun getAnswerVotes(answerID: Int): LiveData<Int>

    /**
     * Functions for QuestionEntity
     * */

    suspend fun insertQuestion(questionEntity: QuestionEntity)

    suspend fun deleteQuestion(questionEntity: QuestionEntity)

    fun getQuestionsWithZeroAnswers(): LiveData<List<QuestionEntity>>

    fun getUserQuestions(userID: Int): LiveData<List<QuestionEntity>>

    fun getAllQuestions(): LiveData<List<QuestionEntity>>

    fun getQuestionsSortByNumberOfAnswers(): LiveData<List<QuestionEntity>>

    fun getQuestionsSortByDateCreated(): LiveData<List<QuestionEntity>>

    /**
     * Functions for UserEntity
     * */

    suspend fun insertUser(userEntity: UserEntity)

    suspend fun deleteUser(userEntity: UserEntity)

    fun getUserDetails(userID: Int): UserEntity
}