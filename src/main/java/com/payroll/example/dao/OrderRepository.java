package com.payroll.example.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.payroll.example.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
