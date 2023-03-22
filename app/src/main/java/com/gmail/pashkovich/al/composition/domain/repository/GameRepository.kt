package com.gmail.pashkovich.al.composition.domain.repository

import com.gmail.pashkovich.al.composition.domain.entity.GameSettings
import com.gmail.pashkovich.al.composition.domain.entity.Level
import com.gmail.pashkovich.al.composition.domain.entity.Question

interface GameRepository {

    fun generateQuestion(
        maxSumValue: Int,
        countOfOptions: Int
    ): Question

    fun getGameSettings(level: Level): GameSettings
}