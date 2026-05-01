package com.example.employeemanager.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.employeemanager.data.repository.EmployeeRepository
import com.example.employeemanager.model.ApiResponse
import com.example.employeemanager.model.AuthResponse
import com.example.employeemanager.model.LoginDto
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: EmployeeRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<Result<ApiResponse<AuthResponse>>>()
    val loginResult: LiveData<Result<ApiResponse<AuthResponse>>> = _loginResult

    fun login(username: String, password: String) {
        viewModelScope.launch {
            try {
                val response = repository.login(LoginDto(username, password))
                if (response.isSuccessful && response.body() != null) {
                    _loginResult.postValue(Result.success(response.body()!!))
                } else {
                    _loginResult.postValue(Result.failure(Exception(response.message())))
                }
            } catch (e: Exception) {
                _loginResult.postValue(Result.failure(e))
            }
        }
    }
}
