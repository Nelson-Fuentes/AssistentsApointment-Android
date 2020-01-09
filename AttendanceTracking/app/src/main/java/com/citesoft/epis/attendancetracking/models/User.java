package com.citesoft.epis.attendancetracking.models;

/**
 * Created by harold on 2/7/18.
 */

public class User {

    private int id;
    private String dni;
    private String name;
    private String lastname;
    private String email;
    private String phone;

    public User() {
    }

    public User(int id, String dni, String name, String lastname, String email, String phone) {
        this.id = id;
        this.dni = dni;
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
    }
    
    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public String getDni() {
        return dni;
    }

    public String getName() {
        return name;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}
