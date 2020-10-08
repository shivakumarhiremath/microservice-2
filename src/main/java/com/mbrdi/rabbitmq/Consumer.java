package com.mbrdi.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mbrdi.service.EmployeeService;

@Component
public class Consumer {
	
	@Autowired
	EmployeeService employeeService;

	 @RabbitListener(queues="${jsa.rabbitmq.store.csv.queue}")
	    public void recievedStoreCsvMessage(String msg) {
	        System.out.println("Recieved Message store csv: " + msg);
	        employeeService.storeEmployeeDetailsInCSV(msg);
	    }
	 
	 @RabbitListener(queues="${jsa.rabbitmq.update.csv.queue}")
	    public void recievedUpdateCsvMessage(String msg) {
	        System.out.println("Recieved Message update csv: " + msg);
	        employeeService.updateCsvFileEmployeeDetails(msg);
	    }
	 @RabbitListener(queues="${jsa.rabbitmq.store.xml.queue}")
	    public void recievedStoreXmlMessage(String msg) {
	        System.out.println("Recieved Message store xml : " + msg);
	        employeeService.storeEmployeeDetailsInXml(msg);
	    }
	 
	 @RabbitListener(queues="${jsa.rabbitmq.update.xml.queue}")
	    public void recievedUpdateXmlMessage(String msg) {
	        System.out.println("Recieved Message update xml : " + msg);
	        employeeService.updateXmlFileEmployeeDetails(msg);
	    }
	
}
