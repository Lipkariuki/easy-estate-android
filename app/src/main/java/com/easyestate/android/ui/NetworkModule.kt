package com.easyestate.android.data

import com.easyestate.android.BuildConfig
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

data class PropertyCreateRequest(
    val name: String,
    @SerializedName("property_type") val propertyType: String,
    @SerializedName("address_line_1") val addressLine1: String?,
    val city: String?,
    val location: String?,
    @SerializedName("image_url") val imageUrl: String?,
    val notes: String?
)

data class PropertyResponse(
    val id: Int,
    val name: String,
    val code: String?,
    @SerializedName("property_type") val propertyType: String?,
    val city: String?,
    val location: String?,
    @SerializedName("image_url") val imageUrl: String?,
    @SerializedName("occupancy_rate") val occupancyRate: Double,
    @SerializedName("units_total") val unitsTotal: Int,
    @SerializedName("units_vacant") val unitsVacant: Int,
    @SerializedName("pending_kyc") val pendingKyc: Int,
    @SerializedName("monthly_revenue") val monthlyRevenue: Double,
    @SerializedName("address_line_1") val addressLine1: String?,
    val notes: String?,
    @SerializedName("owner_id") val ownerId: Int?,
    @SerializedName("manager_id") val managerId: Int?,
    @SerializedName("created_at") val createdAt: String?
)

// --- Retrofit API Service ---

interface EasyEstateApiService {
    @POST("auth/login")
    suspend fun signIn(@Body request: LoginRequest): Response<TokenResponse>

    @POST("auth/signup")
    suspend fun register(@Body request: SignupRequest): Response<SignupResponse>

    @POST("tenants/")
    suspend fun createTenant(@Body request: TenantCreateRequest): Response<TenantResponse>

    @POST("properties/")
    suspend fun createProperty(@Body request: PropertyCreateRequest): Response<PropertyResponse>
}

// --- Retrofit Client ---

object ApiClient {
    private const val DEFAULT_BASE_URL = "http://10.0.2.2:8000/"

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

    private fun resolveBaseUrl(): String {
        val configured = BuildConfig.API_BASE_URL.trim()
        if (configured.isEmpty()) {
            return DEFAULT_BASE_URL
        }
        return if (configured.endsWith("/")) configured else "$configured/"
    }

    private fun createRetrofit(baseUrl: String): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val instance: EasyEstateApiService by lazy {
        createRetrofit(resolveBaseUrl()).create(EasyEstateApiService::class.java)
    }

    fun updateAuthToken(token: String?) {
        authToken = token
    }
}
