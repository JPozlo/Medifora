package com.misolova.medifora.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.misolova.medifora.data.source.local.entities.*

@Dao
interface MediforaDao {

    /**
     * DAO functions for AnswerEntity
     * */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnswer(answerEntity: AnswerEntity)

    @Delete
    suspend fun deleteAnswer(answerEntity: AnswerEntity)

    @Query("SELECT * FROM answer_table WHERE answerID == :id")
    fun getAnswer(id: Int): LiveData<AnswerEntity>

    @Query("SELECT * FROM answer_table WHERE answerQuestionID == :questionID")
    fun getAnswersToQuestion(questionID: Int): LiveData<List<AnswerEntity>>

    @Query("SELECT * FROM answer_table WHERE answerAuthorID == :userID")
    fun getUserAnswers(userID: Int): LiveData<List<AnswerEntity>>

    @Query("SELECT * FROM answer_table")
    fun getAllAnswers(): LiveData<List<AnswerEntity>>

    @Query("SELECT * FROM answer_table ORDER BY votes DESC")
    fun getAnswersSortByVotes(): LiveData<List<AnswerEntity>>

    @Query("SELECT * FROM answer_table ORDER BY answerCreatedAt DESC")
    fun getAnswersSortByDateCreated(): LiveData<List<AnswerEntity>>

//    @Query("SELECT * FROM answer_table WHERE answerID == :answerID")
//    fun getAnswerVotes(answerID: Int): LiveData<Int>

    /**
     * DAO functions for QuestionEntity
     * */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(questionEntity: QuestionEntity)

    @Delete
    suspend fun deleteQuestion(questionEntity: QuestionEntity)

    @Query("SELECT * FROM question_table WHERE rowid == :id")
    fun getQuestion(id: Int): LiveData<QuestionEntity>

    @Query("SELECT * FROM question_table WHERE totalNumberOfAnswers <= 0")
    fun getQuestionsWithZeroAnswers(): LiveData<List<QuestionEntity>>

    @Query("SELECT * FROM question_table WHERE questionAuthorID == :userID")
    fun getUserQuestions(userID: Int): LiveData<List<QuestionEntity>>

    @Query("SELECT * FROM question_table")
    fun getAllQuestions(): LiveData<List<QuestionEntity>>

    @Query("SELECT COUNT(questionAnswerIDs) FROM question_table WHERE rowid == :questionID")
    fun getTotalNumberOfAnswers(questionID: Int): LiveData<Int>

    @Query("SELECT * FROM question_table ORDER BY totalNumberOfAnswers DESC")
    fun getQuestionsSortByNumberOfAnswers(): LiveData<List<QuestionEntity>>

    @Query("SELECT * FROM question_table ORDER BY questionCreatedAt DESC")
    fun getQuestionsSortByDateCreated(): LiveData<List<QuestionEntity>>

    /**
     * DAO functions for UserEntity
     * */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userEntity: UserEntity)

    @Delete
    suspend fun deleteUser(userEntity: UserEntity)

    @Query("SELECT * FROM user_table WHERE userID == :userID")
    fun getUserDetails(userID: Int): LiveData<UserEntity>

    @Query("SELECT email FROM user_table")
    fun getAllUsernames(): LiveData<List<String>>

    /**
     * DAO functions for Relational Entities
     * */

    @Transaction
    @Query("SELECT * FROM user_table")
    fun getUserWithQuestionsAndAnswers(): LiveData<List<UserQuestionAnswersEntity>>

//    @Transaction
//    @Query("SELECT * FROM question_table WHERE questionAuthorID == 0")
//    fun getQuestionWithAnswers(): LiveData<List<QuestionAnswerEntity>>

    @Transaction
    @Query("SELECT * FROM user_table")
    fun getUserWithAnswers(): LiveData<List<UserAnswerEntity>>



}
