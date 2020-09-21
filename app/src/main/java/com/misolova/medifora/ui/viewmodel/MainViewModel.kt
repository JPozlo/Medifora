package com.misolova.medifora.ui.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.misolova.medifora.data.repo.MediforaRepository
import com.misolova.medifora.data.source.local.entities.AnswerEntity
import com.misolova.medifora.data.source.local.entities.QuestionEntity
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    private val mediforaRepository: MediforaRepository
): ViewModel() {

    init {

    }
    private val userID = 1028

    private val currentUser = mediforaRepository.getUserDetails(userID)

    val userWithQuestionsAndAnswers = mediforaRepository.getUserWithQuestionsAnswers()
    val questionWithAnswers = mediforaRepository.getQuestionsWithZeroAnswers()
    val userWithAnswers = mediforaRepository.getUserWithAnswers()

    private val answerRequestQuestions = mediforaRepository.getQuestionsWithZeroAnswers()
    val homeFeedQuestions = mediforaRepository.getQuestionsSortByNumberOfAnswers()

    private val questionsSortByNumberOfAnswers = mediforaRepository.getQuestionsSortByNumberOfAnswers()

    private val userQuestions = mediforaRepository.getUserQuestions(userID)

    val questions = MediatorLiveData<List<QuestionEntity>>()

    fun getAnswer(id: Int) = mediforaRepository.getAnswer(id)

    fun getQuestion(id: Int) = mediforaRepository.getQuestion(id)

    fun getUserInfo() = mediforaRepository.getUserDetails(userID)

    fun getAnswersToQuestion(quizID: Int) = mediforaRepository.getAnswersToQuestion(quizID)

    fun addQuestion(questionEntity: QuestionEntity) = viewModelScope.launch {
        mediforaRepository.insertQuestion(questionEntity)
    }

    fun addAnswer(answerEntity: AnswerEntity) = viewModelScope.launch {
        mediforaRepository.insertAnswer(answerEntity)
    }

    fun deleteQuestion(questionEntity: QuestionEntity) = viewModelScope.launch {
        mediforaRepository.deleteQuestion(questionEntity)
    }

    fun deleteAnswer(answerEntity: AnswerEntity) = viewModelScope.launch {
        mediforaRepository.deleteAnswer(answerEntity)
    }

}