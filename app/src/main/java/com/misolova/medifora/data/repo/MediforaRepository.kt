package com.misolova.medifora.data.repo

import androidx.lifecycle.LiveData
import com.misolova.medifora.data.source.local.MediforaDao
import com.misolova.medifora.data.source.local.entities.*
import javax.inject.Inject

class MediforaRepository @Inject constructor(private val mediforaDao: MediforaDao) :
    MediforaRepositoryInterface {

    override suspend fun insertAnswer(answerEntity: AnswerEntity) = mediforaDao.insertAnswer(answerEntity)

    override suspend fun deleteAnswer(answerEntity: AnswerEntity)  = mediforaDao.deleteAnswer(answerEntity)

    override fun getAnswer(id: Int): LiveData<AnswerEntity> = mediforaDao.getAnswer(id)

    override fun getAnswersToQuestion(questionID: Int): LiveData<List<AnswerEntity>> = mediforaDao.getAnswersToQuestion(questionID)

    override fun getUserAnswers(userID: Int): LiveData<List<AnswerEntity>>  = mediforaDao.getUserAnswers(userID)

    override fun getAllAnswers(): LiveData<List<AnswerEntity>> =  mediforaDao.getAllAnswers()

    override fun getAnswersSortByVotes(): LiveData<List<AnswerEntity>> =  mediforaDao.getAnswersSortByVotes()

    override fun getAnswersSortByDateCreated(): LiveData<List<AnswerEntity>>  = mediforaDao.getAnswersSortByDateCreated()

//    override fun getAnswerVotes(answerID: Int): LiveData<Int>  = mediforaDao.getAnswerVotes(answerID)

    override suspend fun insertQuestion(questionEntity: QuestionEntity) = mediforaDao.insertQuestion(questionEntity)

    override suspend fun deleteQuestion(questionEntity: QuestionEntity) = mediforaDao.deleteQuestion(questionEntity)

    override fun getQuestion(id: Int): LiveData<QuestionEntity> = mediforaDao.getQuestion(id)

    override fun getQuestionsWithZeroAnswers(): LiveData<List<QuestionEntity>>  = mediforaDao.getQuestionsWithZeroAnswers()

    override fun getUserQuestions(userID: Int): LiveData<List<QuestionEntity>> = mediforaDao.getUserQuestions(userID)

    override fun getAllQuestions(): LiveData<List<QuestionEntity>>  = mediforaDao.getAllQuestions()

    override fun getTotalNumberOfAnswers(questionID: Int): LiveData<Int> = mediforaDao.getTotalNumberOfAnswers(questionID)

    override fun getQuestionsSortByNumberOfAnswers(): LiveData<List<QuestionEntity>> = mediforaDao.getQuestionsSortByNumberOfAnswers()

    override fun getQuestionsSortByDateCreated(): LiveData<List<QuestionEntity>>  = mediforaDao.getQuestionsSortByDateCreated()

    override suspend fun insertUser(userEntity: UserEntity) = mediforaDao.insertUser(userEntity)

    override suspend fun deleteUser(userEntity: UserEntity) = mediforaDao.deleteUser(userEntity)

    override fun getUserDetails(userID: Int): LiveData<UserEntity> = mediforaDao.getUserDetails(userID)

    override fun getUserWithQuestionsAnswers(): LiveData<List<UserQuestionAnswersEntity>> = mediforaDao.getUserWithQuestionsAndAnswers()
//    override fun getQuestionWithAnswers(): LiveData<List<QuestionAnswerEntity>> = mediforaDao.getQuestionWithAnswers()
    override fun getUserWithAnswers(): LiveData<List<UserAnswerEntity>> = mediforaDao.getUserWithAnswers()
}