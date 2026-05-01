package com.example.employeemanager.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.employeemanager.data.repository.EmployeeRepository
import com.example.employeemanager.model.*
import kotlinx.coroutines.launch

class EmployeeViewModel(private val repository: EmployeeRepository) : ViewModel() {

    private val _employees = MutableLiveData<List<Employee>>()
    val employees: LiveData<List<Employee>> = _employees

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _operationSuccess = MutableLiveData<Boolean>()
    val operationSuccess: LiveData<Boolean> = _operationSuccess

    fun fetchEmployees(search: String? = null) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.getEmployees(search)
                if (response.isSuccessful && response.body()?.success == true) {
                    _employees.postValue(response.body()?.data ?: emptyList())
                    _error.postValue(null)
                } else {
                    _error.postValue(response.body()?.message ?: "Failed to fetch employees")
                }
            } catch (e: Exception) {
                _error.postValue(e.message)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun createEmployee(employee: CreateEmployeeDto) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.createEmployee(employee)
                _operationSuccess.postValue(response.isSuccessful && response.body()?.success == true)
            } catch (e: Exception) {
                _error.postValue(e.message)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun updateEmployee(id: Int, employee: UpdateEmployeeDto) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.updateEmployee(id, employee)
                _operationSuccess.postValue(response.isSuccessful && response.body()?.success == true)
            } catch (e: Exception) {
                _error.postValue(e.message)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun deleteEmployee(id: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.deleteEmployee(id)
                if (response.isSuccessful && response.body()?.success == true) {
                    fetchEmployees() // Refresh list
                } else {
                    _error.postValue(response.body()?.message ?: "Failed to delete employee")
                }
            } catch (e: Exception) {
                _error.postValue(e.message)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}
