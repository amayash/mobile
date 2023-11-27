package com.example.myapplication.api

import com.example.myapplication.api.cinema.CinemaRemote
import com.example.myapplication.api.session.SessionRemote
import com.example.myapplication.api.order.OrderRemote
import com.example.myapplication.api.session.SessionFromCinemaRemote
import com.example.myapplication.api.user.UserRemote
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface MyServerService {
    @GET("cinemas")
    suspend fun getCinemas(): List<CinemaRemote>

    @GET("sessions")
    suspend fun getSessions(): List<SessionRemote>

    @GET("orders")
    fun getOrders(): List<OrderRemote>

    @GET("users")
    fun getUsers(): List<UserRemote>

    @GET("cinemas")
    suspend fun getCinemas(
        @Query("_page") page: Int,
        @Query("_limit") limit: Int,
    ): List<CinemaRemote>

    @GET("cinemas/{id}")
    suspend fun getCinema(
        @Path("id") id: Int,
    ): CinemaRemote

    @POST("cinemas")
    suspend fun createCinema(
        @Body cinema: CinemaRemote,
    ): CinemaRemote

    @PUT("cinemas/{id}")
    suspend fun updateCinema(
        @Path("id") id: Int,
        @Body cinema: CinemaRemote,
    ): CinemaRemote

    @DELETE("cinemas/{id}")
    suspend fun deleteCinema(
        @Path("id") id: Int,
    ): CinemaRemote

    @GET("cinemas/{cinemaId}/sessions")
    suspend fun getSessionsForCinema(
        @Path("cinemaId") cinemaId: Int
    ): List<SessionFromCinemaRemote>

    @GET("sessions/{id}")
    suspend fun getSession(
        @Path("id") id: Int,
    ): SessionRemote

    @POST("sessions")
    suspend fun createSession(
        @Body session: SessionRemote,
    ): SessionRemote

    @PUT("sessions/{id}")
    suspend fun updateSession(
        @Path("id") id: Int,
        @Body session: SessionRemote,
    ): SessionRemote

    @DELETE("sessions/{id}")
    suspend fun deleteSession(
        @Path("id") id: Int,
    ): SessionFromCinemaRemote

    @GET("users/{id}")
    suspend fun getUserCart(
        @Path("id") id: Int,
    ): UserRemote

    @PUT("users/{id}")
    suspend fun updateUserCart(
        @Path("id") id: Int,
        @Body userRemote: UserRemote,
    ): UserRemote

    @GET("orders")
    suspend fun getOrders(
        @Query("_page") page: Int,
        @Query("_limit") limit: Int,
    ): List<OrderRemote>

    @GET("orders/{id}")
    suspend fun getOrder(
        @Path("id") id: Int,
    ): OrderRemote

    @POST("orders")
    suspend fun createOrder(
        @Body cinema: OrderRemote,
    ): OrderRemote

    companion object {
        private const val BASE_URL = "http://192.168.0.101:8079/"

        @Volatile
        private var INSTANCE: MyServerService? = null

        fun getInstance(): MyServerService {
            return INSTANCE ?: synchronized(this) {
                val logger = HttpLoggingInterceptor()
                logger.level = HttpLoggingInterceptor.Level.BASIC
                val client = OkHttpClient.Builder()
                    .addInterceptor(logger)
                    .build()
                val json = Json {
                    ignoreUnknownKeys = true
                    serializersModule = SerializersModule {
                        contextual(LocalDateTimeSerializer)
                    }
                } // Создаем экземпляр Json с ignoreUnknownKeys = true
                return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(json.asConverterFactory("application/json".toMediaType())) // Применяем конфигурацию Json
                    .build()
                    .create(MyServerService::class.java)
                    .also { INSTANCE = it }
            }
        }
    }
}