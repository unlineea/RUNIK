package com.example.auth.data

import android.util.Log
import com.example.auth.domain.AuthRepository
import com.example.core.data.networking.post
import com.example.core.domain.AuthInfo
import com.example.core.domain.SessionStorage
import com.example.core.domain.util.DataError
import com.example.core.domain.util.EmptyResult
import com.example.core.domain.util.Result
import com.example.core.domain.util.asEmptyDataResult
import io.ktor.client.HttpClient

class AuthRepositoryImpl(
    private val httpClient: HttpClient,
    val sessionStorage: SessionStorage
): AuthRepository {
    override suspend fun login(email: String, password: String): EmptyResult<DataError.Network> {
        val result = httpClient.post<LoginRequest, LoginResponse>(
            route = "/login",
            body = LoginRequest(
                email = email,
                password = password
            )
        )
        if (result is Result.Success) {
            Log.d("authRepoImpl", "setting auth info: \n" +
                    "accessToken = ${result.data.accessToken},\n" +
                    "refreshToken = ${result.data.refreshToken},\n" +
                    "userId = ${result.data.userId}")
            sessionStorage.set(
                info = AuthInfo(
                    accessToken = result.data.accessToken,
                    refreshToken = result.data.refreshToken,
                    userId = result.data.userId
                )
            )
        }
        return result.asEmptyDataResult()
    }

    override suspend fun register(email: String, password: String): EmptyResult<DataError.Network> {
        return httpClient.post<RegisterRequest, Unit>(
            route = "/register",
            body = RegisterRequest(
                email = email,
                password = password
            )
        )
    }
}