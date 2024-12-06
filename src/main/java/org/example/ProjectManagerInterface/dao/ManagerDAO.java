package org.example.ProjectManagerInterface.dao;


import org.example.ProjectManagerInterface.models.Manager;
import org.example.ProjectManagerInterface.models.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.List;

@Component
public class ManagerDAO {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ManagerDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ManagerDAO() {}

    public List<Manager> index() {
        return jdbcTemplate.query("SELECT * FROM manager", new BeanPropertyRowMapper<>(Manager.class));
    }

    public Manager show(int id) {
        return jdbcTemplate.query("SELECT * FROM manager WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Manager.class))
                .stream().findAny().orElse(null);
    }

    public void save(Manager manager) {
        jdbcTemplate.update("INSERT INTO manager(last_name, first_name, middle_name, email) VALUES(?,?,?,?)",
                manager.getLast_name(),
                manager.getFirst_name(),
                manager.getMiddle_name(),
                manager.getEmail());
    }

    public void update(int id, Manager updatedManager) {
        jdbcTemplate.update("UPDATE manager SET last_name = ?, first_name = ?, middle_name = ?, email = ? WHERE id = ?",
                updatedManager.getLast_name(),
                updatedManager.getFirst_name(),
                updatedManager.getMiddle_name(),
                updatedManager.getEmail(),
                id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM manager WHERE id = ?", id);
    }

    public List<Task> getTasksByManagerId(int managerId) {
        String sql = "SELECT t.* FROM task t JOIN manager m ON t.manager_id = m.id WHERE m.id = ?";
        return jdbcTemplate.query(sql, new Object[]{managerId}, new BeanPropertyRowMapper<>(Task.class));
    }

    public int getTaskCountByManagerId(int managerId) { // Использование функции подсчёта количества задач у определенного менеджера
        String sql = "SELECT GetTaskCountForManager(?)";
        return jdbcTemplate.queryForObject(sql, new Object[]{managerId}, Integer.class);
    }

}
