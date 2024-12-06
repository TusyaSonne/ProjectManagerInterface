package org.example.ProjectManagerInterface.dao;


import org.example.ProjectManagerInterface.models.Manager;
import org.example.ProjectManagerInterface.models.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class TaskDAO {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public TaskDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public TaskDAO() {}

    public List<Task> index() {
        return jdbcTemplate.query("SELECT * FROM task", new BeanPropertyRowMapper<>(Task.class));
    }

    public Task show(int id) {
        return jdbcTemplate.query("SELECT * FROM task WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Task.class))
                .stream().findAny().orElse(null);
    }

    public void save(Task task) {
        jdbcTemplate.update("INSERT INTO task(name, description, deadline, manager_id) VALUES(?,?,?,?)",
                task.getName(),
                task.getDescription(),
                task.getDeadline(),
                task.getManager_id());
    }

    public void update(int id, Task updatedTask) {
        jdbcTemplate.update("UPDATE task SET name = ?, description = ?, deadline = ?, manager_id = ? WHERE id = ?",
                updatedTask.getName(),
                updatedTask.getDescription(),
                updatedTask.getDeadline(),
                updatedTask.getManager_id(),
                id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM task WHERE id = ?", id);
    }

    public Manager getManagerById(int id) {
        return jdbcTemplate.query("SELECT manager.* FROM task JOIN manager ON task.manager_id=manager.id WHERE manager.id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Manager.class))
                .stream().findAny().orElse(null);
    }

    public void addDistribution(int specialistId, int taskId) {
        String sql = "INSERT INTO Work_distribution (specialist_id, task_id) VALUES (?, ?)";

        // Добавляем только если записи еще нет
        String checkSql = "SELECT COUNT(*) FROM Work_distribution WHERE specialist_id = ? AND task_id = ?";
        Integer count = jdbcTemplate.queryForObject(checkSql, new Object[]{specialistId, taskId}, Integer.class);

        if (count == 0) {
            jdbcTemplate.update(sql, specialistId, taskId);
        }
    }

    public void removeAllTaskAssignments(int specialistId) {
        String sql = "DELETE FROM Work_distribution WHERE specialist_id = ?";
        jdbcTemplate.update(sql, specialistId);
    }

    public List<Task> getTasksBySpecialistId(int specialistId) {
        String sql = "SELECT t.* FROM Task t " +
                "JOIN Work_distribution wd ON t.id = wd.task_id " +
                "WHERE wd.specialist_id = ?";
        return jdbcTemplate.query(sql, new Object[]{specialistId}, new BeanPropertyRowMapper<>(Task.class));
    }

    public List<Task> searchTasks(String name, Integer specialistId, Integer managerId) {
        StringBuilder sql = new StringBuilder("SELECT DISTINCT t.* FROM task t ");
        List<Object> params = new ArrayList<>();

        if (specialistId != null) {
            sql.append("JOIN Work_distribution wd ON t.id = wd.task_id ");
        }
        sql.append("WHERE 1=1 ");

        if (name != null && !name.isEmpty()) {
            sql.append("AND t.name LIKE ? ");
            params.add("%" + name + "%");
        }
        if (specialistId != null) {
            sql.append("AND wd.specialist_id = ? ");
            params.add(specialistId);
        }
        if (managerId != null) {
            sql.append("AND t.manager_id = ? ");
            params.add(managerId);
        }

        return jdbcTemplate.query(sql.toString(), params.toArray(), new BeanPropertyRowMapper<>(Task.class));
    }


}
