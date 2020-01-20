package com.citesoft.epis.attendancetracking.models;

/**
 * Created by harold on 2/7/18.
 */

public class User {

    private String dni;
    private String first_name;
    private String last_name;
    private String email;
    private String phone;

    public User() {
    }

    public User(int id, String dni, String first_name, String last_name, String email, String phone) {
        this.dni = dni;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.phone = phone;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setFirstName(String first_name) { this.first_name = first_name; }

    public void setLastname(String last_name) {
        this.last_name = last_name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDni() {
        return this.dni;
    }

    public String getFirstName() {
        return this.first_name;
    }

    public String getLastname() {
        return this.last_name;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPhone() {
        return this.phone;
    }

}
