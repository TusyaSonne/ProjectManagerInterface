package org.example.ProjectManagerInterface.models;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class Task {
    private int id;

    @NotEmpty(message = "Задача не может быть пустой")
    private String name;
    @NotEmpty(message = "Добавьте описание")
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate deadline;

    private int manager_id;

    public Task() {}

    public Task(int id, String name, String description, LocalDate deadline, int manager_id) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.manager_id = manager_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public int getManager_id() {
        return manager_id;
    }

    public void setManager_id(int manager_id) {
        this.manager_id = manager_id;
    }
}
