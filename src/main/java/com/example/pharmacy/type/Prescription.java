package com.example.pharmacy.type;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Arrays;

public class Prescription implements Serializable {

    private static long idCounter = System.currentTimeMillis();

    private long id;
    private User patient;
    private LocalDate dateOfIssue;
    private String[] medicines;
    private User doctor;

    public Prescription(){
    }

    public long getId() {
        return id;
    }

    public void setId(){
        this.id = idCounter;
        idCounter++;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getPatient() {
        return patient;
    }

    public void setPatient(User patient) {
        if(patient.getUserRole().equals(UserRole.PATIENT))
            this.patient = patient;
    }

    public LocalDate getDateOfIssue() {
        return dateOfIssue;
    }

    public void setDateOfIssue(LocalDate dateOfIssue) {
        this.dateOfIssue = dateOfIssue;
    }

    public String[] getMedicines() {
        return medicines;
    }

    public void setMedicines(String[] medicines) {
        this.medicines = medicines;
    }

    public User getDoctor() {
        return doctor;
    }

    public void setDoctor(User doctor) {
        if(doctor.getUserRole().equals(UserRole.DOCTOR))
            this.doctor = doctor;
    }

    @Override
    public String toString() {
        return "Prescription{" +
                "id=" + id +
                ", patient=" + patient +
                ", dateOfIssue=" + dateOfIssue +
                ", medicines=" + Arrays.toString(medicines) +
                ", doctor=" + doctor +
                '}';
    }
}
