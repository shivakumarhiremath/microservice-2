# Microservice-2
An assignment application of mbrdi

------------
INTRODUCTION
------------

The Project is an application developed as an assignment for the MBRDI, There are two microservices named as microservice-1 & microservice-2. The microservice-1 is the
consumer facing service, which consists the REST End Points for the operations Store, Update & Read. These inputs would be handled by the controller and the data would
be converted from JSON to Protobuf and that data would be encrypted using AES encryption and then it would be sent to microservice-2 with the help of RabbitMQ Messages. 

The Communication between microservice-1 & microservice-2 is configured using the RabbitMQ Queues, RoutingKey and Direct Exchange. Each operation has its own queue & 
routing key configured, which would pass the request received from microservice-1 to microservice-2. The AES provides the encryption & decryption of the data being 
transferred , before transmission of data the data would be converted to google protobuf format and would be encrypted or decrypted on both the ends.

The microservice-2 is responsible for processing the request and providing the service for microservice-1, the recieved data would be decrypted first and would be 
converted from google protobuf to JSON and then according to request it would call the service class actions, need to configure the directory location in the file
application.properties for storing or updating the respective files.

-------------
PREREQUISITES
-------------

* JAVA 11 or compatible version to be installed on the machine, as the development of project is carried out on JAVA language.
* RabbitMQ server with version 3.8.3 or compatible needs to be installed on the machine as for the communication between microservice-1 & microservice-2.  
* Erlang with version OTP-22.3 or compatible needs to be installed as RabbitMQ is written on Erlang hence to run it Erlang should be installed on the machine.
* Postman with version 7.33.1 or compatible needs to be installed to run and verify the REST End Points, API document would be attached for the reference.
* Eclipse IDE with version 2020-09 (4.17.0) or compatible needs to be installed to run the microservices as well as for unit testing of the APIs.
* folder.location filed in application.properties file present in src/main/resource of both microservice-1 & microservice-2 should be changed according to desired 
  location for storing, updation or reading of files operations to carry out.

-------------------------
CONFIGURATION & EXECUTION
-------------------------

* RUNNING MICROSERVICE-1 AND MICROSERVICE-2

  - Clone both the microservices locally on the machine and then open an Eclipse IDE and import the files.
  - To Import the files on Eclipse IDE, look for import, click on it and choose the maven and click on exisiting_maven_project and add the pom.xml file of both the 
    services, which would include all the project source file.
  - After importing all the source file, update the project ( ALT+F5 ) select both & click on force update the project & after updating, open Microservice1Application.java
    which is present in com.mbrdi.main package of microservice-1 folder and run the microservice-1 application follow the same process, open Microservice2Application.java
    present in present in com.mbrdi.main package of microservice-2 folder and run the microservice-2 application.
  - After both the services are started up, send the REST API requests using the postman, API documentation would be attached.

* RUNNING THE JUNIT TESTS

  - Once the project has been added and updated in Eclipse IDE, Look for the src/test/java folder and the right click on EmployeeControllerTest.java file which is present
    in com.mbrdi.test.controller and after right click, click on Run As, and click on JUnit Test, which would run all the test cases and provides the result on IDE console   

----------------
TROUBLE SHOOTING
----------------

* JAVA version  
  
  - Look out for the JAVA version on machine and try to run accordingly, if it needs to run on lower version change the 'java.version' properties in pom.xml file of both
    the microservices, build it and run it again on the compatible version.   
