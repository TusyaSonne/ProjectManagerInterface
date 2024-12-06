package org.example.ProjectManagerInterface.dao;


import org.example.ProjectManagerInterface.models.Manager;
import org.example.ProjectManagerInterface.models.Specialist;
import org.example.ProjectManagerInterface.models.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.Arrays.stream;

@Component
public class SpecialistDAO {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public SpecialistDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public SpecialistDAO() {
    }

    public List<Specialist> index() {
        return jdbcTemplate.query("SELECT * FROM specialist", new BeanPropertyRowMapper<>(Specialist.class));
    }

    public Specialist show(int id) {
        return jdbcTemplate.query("SELECT * FROM specialist WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Specialist.class))
                .stream().findAny().orElse(null);// ПРОВЕРИТЬ ЧТО ПОЛЯ СОВПАДАЮТ ПО ИМЕНИ В ПОЛЯМИ В ТАБЛИЦЕ
    }

    public void save(Specialist specialist) { // Вызов процедуры addSpecialist
        jdbcTemplate.update(
                "CALL addSpecialist(?, ?, ?, ?, ?)",
                specialist.getLast_name(),
                specialist.getFirst_name(),
                specialist.getMiddle_name(),
                specialist.getSpecialization(),
                specialist.getEmail()
        );
    }



    public void update(int id, Specialist updatedSpecialist) {
        jdbcTemplate.update("UPDATE specialist SET last_name = ?, first_name = ?, middle_name = ?, specialization = ?, email = ? WHERE id = ?", updatedSpecialist.getLast_name(), updatedSpecialist.getFirst_name(), updatedSpecialist.getMiddle_name(), updatedSpecialist.getSpecialization(), updatedSpecialist.getEmail(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM specialist WHERE id = ?", id);
    }

    public List<Task> getTasksBySpecialistId(int id) {
        return jdbcTemplate.query("SELECT task.* FROM specialist JOIN work_distribution ON specialist.id=work_distribution.specialist_id JOIN task ON work_distribution.task_id=task.id WHERE specialist.id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Task.class));
    }

    public List<Specialist> getSpecialistsByTask(int taskId) {
        String sql = "SELECT s.* FROM Specialist s " +
                "JOIN Work_distribution wd ON s.id = wd.specialist_id " +
                "WHERE wd.task_id = ?";
        return jdbcTemplate.query(sql, new Object[]{taskId}, new BeanPropertyRowMapper<>(Specialist.class));
    }

}
