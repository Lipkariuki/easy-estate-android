package com.easyestate.android.data

import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

// --- Data Transfer Objects (DTOs) ---

data class LoginRequest(
    val email: String,
    val password: String
)

data class SignupRequest(
    val email: String,
    val password: String,
    val role: String
)

data class TokenResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("token_type") val tokenType: String,
    val user: UserInfo
)

data class UserInfo(
    val id: Int,
    val email: String,
    val role: String,
    val active: Boolean
)

data class SignupResponse(
    @SerializedName("user_id") val userId: Int,
    val email: String,
    val role: String,
    val active: Boolean,
    @SerializedName("verification_sent") val verificationSent: Boolean,
    @SerializedName("verification_expires_at") val verificationExpiresAt: String,
    @SerializedName("debug_token") val debugToken: String?
)

data class TenantCreateRequest(
    @SerializedName("full_name") val fullName: String,
    val email: String?,
    val phone: String?,
    @SerializedName("id_number") val idNumber: String?,
    @SerializedName("date_of_birth") val dateOfBirth: String?,
    val gender: String?,
    val occupation: String?,
    @SerializedName("emergency_contact_name") val emergencyContactName: String?,
    @SerializedName("emergency_contact_phone") val emergencyContactPhone: String?,
    val notes: String?
)

data class TenantResponse(
    val id: Int,
    @SerializedName("full_name") val fullName: String,
    val email: String?,
    val phone: String?,
    @SerializedName("id_number") val idNumber: String?,
    @SerializedName("date_of_birth") val dateOfBirth: String?,
    val gender: String?,
    val occupation: String?,
    @SerializedName("kyc_status") val kycStatus: String,
    @SerializedName("kyc_score") val kycScore: Int,
    @SerializedName("kyc_override") val kycOverride: Boolean,
    @SerializedName("pending_documents") val pendingDocuments: Int,
    @SerializedName("created_at") val createdAt: String
)

// --- Retrofit API Service ---

interface EasyEstateApiService {
    @POST("auth/login")
    suspend fun signIn(@Body request: LoginRequest): Response<TokenResponse>

    @POST("auth/signup")
    suspend fun register(@Body request: SignupRequest): Response<SignupResponse>

    @POST("tenants/")
    suspend fun createTenant(@Body request: TenantCreateRequest): Response<TenantResponse>
}

// --- Retrofit Client ---

object ApiClient {
    // Use 10.0.2.2 to connect to localhost from the Android emulator
    private const val BASE_URL = "http://10.0.2.2:8000/"

    @Volatile
    private var authToken: String? = null

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val builder = chain.request().newBuilder()
            val token = authToken
            if (!token.isNullOrBlank()) {
                builder.addHeader("Authorization", "Bearer $token")
            }
            chain.proceed(builder.build())
        }
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

    fun updateAuthToken(token: String?) {
        authToken = token
    }
}
