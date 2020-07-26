package com.payroll.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.payroll.example.dao.OrderRepository;
import com.payroll.example.exception.OrderNotFoundException;
import com.payroll.example.model.Order;
import com.payroll.example.model.Status;

@Service
public class OrderService {
	//@Autowired
	private OrderRepository repo;
	
	
	public OrderService(OrderRepository repo) {
		super();
		this.repo = repo;
	}

	public List<Order> getAllOrders() {
		return repo.findAll();
	}
	
	public Order getOrderById(Long id) {
		return repo.findById(id).orElseThrow(()->new OrderNotFoundException(id));
	}
	
	public Order createOrder(Order order) {
		return repo.save(order);
	}
	
	public Order completeOrder(Order order) {
		order.setStatus(Status.COMPLETED);
		return repo.save(order);
	}
	
	public Order cancelOrder(Order order) {
		order.setStatus(Status.CANCELLED);
		return repo.save(order);
	}

}
