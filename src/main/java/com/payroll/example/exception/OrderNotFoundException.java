package com.payroll.example.exception;

public class OrderNotFoundException extends RuntimeException {

	public OrderNotFoundException(Long id) {
		super("The order with id "+id+" cannot be found");
	
	}
	

}
