package com.example.employeemanager.data.api

import com.example.employeemanager.model.*
import retrofit2.Response
import retrofit2.http.*

interface EmployeeApiService {

    @POST("api/Auth/login")
    suspend fun login(@Body loginDto: LoginDto): Response<ApiResponse<AuthResponse>>

    @GET("api/Employee")
    suspend fun getAllEmployees(@Query("search") search: String? = null): Response<ApiResponse<List<Employee>>>

    @GET("api/Employee/{id}")
    suspend fun getEmployeeById(@Path("id") id: Int): Response<ApiResponse<Employee>>

    @POST("api/Employee")
    suspend fun createEmployee(@Body employee: CreateEmployeeDto): Response<ApiResponse<Employee>>

    @PUT("api/Employee/{id}")
    suspend fun updateEmployee(@Path("id") id: Int, @Body employee: UpdateEmployeeDto): Response<ApiResponse<Boolean>>

    @DELETE("api/Employee/{id}")
    suspend fun deleteEmployee(@Path("id") id: Int): Response<ApiResponse<Boolean>>

    @GET("api/Employee/deleted")
    suspend fun getDeletedEmployees(): Response<ApiResponse<List<Employee>>>
}
