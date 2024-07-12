package com.capstone.bloomy.repository

import com.capstone.bloomy.data.remote.authentication.AuthenticationService

class AuthenticationRepository private constructor(private val authenticationService: AuthenticationService) {

    suspend fun signUp(email: String, username: String, password:String, confirmPassword: String) = authenticationService.signUp(email, username, password, confirmPassword)

    suspend fun signIn(email: String, password: String) = authenticationService.signIn(email, password)

    suspend fun sendVerificationLink(email: String) = authenticationService.sendVerificationLink(email)

    suspend fun forgotPassword(email: String) = authenticationService.forgotPassword(email)

    companion object {
        @Volatile
        private var INSTANCE: AuthenticationRepository? = null

        fun getInstance(
            authenticationService: AuthenticationService,
        ): AuthenticationRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: AuthenticationRepository(authenticationService)
            }.also { INSTANCE = it }
    }
}