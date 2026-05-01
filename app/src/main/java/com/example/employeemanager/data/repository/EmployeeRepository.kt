package com.example.employeemanager.data.repository

import com.example.employeemanager.data.api.EmployeeApiService
import com.example.employeemanager.model.*
import retrofit2.Response

class EmployeeRepository(private val apiService: EmployeeApiService) {

    suspend fun login(loginDto: LoginDto) = apiService.login(loginDto)

    suspend fun getEmployees(search: String? = null) = apiService.getAllEmployees(search)

    suspend fun getEmployeeById(id: Int) = apiService.getEmployeeById(id)

    suspend fun createEmployee(employee: CreateEmployeeDto) = apiService.createEmployee(employee)

    suspend fun updateEmployee(id: Int, employee: UpdateEmployeeDto) = apiService.updateEmployee(id, employee)

    suspend fun deleteEmployee(id: Int) = apiService.deleteEmployee(id)
}
