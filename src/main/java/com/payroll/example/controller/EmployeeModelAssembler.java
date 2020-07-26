package com.payroll.example.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.payroll.example.model.Employee;

@Component
public class EmployeeModelAssembler implements RepresentationModelAssembler<Employee, EntityModel<Employee>> {

	@Override
	public EntityModel<Employee> toModel(Employee employee) {
		Link link1 =linkTo(methodOn(EmployeeResource.class).getEmployeeById(employee.getId())).withSelfRel();
		Link link2 = linkTo(methodOn(EmployeeResource.class).getAllEmployees()).withRel("Employees");
		return EntityModel.of(employee, link1, link2);
	}

}
