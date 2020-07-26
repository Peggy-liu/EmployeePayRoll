package com.payroll.example.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.payroll.example.dao.EmployeeRepository;
import com.payroll.example.exception.EmployeeNotFoundException;
import com.payroll.example.model.Employee;

@RestController
public class EmployeeResource {
	
	@Autowired
	private EmployeeRepository repo;
	
	@Autowired
	private EmployeeModelAssembler assembler;

	//Collection URL
	@GetMapping("/employees")
	public CollectionModel<EntityModel<Employee>> getAllEmployees(){
		List<EntityModel<Employee>> list = repo.findAll().stream()
				.map(assembler::toModel)
				.collect(Collectors.toList());
		return CollectionModel.of(list, linkTo(methodOn(EmployeeResource.class).getAllEmployees()).withSelfRel());
	}
	
	@PostMapping("/employees")
	public ResponseEntity<?> createEmployee(@RequestBody Employee newEmployee) {
		EntityModel<Employee> entityModel = assembler.toModel(repo.save(newEmployee));
		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
				
	}
	
	
	//Resource URL
	@GetMapping("/employees/{id}")
	public EntityModel<Employee> getEmployeeById(@PathVariable("id") Long id) {
		return assembler.toModel(repo.findById(id).orElseThrow(()->new EmployeeNotFoundException(id)));
	}
	
	@PutMapping("/employees/{id}")
	public EntityModel<Employee> updateEmployee(@RequestBody Employee newEmployee, @PathVariable("id") Long id) {
		Employee e1 =  repo.findById(id)
		.map(employee-> {
			employee.setName(newEmployee.getName());
			employee.setRole(newEmployee.getRole());
			employee.setId(id);
			return repo.save(employee);
		})
		.orElseThrow(()->new EmployeeNotFoundException(newEmployee.getId()));
		return assembler.toModel(e1);
	}
	
	@DeleteMapping("/employees/{id}")
	public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
		repo.deleteById(id);
		return ResponseEntity.noContent().build();
	}
	

}
