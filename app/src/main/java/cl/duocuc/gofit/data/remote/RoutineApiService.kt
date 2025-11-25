package cl.duocuc.gofit.data.remote


import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient

import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface RoutineApiService {

    
    @GET("routine")
    suspend fun getRoutines(): List<RoutineDto>

    companion object {
        
        private const val BASE_URL = "https://x8ki-letl-twmt.n7.xano.io/api:VqkNg-sc/"

        fun create(): RoutineApiService {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

            val gson = GsonBuilder()
                .setLenient()
                .create()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(RoutineApiService::class.java)
        }
    }
}
