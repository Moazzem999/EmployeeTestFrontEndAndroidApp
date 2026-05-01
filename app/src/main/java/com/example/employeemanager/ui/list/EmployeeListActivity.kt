package com.example.employeemanager.ui.list

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.employeemanager.data.api.ApiClient
import com.example.employeemanager.data.repository.EmployeeRepository
import com.example.employeemanager.databinding.ActivityEmployeeListBinding
import com.example.employeemanager.model.Employee
import com.example.employeemanager.ui.detail.AddEditEmployeeActivity
import com.example.employeemanager.ui.viewmodel.EmployeeViewModel
import com.example.employeemanager.ui.viewmodel.ViewModelFactory
import com.example.employeemanager.utils.TokenManager

class EmployeeListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmployeeListBinding
    private lateinit var viewModel: EmployeeViewModel
    private lateinit var adapter: EmployeeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmployeeListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        setupViewModel()
        observeData()

        viewModel.fetchEmployees()
    }

    private fun setupUI() {
        adapter = EmployeeAdapter(
            onEditClick = { employee -> navigateToAddEdit(employee) },
            onDeleteClick = { employee -> showDeleteDialog(employee) }
        )
        binding.rvEmployees.layoutManager = LinearLayoutManager(this)
        binding.rvEmployees.adapter = adapter

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.fetchEmployees(binding.searchView.query.toString())
        }

        binding.fabAdd.setOnClickListener { navigateToAddEdit(null) }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.fetchEmployees(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    viewModel.fetchEmployees()
                }
                return true
            }
        })
    }

    private fun setupViewModel() {
        val repository = EmployeeRepository(ApiClient.getApiService(this))
        val factory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(EmployeeViewModel::class.java)
    }

    private fun observeData() {
        viewModel.employees.observe(this) { employees ->
            adapter.setEmployees(employees)
            binding.swipeRefresh.isRefreshing = false
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.swipeRefresh.isRefreshing = isLoading
        }

        viewModel.error.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun navigateToAddEdit(employee: Employee?) {
        val intent = Intent(this, AddEditEmployeeActivity::class.java)
        employee?.let {
            intent.putExtra("EMP_ID", it.id)
            intent.putExtra("EMP_NAME", it.name)
            intent.putExtra("EMP_EMAIL", it.email)
            intent.putExtra("EMP_PHONE", it.phone)
            intent.putExtra("EMP_SALARY", it.salary)
        }
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchEmployees(binding.searchView.query.toString())
    }

    private fun showDeleteDialog(employee: Employee) {
        AlertDialog.Builder(this)
            .setTitle("Delete Employee")
            .setMessage("Are you sure you want to delete ${employee.name}?")
            .setPositiveButton("Yes") { _, _ -> viewModel.deleteEmployee(employee.id) }
            .setNegativeButton("No", null)
            .show()
    }
}
