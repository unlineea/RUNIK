package com.example.auth.domain

class UserDataValidator(
    private val patternValidator: PatternValidator
) {

    fun isValidEmail(email: String): Boolean {
        return patternValidator.matches(email.trim())
    }

    fun validatePassword(password: String): PasswordValidationState {
        val hasMinLength = password.length >= MIN_PASSWORD_LENGTH
        val hasDigits = password.any { it.isDigit()  }
        val hasLowercaseCharacter = password.any { it.isLowerCase() }
        val hasUppercaseCharacter = password.any { it.isUpperCase() }

        return PasswordValidationState(
            hasNumber = hasDigits,
            hasMinLength = hasMinLength,
            hasUpperCaseCharacter = hasUppercaseCharacter,
            hasLowerCaseCharacter = hasLowercaseCharacter
        )
    }

    companion object {
        const val MIN_PASSWORD_LENGTH = 9
    }
}