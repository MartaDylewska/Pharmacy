package com.example.pharmacy.storage.impl;

import com.example.pharmacy.storage.PrescriptionStorage;
import com.example.pharmacy.type.Prescription;
import com.example.pharmacy.type.User;
import com.example.pharmacy.type.UserRole;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StaticListPrescriptionStorageImpl implements PrescriptionStorage {

    public static List<Prescription> prescriptionStorage = new ArrayList<>();
    public static Set<User> userStorage = new HashSet<>();

    User pharmacist1 = new User("Agnieszka", "Kozlowska","88020505182",UserRole.PHARMACIST);
    User patient1 = new User("Marta", "Dylewska","85111203456",UserRole.PATIENT);
    User doctor1 = new User("Paweł", "Adamowicz", "74061309098", UserRole.DOCTOR);
    User pharmacist2 = new User("Paulina", "Sroka","78020505182",UserRole.PHARMACIST);
    User patient2 = new User("Maciej", "Dylewski","85101003446",UserRole.PATIENT);
    User doctor2 = new User("Aleksander", "Adamik", "73071809098", UserRole.DOCTOR);


    String[] medicines1 =  new String[]{"Acard 3op. 5ml","Traumal 4 szt. 10ml"};
    String[] medicines2 = new String[]{"Mucosolvan 1op. 50ml", "Dicortineff 1op. 15ml"};
    String[] medicines3 = new String[]{"Dicortineff 1op. 15ml"};
    String[] medicines4 = new String[]{"Sambucon 1op. 50ml"};

    Prescription prescription1 = new Prescription();
    Prescription prescription2 = new Prescription();
    Prescription prescription3 = new Prescription();
    Prescription prescription4 = new Prescription();
   // prescription1 customer1,doctor1,new String[]{"Acard 3op. 5ml","Traumal 4 szt. 10ml"});
    //Prescription prescription2 = new Prescription(customer1, doctor1,new String[]{"Mucosolvan 1op. 50ml", "Dicortineff 1op. 15ml"});
    //Prescription prescription3 = new Prescription(customer2, doctor1,new String[]{"Dicortineff 1op. 15ml"});
    //Prescription prescription4 = new Prescription(customer2, doctor2,new String[]{"Sambucon 1op. 50ml"});

    public void setPrescription(Prescription prescription, User doctor, User patient, String[] medicines){
        prescription.setDoctor(doctor);
        prescription.setPatient(patient);
        prescription.setDateOfIssue(LocalDate.now());
        prescription.setId();
        prescription.setMedicines(medicines);
    }
    public void addAllPrescritions(){
        setPrescription(prescription1,doctor1,patient1, medicines1);
        prescriptionStorage.add(prescription1);

        setPrescription(prescription2, doctor2, patient2,medicines3);
        prescriptionStorage.add(prescription2);

        setPrescription(prescription3,doctor1,patient2,medicines2);
        prescriptionStorage.add(prescription3);

        setPrescription(prescription4, doctor2, patient1, medicines4);
        prescriptionStorage.add(prescription4);

        userStorage.add(patient1);
        userStorage.add(patient2);
        userStorage.add(pharmacist1);
        userStorage.add(pharmacist2);
        userStorage.add(doctor1);
        userStorage.add(doctor2);

       // return prescriptionStorage;
    }

    public static void serializePrescriptions(List<Prescription> prescriptions ){
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("Prescriptions.bin"));
            outputStream.writeObject(prescriptions);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void serializeUsers(Set<User> users ){
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("Users.bin"));
            outputStream.writeObject(users);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Prescription> deserializePrescriptions(){
        List<Prescription> deserializedPrescriptions = null;
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("Prescriptions.bin"));
            deserializedPrescriptions = (List<Prescription>) inputStream.readObject();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return deserializedPrescriptions;
    }

    public static Set<User> deserializeUsers(){
        Set<User> deserializedUsers = null;
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("Users.bin"));
            deserializedUsers = (Set<User>) inputStream.readObject();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return deserializedUsers;
    }

    @Override
    public Prescription getPrescription(long id, String pharmacistPesel) {
        UserRole userRole = null;
        for(User u: userStorage){
            if(u.getPesel().equals(pharmacistPesel)){
                userRole = u.getUserRole();
            }
        }
        if(userRole.equals(UserRole.PHARMACIST)) {
            for (Prescription p : prescriptionStorage) {
                if (p.getId() == id)
                    return p;
            }
        }
        else {
            System.out.println("You are not a pharmacist");
        }
        return null;
    }

    @Override
    public Prescription deletePrescription(long id, String pharmacistPesel) {
            Prescription prescriptionToRemove = getPrescription(id,pharmacistPesel);
            prescriptionStorage.remove(prescriptionToRemove);
            return prescriptionToRemove;
    }

    @Override
    public String addPrescritpion(Prescription prescription, String doctorPesel) {
        UserRole userRole = null;
        for(User u: userStorage){
            if(u.getPesel().equals(doctorPesel)){
                userRole = u.getUserRole();
            }
        }

        if(userRole.equals(UserRole.DOCTOR)) {
            prescriptionStorage.add(prescription);
            userStorage.add(prescription.getDoctor());
            userStorage.add(prescription.getPatient());
            return "Prescription with id of: " + prescription.getId() + " was added to database";
        }
        else {
            return "You are not a doctor";
        }

    }
    @Override
    public void addUsers(Prescription prescription){
        userStorage.add(prescription.getDoctor());
        userStorage.add(prescription.getPatient());
    }

    @Override
    public List<Prescription> getAll(String patientPesel) {
        UserRole userRole = null;
        for(User u: userStorage){
            if(u.getPesel().equals(patientPesel)){
                userRole = u.getUserRole();
            }
        }
        System.out.println(userRole.toString());
        if(userRole.equals(UserRole.PATIENT)) {
            List<Prescription> patientPrescriptions = new ArrayList<>();
            for(Prescription p: prescriptionStorage){
                if(p.getPatient().getPesel().equals(patientPesel))
                    patientPrescriptions.add(p);
            }
            return patientPrescriptions;
        }
        else {
            System.out.println("Wrong patient pesel");
        }
        return null;
    }
}
