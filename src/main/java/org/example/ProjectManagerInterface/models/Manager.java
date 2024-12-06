package org.example.ProjectManagerInterface.models;


import jakarta.validation.constraints.*;

import java.util.List;

public class Manager {

    private int id;

    @NotEmpty(message = "Фамилия не может быть пустой")
//    @Max(value = 100, message = "Фамилия должно быть короче 100 символов")
//    @Min(value = 2, message = "Фамилия должно быть длиннее или равно 2 символов")
    private String last_name;

    @NotEmpty(message = "Имя не может быть пустым")
//    @Max(value = 100, message = "Имя должно быть короче 100 символов")
//    @Min(value = 2, message = "Имя должно быть длиннее или равно 2 символов")
    private String first_name;


    private String middle_name;

    @NotEmpty(message = "Почта не может быть пустой")
    @Email
    private String email;

    private List<Task> Tasks;

    public Manager() {}

    public Manager(int id, String last_name, String first_name, String middle_name, String email, List<Task> tasks) {
        this.id = id;
        this.last_name = last_name;
        this.first_name = first_name;
        this.middle_name = middle_name;
        this.email = email;
        Tasks = tasks;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Task> getTasks() {
        return Tasks;
    }

    public void setTasks(List<Task> tasks) {
        Tasks = tasks;
    }
}
