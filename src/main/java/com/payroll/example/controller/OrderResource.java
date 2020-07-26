package com.payroll.example.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.payroll.example.model.Order;
import com.payroll.example.model.Status;
import com.payroll.example.service.OrderService;

@RestController
public class OrderResource {
		@Autowired
		private OrderService service ;
		
		@Autowired
		private OrderModelAssembler assembler;

		@CrossOrigin
		@GetMapping("/orders")
		public CollectionModel<EntityModel<Order>> getAllOrder(){
			List<Order> orders = service.getAllOrders();
			List<EntityModel<Order>> entities = orders.stream().map(assembler::toModel).collect(Collectors.toList());
			return CollectionModel.of(entities, 
					linkTo(methodOn(this.getClass()).getAllOrder()).withSelfRel()
					);
			
		}
		
		@GetMapping("/orders/{id}")
		public EntityModel<Order> getOrderById(@PathVariable("id")Long id){
			return assembler.toModel(service.getOrderById(id));
		}
		
		@CrossOrigin
		@PostMapping("/orders")
		public ResponseEntity<?> createOrder(@RequestBody Order order){
			order.setStatus(Status.IN_PROGRESS);
			EntityModel<Order> entity = assembler.toModel(service.createOrder(order));
			return ResponseEntity.created(entity.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entity);
		}
		
		@PutMapping("/orders/{id}/complete")
		public ResponseEntity<?> completeOrder(@PathVariable("id") Long id){
			Order order = service.getOrderById(id);
			if(order.getStatus()==Status.IN_PROGRESS) {
				return ResponseEntity.ok(assembler.toModel(service.completeOrder(order)));
			}
			return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
					.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PROBLEM_JSON_VALUE)
					.body(Problem.create().withTitle("Method Not Allowed")
							.withDetail("You cannot complete an order which is in "+order.getStatus()+" state"));
					
		}
		
		@PutMapping("/orders/{id}/cancel")
		public ResponseEntity<?> cancelOrder(@PathVariable("id") Long id){
			Order order = service.getOrderById(id);
			if(order.getStatus() == Status.IN_PROGRESS) {
				return ResponseEntity.ok(assembler.toModel(service.cancelOrder(order)));
			}
			return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
					.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PROBLEM_JSON_VALUE)
					.body(Problem.create().withTitle("Method Not Allowed")
							.withDetail("You cannot complete an order which is in "+order.getStatus()+" state"));
		}
}
