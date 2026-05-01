package com.example.employeemanager.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.employeemanager.data.api.ApiClient
import com.example.employeemanager.data.repository.EmployeeRepository
import com.example.employeemanager.databinding.ActivityLoginBinding
import com.example.employeemanager.ui.list.EmployeeListActivity
import com.example.employeemanager.ui.viewmodel.LoginViewModel
import com.example.employeemanager.ui.viewmodel.ViewModelFactory
import com.example.employeemanager.utils.TokenManager

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = EmployeeRepository(ApiClient.getApiService(this))
        val factory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(LoginViewModel::class.java)

        val tokenManager = TokenManager(this)
        if (tokenManager.getToken() != null) {
            navigateToMain()
        }

        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                binding.progressBar.visibility = View.VISIBLE
                viewModel.login(username, password)
            } else {
                Toast.makeText(this, "Please enter credentials", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.loginResult.observe(this) { result ->
            binding.progressBar.visibility = View.GONE
            result.onSuccess {
                it.data?.let { auth ->
                    tokenManager.saveToken(auth.token)
                    navigateToMain()
                }
            }.onFailure {
                Toast.makeText(this, "Login Failed: ${it.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun navigateToMain() {
        startActivity(Intent(this, EmployeeListActivity::class.java))
        finish()
    }
}
