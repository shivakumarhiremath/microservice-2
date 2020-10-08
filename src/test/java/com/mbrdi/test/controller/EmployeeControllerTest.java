package com.mbrdi.test.controller;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.mbrdi.controller.EmployeeController;
import com.mbrdi.service.EmployeeService;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
@WebMvcTest(controllers = EmployeeController.class)
@ContextConfiguration(classes = EmployeeController.class)
public class EmployeeControllerTest {
	
	@Mock
	private EmployeeController employeeController;
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private EmployeeService employeeService;
	
	@Test
	public void testReadEmployeesDetails() {
	    
	    Mockito.when(employeeService.readEmployeeDetails(Mockito.anyString())).thenReturn(new ResponseEntity<>(Mockito.anyString(),HttpStatus.OK));
	    
	    RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/readEmployeesDetails/Mary_2200-03-20.CSV").accept(MediaType.APPLICATION_JSON);
		try {
			MvcResult result = mockMvc.perform(requestBuilder).andReturn();
			MockHttpServletResponse response = result.getResponse();
			assertEquals(HttpStatus.OK.value(), response.getStatus());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testReadEmployeesDetailsFailedCondition() {
	    
	    Mockito.when(employeeService.readEmployeeDetails(Mockito.anyString())).thenReturn(new ResponseEntity<>(Mockito.anyString(),HttpStatus.BAD_REQUEST));
	    
	    RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/readEmployeesDetails/Mary_2200-03-20.CSV").accept(MediaType.APPLICATION_JSON);
		try {
			MvcResult result = mockMvc.perform(requestBuilder).andReturn();
			MockHttpServletResponse response = result.getResponse();
			assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
