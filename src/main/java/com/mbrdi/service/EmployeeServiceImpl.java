package com.mbrdi.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Struct;
import com.google.protobuf.Struct.Builder;
import com.google.protobuf.TextFormat.ParseException;
import com.google.protobuf.util.JsonFormat;
import com.mbrdi.EncryptDecrypt.AESEncryptDecryptUtil;
import com.mbrdi.domain.Employee;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

@Component
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	Environment environment;
	
	@Autowired
	AESEncryptDecryptUtil encryptUtils;

	@Override
	public ResponseEntity<String> readEmployeeDetails(String fileName) {
		String[] fileType = fileName.split("\\.");

		if (fileType[1].equalsIgnoreCase("CSV")) {
			
			Employee e = readFromCSV(fileName);
			Builder structBuilder = JsonToProtobufConverter(e);
			String encryptedData = encryptUtils.encrypt(structBuilder.toString());
			return new ResponseEntity<>(encryptedData,HttpStatus.OK);
			
		} else if (fileType[1].equalsIgnoreCase("XML")) {
			
			Employee e = readFromXML(fileName);
			Builder structBuilder = JsonToProtobufConverter(e);
			String encryptedData = encryptUtils.encrypt(structBuilder.toString());
			return new ResponseEntity<>(encryptedData,HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<>("Error : Couldn't find the file ",HttpStatus.BAD_REQUEST);
		}
	}
	
	@Override
	public String storeEmployeeDetailsInCSV(String employeeData) {

		ObjectMapper mapper = new ObjectMapper();
		
		try {
			String decryptedData = encryptUtils.decrypt(employeeData);
			String convertedData = ProtobufToJsonConverter(decryptedData);
			Employee employee = mapper.readValue(convertedData, Employee.class);
			writeToCSV(employee);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String storeEmployeeDetailsInXml(String employeeData) {

		ObjectMapper mapper = new ObjectMapper();
		
		try {
			String decryptedData = encryptUtils.decrypt(employeeData);
			String convertedData = ProtobufToJsonConverter(decryptedData);
			Employee employee = mapper.readValue(convertedData, Employee.class);
			writeToXML(employee);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String updateCsvFileEmployeeDetails(String employeeData) {
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			String decryptedData = encryptUtils.decrypt(employeeData);
			String convertedData = ProtobufToJsonConverter(decryptedData);
			Employee employee = mapper.readValue(convertedData, Employee.class);
			writeToCSV(employee);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String updateXmlFileEmployeeDetails(String employeeData) {
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			String decryptedData = encryptUtils.decrypt(employeeData);
			String convertedData = ProtobufToJsonConverter(decryptedData);
			Employee employee = mapper.readValue(convertedData, Employee.class);
			writeToXML(employee);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void writeToCSV(Employee employee) {
		final String COMMA_DELIMITER = ",";
		final String LINE_SEPARATOR = "\n";
		final String HEADER = "Name,DOB,Salary,Age";

		FileWriter fileWriter = null;

		try {

			String folder = environment.getProperty("folder.location");
			fileWriter = new FileWriter(
					folder + "\\" + employee.getEmployeeName() + "_" + employee.getDateOfBirth() + ".csv");

			// Adding the header
			fileWriter.append(HEADER);
			// New Line after the header
			fileWriter.append(LINE_SEPARATOR);

			fileWriter.append(employee.getEmployeeName());
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(employee.getDateOfBirth());
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(employee.getSalary()));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(employee.getAge()));
			fileWriter.append(COMMA_DELIMITER);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fileWriter.close();
			} catch (IOException ie) {
				System.out.println("Error occured while closing the fileWriter");
				ie.printStackTrace();
			}
		}
	}

	private void writeToXML(Employee employee) {

		try {

			// Create JAXB Context
			JAXBContext jaxbContext = JAXBContext.newInstance(Employee.class);

			// Create Marshaller
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			// Required formatting??
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			// Store XML to File
			String folder = environment.getProperty("folder.location");
			File file = new File(folder + "\\" + employee.getEmployeeName() + "_" + employee.getDateOfBirth() + ".xml");

			// If we DO NOT have JAXB annotated class
			JAXBElement<Employee> jaxbElement = new JAXBElement<Employee>(new QName("", "employee"), Employee.class,
					employee);

			// Writes XML file to file-system
			jaxbMarshaller.marshal(jaxbElement, file);

		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	private Employee readFromXML(String fileName) {
		Employee employee = new Employee();
		try {
			String folder = environment.getProperty("folder.location");
			File file = new File(folder + "\\" + fileName);
			JAXBContext jaxbContext = JAXBContext.newInstance(Employee.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			// to unmarshal with using the @XmlRootElement(name="employee")
			// above the Employee class name in Employee.java file
			// employee = (Employee) jaxbUnmarshaller.unmarshal(file);

			JAXBElement<Employee> jaxbElement = (JAXBElement<Employee>) jaxbUnmarshaller
					.unmarshal(new StreamSource(file), Employee.class);

			employee = jaxbElement.getValue();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return employee;
	}

	private Employee readFromCSV(String fileName) {
		final String COMMA_DELIMITER = ",";
		Scanner scanner = null;
		Employee employee = new Employee();
		try {
			// Get the scanner instance
			String folder = environment.getProperty("folder.location");
			File file = new File(folder + "\\" + fileName);
			scanner = new Scanner(file);
			// Use Delimiter as COMMA
			scanner.useDelimiter(COMMA_DELIMITER);

			while (scanner.hasNext()) {
				scanner.nextLine();
				// System.out.print(scanner.next()+" ");
				employee.setEmployeeName(scanner.next());
				// Date dateOfBirth = new SimpleDateFormat("yyyy-MM-dd").parse(scanner.next());
				// employee.setDateOfBirth(dateOfBirth);
				employee.setDateOfBirth(scanner.next());
				employee.setSalary(scanner.nextFloat());
				employee.setAge(scanner.nextInt());
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			scanner.close();
		}

		return employee;
	}

	@Override
	public String findTheEmployeeFile(String FileName) {
		String folder = environment.getProperty("folder.location");
		File file = new File(folder + "\\" + FileName);
		if (file.exists()) {
			return file.getName();
		} else {
			return null;
		}
	}
	
	/*
	 * Method which converts the protobuf format data to json format data
	 */
	private String ProtobufToJsonConverter(String protoBufReceivedMsg) {
		try {
			CharSequence data = protoBufReceivedMsg;
			Builder structBuilder = Struct.newBuilder();
			com.google.protobuf.TextFormat.getParser().merge(data, structBuilder);
			return JsonFormat.printer().print(structBuilder);
		} catch (InvalidProtocolBufferException | ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/*
	 * Method which converts the json format data to protobuf format data
	 */
	private Builder JsonToProtobufConverter(Object obj)
	{
		JSONObject parameters = new JSONObject(obj);
		Builder structBuilder = Struct.newBuilder();
		try {
			JsonFormat.parser().merge(parameters.toString(), structBuilder);
			
			System.out.println("from protobuf to json format -> "+JsonFormat.printer().print(structBuilder));
			
			return structBuilder;
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
			return null;
		}
	}
}
