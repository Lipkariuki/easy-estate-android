package com.easyestate.android.data

import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

// --- Data Transfer Objects (DTOs) ---

data class AuthRequest(
    val email: String,
    val password: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("phone_number") val phoneNumber: String,
    @SerializedName("user_role") val userRole: String
)

data class Token(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("token_type") val tokenType: String
)

data class User(
    val id: Int,
    val email: String,
    @SerializedName("is_active") val isActive: Boolean,
    @SerializedName("first_name") val firstName: String?,
    @SerializedName("last_name") val lastName: String?
)

// --- Retrofit API Service ---

interface EasyEstateApiService {
    @FormUrlEncoded
    @POST("login")
    suspend fun signIn(
        @Field("username") email: String,
        @Field("password") password: String
    ): Response<Token>

    @POST("users/")
    suspend fun register(@Body request: AuthRequest): Response<User>

    @GET("users/me")
    suspend fun getCurrentUser(@Header("Authorization") token: String): User
}

// --- Retrofit Client ---

object ApiClient {
    // Use 10.0.2.2 to connect to localhost from the Android emulator
    private const val BASE_URL = "http://10.0.2.2:8000/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val instance: EasyEstateApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EasyEstateApiService::class.java)
    }
}