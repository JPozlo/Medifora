package com.misolova.medifora.ui.home.viewmodel

import android.content.SharedPreferences
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.misolova.medifora.data.repo.MediforaRepository
import com.misolova.medifora.data.source.local.entities.AnswerEntity
import com.misolova.medifora.data.source.local.entities.QuestionEntity
import com.misolova.medifora.data.source.remote.FirebaseProfileService
import com.misolova.medifora.domain.model.AnswerInfo
import com.misolova.medifora.domain.model.QuestionInfo
import com.misolova.medifora.domain.model.User
import com.misolova.medifora.domain.model.UserInfo
import com.misolova.medifora.util.Constants.KEY_USER_ID
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import timber.log.Timber
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

    val questionID = MutableLiveData<String>()

    fun setQuestionId(questionId: String) {
        questionID.setValue(questionId)
    }

    private fun getQuestionId() = questionID.value!!


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
    private val _userDetails = MutableLiveData<UserInfo>()
    val userDetails: LiveData<UserInfo> = _userDetails

    val user by lazy {
        FirebaseAuth.getInstance().currentUser
    }

    val logout =
        mediforaRepository.logout()

    init {
        viewModelScope.launch {
            FirebaseProfileService.getQuestions().collect { value ->
                _homeFeedQuestions.value = value
            }
            FirebaseProfileService.getUserQuestions(getUserId()).collect { value ->
                _allQuestions.value = value
            }
            FirebaseProfileService.getUserAnswers(getUserId()).collect { value ->
                _userAnswers.value = value
            }
            FirebaseProfileService.getUserDetails(id = getUserId()).collect { value ->
                _userDetails.value = value
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

    fun getUserId() =
        sharedPreferences.getString(KEY_USER_ID, "")!!

    fun getProfileData(id: String) = FirebaseProfileService.getProfileData(id)

    fun addQuestion(questionId: String, content: String, userID: String, author: String) = viewModelScope.launch {
        FirebaseProfileService.createQuestion(
            questionId = questionId,
            content = content,
            userId = userID,
            author = author
        )
            .onCompletion { cause ->
                Timber.d("$TAG: Cause of completing question -> $cause")
            }
            .catch { cause ->
                Timber.e("$TAG: Caught the exception -> $cause")
            }
            .collect { value ->
                Timber.d("$TAG: The collected value is -> ${value.get().result}")
            }
    }

    fun addAnswer(answer: AnswerInfo) = viewModelScope.launch {
        FirebaseProfileService.createAnswer(answer = answer, answerId = answer.answerId)
            .onCompletion {cause ->
                Timber.d("$TAG: Cause of completing question -> $cause")
            }
            .catch { cause ->
                Timber.e("$TAG: Caught the exception -> $cause")
            }
            .collect { value ->
                Timber.d("$TAG: Results of adding answer -> ${value.get().result}")
            }
    }

    fun deleteQuestion(questionEntity: QuestionEntity) = viewModelScope.launch {
        mediforaRepository.deleteQuestion(questionEntity)
    }

    fun deleteAnswer(answerEntity: AnswerEntity) = viewModelScope.launch {
        mediforaRepository.deleteAnswer(answerEntity)
    }

}