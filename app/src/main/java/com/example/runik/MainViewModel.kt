package com.example.runik

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.SessionStorage
import kotlinx.coroutines.launch

class MainViewModel(
    private val sessionStorage: SessionStorage
): ViewModel() {

    var state by mutableStateOf(MainState())
        private set

    init {

        viewModelScope.launch {
            state.copy(isCheckingAuth = true)
            state.copy(
                isLoggedIn = sessionStorage.get() != null
            )
            state.copy(isCheckingAuth = false)
        }

    }
}