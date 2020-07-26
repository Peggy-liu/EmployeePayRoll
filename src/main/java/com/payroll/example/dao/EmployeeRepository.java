package com.payroll.example.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.payroll.example.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
