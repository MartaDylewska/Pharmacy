# PHARMACY
REST application for prescription management.
- 3 types of users: pharmacist, doctor, patient, with different priviledges
- possibility to view/add/delete prescriptions
- object serialization after every add/delete operation

#TECH STACK
- Java 8
- Postman (as a REST client)
- Maven
- IDE: Intellij Idea

All prescriptions for a given patient pesel can be viewed:

![getPrescrpiton](src/main/resources/getPresc.PNG)

Prescriptions can be added:
![postPresc](src/main/resources/postPresc.PNG)

...and a new id is automatically generated.
![postPrescRes](src/main/resources/postPrescResult.PNG)

Deleting prescription:
![del](src/main/resources/deletePresc.PNG)
