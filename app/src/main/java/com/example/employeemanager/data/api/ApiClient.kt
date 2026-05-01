package com.example.employeemanager.data.api

import android.content.Context
import com.example.employeemanager.utils.Constants
import com.example.employeemanager.utils.TokenManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private var apiService: EmployeeApiService? = null

    fun getApiService(context: Context): EmployeeApiService {
        if (apiService == null) {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val authInterceptor = AuthInterceptor(TokenManager(context))

            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(authInterceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            apiService = retrofit.create(EmployeeApiService::class.java)
        }
        return apiService!!
    }
}
