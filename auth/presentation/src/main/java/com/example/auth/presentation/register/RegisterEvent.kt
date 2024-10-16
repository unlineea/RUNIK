package com.example.auth.presentation.register

import com.example.core.presentation.ui.UiText

sealed interface RegisterEvent {
    data object RegistrationSuccess: RegisterEvent
    data class RegisterError(val error: UiText): RegisterEvent
}