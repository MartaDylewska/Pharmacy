package com.example.pharmacy.type;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {


    private String firstName;
    private String lastName;
    private String pesel;

    private UserRole userRole;

    public User(){
    }

    public User(String firstName, String lastName, String pesel, UserRole userRole) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.pesel = pesel;
        this.userRole = userRole;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }


    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(getFirstName(), user.getFirstName()) &&
                Objects.equals(getLastName(), user.getLastName()) &&
                Objects.equals(getPesel(), user.getPesel()) &&
                getUserRole() == user.getUserRole();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getPesel(), getUserRole());
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", pesel='" + pesel + '\'' +
                ", userRole=" + userRole +
                '}';
    }
}
