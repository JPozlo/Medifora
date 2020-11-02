package com.misolova.medifora.ui.home.viewmodel

import android.content.SharedPreferences
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.misolova.medifora.data.repo.MediforaRepository
import com.misolova.medifora.data.source.remote.FirebaseProfileService
import com.misolova.medifora.domain.model.AnswerInfo
import com.misolova.medifora.domain.model.QuestionInfo
import com.misolova.medifora.domain.model.User
import com.misolova.medifora.domain.model.UserInfo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
@ExperimentalTime
class MainViewModel @ViewModelInject constructor(
    private val mediforaRepository: MediforaRepository
) : ViewModel() {

    companion object {
        private const val TAG = "MAIN_VIEW_MODEL"
    }

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private val userDetails = MutableLiveData<UserInfo>()
    fun getUserDetails() = userDetails

    private val questionID = MutableLiveData<String>()
    fun setQuestionId(questionId: String) {
        questionID.setValue(questionId)
    }
    fun getQuestionId() = questionID.value!!

    private val _userProfile = MutableLiveData<User>()
    val userProfile: LiveData<User> = _userProfile
    private val _homeFeedQuestions = MutableLiveData<List<QuestionInfo>>()
    val homeFeedQuestions: LiveData<List<QuestionInfo>> = _homeFeedQuestions
    private val _allQuestions = MutableLiveData<List<QuestionInfo>>()
    val allQuestions: LiveData<List<QuestionInfo>> = _allQuestions
    private val _questionsByCreationDate = MutableLiveData<List<QuestionInfo>>()
    val questionsByCreationDate: LiveData<List<QuestionInfo>> = _questionsByCreationDate
    private val _questionsWithoutAnswers = MutableLiveData<List<QuestionInfo>>()
    val questionsWithoutAnswers: LiveData<List<QuestionInfo>> = _questionsWithoutAnswers
    private val _answers = MutableLiveData<List<AnswerInfo>>()
    val answers: LiveData<List<AnswerInfo>> = _answers
    private val _answersToQuiz = MutableLiveData<List<AnswerInfo>>()
    val answersToQuiz: LiveData<List<AnswerInfo>> = _answersToQuiz
    private val _userQuestions = MutableLiveData<List<QuestionInfo>>()
    val userQuestions: LiveData<List<QuestionInfo>> = _userQuestions
    private val _userAnswers = MutableLiveData<List<AnswerInfo>>()
    val userAnswers: LiveData<List<AnswerInfo>> = _userAnswers
    private val _questionById = MutableLiveData<QuestionInfo>()
    val questionById: LiveData<QuestionInfo> = _questionById

    val user by lazy {
        FirebaseAuth.getInstance().currentUser
    }

    val logout =
        mediforaRepository.logout()

    fun fetchUserById(id: String){
        viewModelScope.launch {
            FirebaseProfileService.getUserById(id).collect{ value ->
                userDetails.value = value
            }
        }
    }

    fun fetchQuestionById(id: String){
        viewModelScope.launch {
            FirebaseProfileService.getQuestionById(id).collect { value ->
                _questionById.value = value
            }
        }
    }

    fun startFetchingAnswersToQuestion(){
        viewModelScope.launch {
            FirebaseProfileService.getAnswersToQuestion(questionId = getQuestionId()).collect { value ->
                _answersToQuiz.value = value
            }
        }
    }

    fun startFetchingHomeQuestions(){
        viewModelScope.launch {
            FirebaseProfileService.getQuestionsSortByDate().collect { value ->
                _questionsByCreationDate.value = value
            }
        }
    }

    fun startFetchingQuestionsWithoutAnswers(){
        viewModelScope.launch {
            FirebaseProfileService.getQuestionsWithZeroAnswers().collect{ value ->
                _questionsWithoutAnswers.value = value
            }
        }
    }

    fun startFetchingUserAnswers(userID: String){
        viewModelScope.launch {
            FirebaseProfileService.getUserAnswers(userID).collect{ value ->
                _userAnswers.value = value
            }
        }
    }

    fun startFetchingUserQuestions(userID: String){
        viewModelScope.launch {
            FirebaseProfileService.getUserQuestions(userID).collect { value ->
                _userQuestions.value = value
            }
        }
    }

    fun updateEmail(email: String, id: String) = mediforaRepository.updateEmail(email, id)

    fun updateName(name: String, id: String) = mediforaRepository.updateName(name, id)

    fun addQuestion(questionId: String, content: String, userID: String, author: String) = mediforaRepository.createQuestion(questionId, content, userID, author)

    fun addAnswer(answer: AnswerInfo) = mediforaRepository.createAnswer(answer, answer.answerId)

    fun deleteQuestion(id: String) = mediforaRepository.deleteQuestion(id)

    fun deleteAnswer(id: String) = mediforaRepository.deleteAnswer(id)

    fun deleteAcount(id: String) = mediforaRepository.deleteAccount(id)

}