package com.payroll.example.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.payroll.example.model.Order;
import com.payroll.example.model.Status;

@Component
public class OrderModelAssembler implements RepresentationModelAssembler<Order, EntityModel<Order>> {

	@Override
	public EntityModel<Order> toModel(Order order) {
		EntityModel<Order> result = EntityModel.of(order,
				linkTo(methodOn(OrderResource.class).getOrderById(order.getId())).withSelfRel(),
				linkTo(methodOn(OrderResource.class).getAllOrder()).withRel("Orders"));
		
		if(order.getStatus() == Status.IN_PROGRESS) {
			result.add(
					linkTo(methodOn(OrderResource.class).completeOrder(order.getId())).withRel("Complete"),
					linkTo(methodOn(OrderResource.class).cancelOrder(order.getId())).withRel("Cancel")				
					);
		}
		return result;
		
	}

}
