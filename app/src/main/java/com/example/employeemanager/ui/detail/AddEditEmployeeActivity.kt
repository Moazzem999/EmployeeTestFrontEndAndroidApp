package com.example.employeemanager.ui.detail

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.employeemanager.data.api.ApiClient
import com.example.employeemanager.data.repository.EmployeeRepository
import com.example.employeemanager.databinding.ActivityAddEditEmployeeBinding
import com.example.employeemanager.model.CreateEmployeeDto
import com.example.employeemanager.model.UpdateEmployeeDto
import com.example.employeemanager.ui.viewmodel.EmployeeViewModel
import com.example.employeemanager.ui.viewmodel.ViewModelFactory

class AddEditEmployeeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditEmployeeBinding
    private lateinit var viewModel: EmployeeViewModel
    private var employeeId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditEmployeeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        checkIntent()

        binding.btnSave.setOnClickListener { saveEmployee() }
    }

    private fun setupViewModel() {
        val repository = EmployeeRepository(ApiClient.getApiService(this))
        val factory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(EmployeeViewModel::class.java)

        viewModel.operationSuccess.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "Employee saved successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun checkIntent() {
        employeeId = intent.getIntExtra("EMP_ID", -1)
        if (employeeId != -1) {
            supportActionBar?.title = "Edit Employee"
            binding.etName.setText(intent.getStringExtra("EMP_NAME"))
            binding.etEmail.setText(intent.getStringExtra("EMP_EMAIL"))
            binding.etPhone.setText(intent.getStringExtra("EMP_PHONE"))
            binding.etSalary.setText(intent.getDoubleExtra("EMP_SALARY", 0.0).toString())
        } else {
            supportActionBar?.title = "Add Employee"
        }
    }

    private fun saveEmployee() {
        val name = binding.etName.text.toString()
        val email = binding.etEmail.text.toString()
        val phone = binding.etPhone.text.toString()
        val salary = binding.etSalary.text.toString().toDoubleOrNull() ?: 0.0

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (employeeId == -1) {
            viewModel.createEmployee(CreateEmployeeDto(name, email, phone, salary))
        } else {
            viewModel.updateEmployee(employeeId, UpdateEmployeeDto(name, email, phone, salary))
        }
    }
}
