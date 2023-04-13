package com.gmail.pashkovich.al.composition.presentation

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gmail.pashkovich.al.composition.R
import com.gmail.pashkovich.al.composition.data.GameRepositoryImpl
import com.gmail.pashkovich.al.composition.domain.entity.GameResult
import com.gmail.pashkovich.al.composition.domain.entity.GameSettings
import com.gmail.pashkovich.al.composition.domain.entity.Level
import com.gmail.pashkovich.al.composition.domain.entity.Question
import com.gmail.pashkovich.al.composition.domain.usecases.GenerateQuestionUseCase
import com.gmail.pashkovich.al.composition.domain.usecases.GetGameSettingsUseCase

class GameViewModel(
    private val application: Application,
    private val level: Level
    ) : ViewModel() {

    private lateinit var gameSettings: GameSettings

    private val repository = GameRepositoryImpl

    private val generateQuestionUseCase = GenerateQuestionUseCase(repository)
    private val getGameSettingsUseCase = GetGameSettingsUseCase(repository)

    private val _formattedTime = MutableLiveData<String>()
    private val _question = MutableLiveData<Question>()
    private val _percentOfRightAnswers = MutableLiveData<Int>()
    private val _progressAnswers = MutableLiveData<String>()
    private val _enoughCount = MutableLiveData<Boolean>()
    private val _enoughPercent = MutableLiveData<Boolean>()
    private val _minPercent = MutableLiveData<Int>()
    private val _gameResult = MutableLiveData<GameResult>()

    val formattedTime: LiveData<String> get() = _formattedTime
    val question: LiveData<Question> get() = _question
    val percentOfRightQuestion: LiveData<Int> get() = _percentOfRightAnswers
    val progressAnswers: LiveData<String> get() = _progressAnswers
    val enoughCount: LiveData<Boolean> get() = _enoughCount
    val enoughPercent: LiveData<Boolean> get() = _enoughPercent
    val minPercent: LiveData<Int> get() = _minPercent
    val gameResult:LiveData<GameResult> get() = _gameResult

    private var timer: CountDownTimer? = null

    private var countOfRightAnswers = 0
    private var countOfQuestions = 0

    init {
        startGame()
    }

    private fun startGame() {
        getGameSettings()
        startTimer()
        generateQuestion()
        updateProgress()
    }

    fun choseAnswer(number: Int) {
        checkAnswer(number)
        updateProgress()
        generateQuestion()
    }

    private fun updateProgress() {
        val percent = calculatePercentOfRightAnswers()
        _percentOfRightAnswers.value = percent
        _progressAnswers.value = String.format(
            application.resources.getString(R.string.progress_answer),
            countOfRightAnswers,
            gameSettings.minCountOfRightAnswers
        )
        _enoughCount.value = countOfRightAnswers >= gameSettings.minCountOfRightAnswers
        _enoughPercent.value = percent >= gameSettings.minPercentOfRightAnswers
    }

    private fun calculatePercentOfRightAnswers(): Int {
        if (countOfQuestions == 0){
            return 0
        }
        return ((countOfRightAnswers / countOfQuestions.toDouble()) * 100).toInt()
    }

    private fun checkAnswer(number: Int) {
        val rightAnswer = question.value?.rightAnswer
        if (number == rightAnswer) {
            countOfRightAnswers++
        }
        countOfQuestions++
    }

    private fun generateQuestion() {
        _question.value = generateQuestionUseCase(gameSettings.maxSumValue)
    }

    private fun getGameSettings() {
        this.gameSettings = getGameSettingsUseCase(level)
        _minPercent.value = gameSettings.minPercentOfRightAnswers
    }


    private fun startTimer() {
        timer = object : CountDownTimer(
            gameSettings.gameTimeInSeconds * MILLIS_IN_SECONDS,
            MILLIS_IN_SECONDS
        ) {
            override fun onTick(millisUntilFinished: Long) {
                _formattedTime.value = formatTime(millisUntilFinished)
            }

            override fun onFinish() {
                finishGame()
            }

        }
        timer?.start()
    }

    private fun formatTime(millisUntilFinished: Long): String {
        val seconds = millisUntilFinished / MILLIS_IN_SECONDS
        val minutes = seconds / SECONDS_IN_MINUTES
        val leftSeconds = seconds - (minutes * SECONDS_IN_MINUTES)
        return String.format("%02d:%02d", minutes, leftSeconds)
    }

    private fun finishGame() {
        _gameResult.value = GameResult(
            enoughCount.value == true && enoughPercent.value == true,
            countOfRightAnswers,
            countOfQuestions,
            gameSettings
        )
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }

    companion object {
        private const val MILLIS_IN_SECONDS = 1000L
        private const val SECONDS_IN_MINUTES = 60
    }
}