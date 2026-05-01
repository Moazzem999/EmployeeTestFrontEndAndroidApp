package com.example.employeemanager.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.employeemanager.databinding.ItemEmployeeBinding
import com.example.employeemanager.model.Employee

class EmployeeAdapter(
    private val onEditClick: (Employee) -> Unit,
    private val onDeleteClick: (Employee) -> Unit
) : RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder>() {

    private var employeeList = mutableListOf<Employee>()

    fun setEmployees(employees: List<Employee>) {
        employeeList.clear()
        employeeList.addAll(employees)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val binding = ItemEmployeeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EmployeeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        holder.bind(employeeList[position])
    }

    override fun getItemCount(): Int = employeeList.size

    inner class EmployeeViewHolder(private val binding: ItemEmployeeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(employee: Employee) {
            binding.tvName.text = employee.name
            binding.tvEmail.text = employee.email
            binding.tvPhone.text = employee.phone
            binding.tvSalary.text = "$${employee.salary}"

            binding.btnEdit.setOnClickListener { onEditClick(employee) }
            binding.btnDelete.setOnClickListener { onDeleteClick(employee) }
        }
    }
}
