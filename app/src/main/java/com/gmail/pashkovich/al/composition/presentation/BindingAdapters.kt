package com.gmail.pashkovich.al.composition.presentation

import android.content.Context
import android.content.res.ColorStateList
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.gmail.pashkovich.al.composition.R
import com.gmail.pashkovich.al.composition.domain.entity.GameResult

interface OnOptionClickListener{
    fun onOptionClick(option: Int)
}

@BindingAdapter("requiredAnswers")
fun bindRequiredAnswers(textView: TextView, count: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.required_score),
        count
    )
}

@BindingAdapter("scoreAnswers")
fun bindScoreAnswers(textView: TextView, score: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.score_answer),
        score
    )
}

@BindingAdapter("requiredPercentage")
fun bindRequiredPercentage(textView: TextView, percentage: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.required_percentage),
        percentage
    )
}

@BindingAdapter("scorePercentage")
fun bindScorePercentage(textView: TextView, gameResult: GameResult) {
    val percentOfRightAnswer = if (gameResult.countOfQuestions == 0) {
        0
    } else {
        ((gameResult.countOfRightAnswers / gameResult.countOfQuestions.toDouble()) * 100).toInt()
    }
    textView.text = String.format(
        textView.context.getString(R.string.score_percentage),
        percentOfRightAnswer
        )
}

@BindingAdapter("emojiResult")
fun bindEmojiResult(imageView: ImageView, isWinner: Boolean) {
    val imageResId = if (isWinner) {
        R.drawable.ic_smile
    } else {
        R.drawable.ic_sad
    }
    imageView.setImageResource(imageResId)
}

@BindingAdapter("percentOfRightQuestion")
fun bindPercentOfRightQuestion(progressBar: ProgressBar, percent: Int) {
    progressBar.setProgress(percent, true)
}

@BindingAdapter("enoughPercentProgressColor")
fun bindEnoughPercentProgressColor(progressBar: ProgressBar, isEnoughPercent: Boolean) {
    val color = getColorByState(progressBar.context, isEnoughPercent)
    progressBar.progressTintList = ColorStateList.valueOf(color)
}
private fun getColorByState(context:Context, goodState: Boolean): Int {
    val colorResId = if (goodState) {
        android.R.color.holo_green_light
    } else {
        android.R.color.holo_red_light
    }
    return ContextCompat.getColor(context, colorResId)
}

@BindingAdapter("minPercentProgress")
fun bindMinPercentProgress(progressBar: ProgressBar, percent: Int) {
    progressBar.secondaryProgress = percent
}

@BindingAdapter("enoughCount")
fun bindEnoughCount(textView: TextView, isEnoughCount: Boolean) {
    textView.setTextColor(getColorByState(textView.context, isEnoughCount))
}

@BindingAdapter("numberAsText")
fun bindNumberAsText(textView: TextView, number: Int) {
    textView.text = number.toString()
}

@BindingAdapter("onOptionClickListener")
fun bindOnOptionClickListener(textView: TextView, clickListener: OnOptionClickListener) {
    textView.setOnClickListener {
        clickListener.onOptionClick(textView.text.toString().toInt())
    }
}