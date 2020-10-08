package com.mbrdi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbrdi.service.EmployeeService;

@RestController
@RequestMapping("/")
public class EmployeeController {

	@Autowired
	EmployeeService employeeService;
	
	@GetMapping("/readEmployeesDetails/{fileName}")
	public ResponseEntity<String> readEmployeeDetails(@PathVariable("fileName") String fileName) {
	
			return employeeService.readEmployeeDetails(fileName);
	}
}
