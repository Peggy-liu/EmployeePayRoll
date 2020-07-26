package com.payroll.example.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Employee {
	

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long Id;
	private String firstname;
	private String lastname;
	private String role;
	
	//the default constructor is used by JPA
	protected Employee() {
		
			}
   //this constructor is used by user
	public Employee(String firstname, String lastname, String role) {
		super();
		this.firstname = firstname;
		this.lastname = lastname;
		this.role = role;
		
	}
	
	public Long getId() {
		return Id;
	}
	public void setId(Long id) {
		Id = id;
	}
	//a virtual getter for client JSON to get "name"
	public String getName() {
		return this.firstname+" "+this.lastname;
	}
	//a virtual setter for old client using "name"
	//setter is called when the client passed the Employee to the server
	public void setName(String name) {
		String[] parts = name.split(" ");
		this.firstname = parts[0];
		this.lastname = parts[1];
				}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getfirstname() {
		return firstname;
	}
	public void setfirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getlastname() {
		return lastname;
	}
	public void setlastname(String lastname) {
		this.lastname = lastname;
	}
}
