package com.mbrdi.service;

import org.springframework.http.ResponseEntity;

public interface EmployeeService {

	String storeEmployeeDetailsInCSV(String employeeData);
	
	String storeEmployeeDetailsInXml(String employeeData);
	
	String updateCsvFileEmployeeDetails(String employeeData);
	
	String updateXmlFileEmployeeDetails(String employeeData);

	String findTheEmployeeFile(String FileName);

	ResponseEntity<String> readEmployeeDetails(String fileName);
	
}
