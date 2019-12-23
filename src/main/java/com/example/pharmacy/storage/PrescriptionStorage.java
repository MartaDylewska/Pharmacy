package com.example.pharmacy.storage;

import com.example.pharmacy.type.Prescription;
import com.example.pharmacy.type.UserRole;

import java.util.List;

public interface PrescriptionStorage {

    //dla aptekarza
    Prescription getPrescription(long id, String pesel);

    Prescription deletePrescription (long id, String pesel);

    //dla lekarza
    String addPrescritpion(Prescription prescription, String pesel);

    void addUsers(Prescription prescription);

    //dla klienta
    List<Prescription> getAll(String patientPesel);



}
