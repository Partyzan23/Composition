package com.gmail.pashkovich.al.composition.domain.usecases

import com.gmail.pashkovich.al.composition.domain.entity.GameSettings
import com.gmail.pashkovich.al.composition.domain.entity.Level
import com.gmail.pashkovich.al.composition.domain.repository.GameRepository

class GetGameSettingsUseCase(
    private val repository: GameRepository
) {
    operator fun invoke(level: Level): GameSettings {
        return repository.getGameSettings(level)
    }
}