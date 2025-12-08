# TravSignOnService
A simple sign on service allowing you to Create, Access(Read), Update or Delete a standard user account    
Provide your name and email and we will enter you into our database. A unique ID will be generated for you    
  
## Running with Docker
This is a gradle project so first create the JAR file by running ***"./gradlew clean build"*** in the projects root terminal   
This will create the JAR file -- **TravSignOnService-fat.jar** -- inside the build/libs folder  

### Creating the Image    
In the projects root terminal run  ***"docker build -t trav-sign-on-service ."***   
This will create the image with the name ***trav-sign-on-service***       
Feel free to use any name just make sure it is lowercase    
  
### Running the Container  
The server is configured to listen on port 8080   
use ***"docker run -d -p 8080:8080 --name user-service-container trav-sign-on-service"*** to start the container  
- This command maps your hosts port at 8080 to the containers port    
* Gives the container the name **user-service-container**  
+ Utilizes the beforementioned image name **trav-sign-on-service**  
  
You should get a container ID as a response e.g. ***a0345e64a626147ccfa699ec81b16d5153dedf989ed646d9743befbb2a29b603***  
    
Now the server should be running and you can trigger the exposed HTTP Endpoints  
   
POST http://localhost:8080/users *(create user)*  
GET http://localhost:8080/users/id *(get user by id)*  
PUT http://localhost:8080/users/id *(update user email)*  
DELETE http://localhost:8080/users/id *(delete user)*  
 
