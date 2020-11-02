package com.misolova.medifora.data.repo

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import com.misolova.medifora.data.source.local.MediforaDao
import com.misolova.medifora.data.source.local.entities.*
import com.misolova.medifora.data.source.remote.FirebaseSource
import com.misolova.medifora.domain.model.AnswerInfo
import javax.inject.Inject

class MediforaRepository @Inject constructor(private val mediforaDao: MediforaDao,
private val firebaseSource: FirebaseSource) :
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
    override fun createUser(id: String, name: String, email: String) =
        firebaseSource.createUser(id, name, email)

    override fun createAnswer(answer: AnswerInfo, answerId: String): Task<Void> = firebaseSource.createAnswer(answer, answerId)

    override fun deleteAnswer(id: String): Task<Void> = firebaseSource.deleteAnswer(id)

    override fun createQuestion(
        questionId: String,
        content: String,
        userId: String,
        author: String
    ): Task<Void>  = firebaseSource.createQuestion(questionId, content, userId, author)

    override fun deleteQuestion(id: String): Task<Void> = firebaseSource.deleteQuestion(id)
    override fun deleteAccount(id: String): Task<Void>? = firebaseSource.deleteAccount(id)

    override suspend fun deleteUser(userEntity: UserEntity) = mediforaDao.deleteUser(userEntity)

    override fun getUserDetails(userID: Int): LiveData<UserEntity> = mediforaDao.getUserDetails(userID)

    override fun getUserWithQuestionsAnswers(): LiveData<List<UserQuestionAnswersEntity>> = mediforaDao.getUserWithQuestionsAndAnswers()
//    override fun getQuestionWithAnswers(): LiveData<List<QuestionAnswerEntity>> = mediforaDao.getQuestionWithAnswers()
    override fun getUserWithAnswers(): LiveData<List<UserAnswerEntity>> = mediforaDao.getUserWithAnswers()
    override fun getAllUsers(): LiveData<List<String>> = mediforaDao.getAllUsernames()


    override fun login(email: String, password: String) = firebaseSource.login(email, password)
    override  fun register(email: String, password: String) = firebaseSource.register(email, password)
    override fun currentUser() = firebaseSource.currentUser()
    override fun logout() = firebaseSource.logout()
}