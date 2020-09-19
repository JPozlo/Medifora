package com.misolova.medifora.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.misolova.medifora.data.source.local.entities.AnswerEntity
import com.misolova.medifora.data.source.local.entities.QuestionEntity
import com.misolova.medifora.data.source.local.entities.UserEntity

@Dao
interface MediforaDao {

    /**
     * DAO functions for AnswerEntity
     * */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnswer(answerEntity: AnswerEntity)

    @Delete
    suspend fun deleteAnswer(answerEntity: AnswerEntity)

    @Query("SELECT * FROM answer_table WHERE questionID == :questionID")
    fun getAnswersToQuestion(questionID: Int): LiveData<List<AnswerEntity>>

    @Query("SELECT * FROM answer_table WHERE authorID == :userID")
    fun getUserAnswers(userID: Int): LiveData<List<AnswerEntity>>

    @Query("SELECT * FROM answer_table")
    fun getAllAnswers(): LiveData<List<AnswerEntity>>

    @Query("SELECT * FROM answer_table ORDER BY votes DESC")
    fun getAnswersSortByVotes(): LiveData<List<AnswerEntity>>

    @Query("SELECT * FROM answer_table ORDER BY createdAt DESC")
    fun getAnswersSortByDateCreated(): LiveData<List<AnswerEntity>>

    @Query("SELECT * FROM answer_table WHERE id == :answerID")
    fun getAnswerVotes(answerID: Int): LiveData<Int>

    /**
     * DAO functions for QuestionEntity
     * */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(questionEntity: QuestionEntity)

    @Delete
    suspend fun deleteQuestion(questionEntity: QuestionEntity)

    @Query("SELECT * FROM question_table WHERE totalNumberOfAnswers <= 0")
    fun getQuestionsWithZeroAnswers(): LiveData<List<QuestionEntity>>

    @Query("SELECT * FROM question_table WHERE authorID == :userID")
    fun getUserQuestions(userID: Int): LiveData<List<QuestionEntity>>

    @Query("SELECT * FROM question_table")
    fun getAllQuestions(): LiveData<List<QuestionEntity>>

    @Query("SELECT * FROM question_table ORDER BY totalNumberOfAnswers DESC")
    fun getQuestionsSortByNumberOfAnswers(): LiveData<List<QuestionEntity>>

    @Query("SELECT * FROM question_table ORDER BY createdAt DESC")
    fun getQuestionsSortByDateCreated(): LiveData<List<QuestionEntity>>

    /**
     * DAO functions for UserEntity
     * */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userEntity: UserEntity)

    @Delete
    suspend fun deleteUser(userEntity: UserEntity)

    @Query("SELECT * FROM user_table WHERE id == :userID")
    fun getUserDetails(userID: Int): UserEntity
}