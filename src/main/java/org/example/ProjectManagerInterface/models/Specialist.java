package org.example.ProjectManagerInterface.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.sql.Time;
import java.time.LocalDate;

public class Specialist {

    private int id;

    @NotEmpty(message = "Фамилия не может быть пустой")
    private String last_name;

    @NotEmpty(message = "Имя не может быть пустым")
    private String first_name;

    private String middle_name;

    @NotEmpty(message = "Специализация не может быть пустой")
    private String specialization;

    @NotEmpty(message = "Почта не может быть пустой")
    @Email
    private String email;

    public Specialist() {}

    public Specialist(int id, String last_name, String first_name, String middle_name, String specialization, String email) {
        this.id = id;
        this.last_name = last_name;
        this.first_name = first_name;
        this.middle_name = middle_name;
        this.specialization = specialization;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getMiddle_name() {
        return middle_name;
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
