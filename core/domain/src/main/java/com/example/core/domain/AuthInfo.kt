package com.example.core.domain

data class AuthInfo(
    val accessToken: String,
    val refreshToken: String,
    val userId: String
) {
    override fun toString(): String {
        return this.userId +
                "\n" + this.refreshToken +
                "\n" + this.userId
    }
}
