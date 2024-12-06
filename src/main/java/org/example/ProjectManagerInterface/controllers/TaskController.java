package org.example.ProjectManagerInterface.controllers;

import jakarta.validation.Valid;

import org.example.ProjectManagerInterface.dao.ManagerDAO;
import org.example.ProjectManagerInterface.dao.SpecialistDAO;
import org.example.ProjectManagerInterface.dao.TaskDAO;
import org.example.ProjectManagerInterface.models.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskDAO taskDAO;
    private final ManagerDAO managerDAO;
    private final SpecialistDAO specialistDAO;

    @Autowired
    public TaskController(TaskDAO taskDAO, ManagerDAO managerDAO, SpecialistDAO specialistDAO) {
        this.taskDAO = taskDAO;
        this.managerDAO = managerDAO;
        this.specialistDAO = specialistDAO;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("tasks", taskDAO.index());
        return "tasks/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        Task task = taskDAO.show(id);
        model.addAttribute("task", task);
        model.addAttribute("manager", taskDAO.getManagerById(task.getManager_id()));
        model.addAttribute("specialists", specialistDAO.getSpecialistsByTask(id));

        return "tasks/show";
    }

    @GetMapping("/new")
    public String newTask(Model model, @ModelAttribute("task") Task task) {
        model.addAttribute("managers", managerDAO.index());
        return "tasks/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("task") @Valid Task task, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "tasks/new";
        }

        taskDAO.save(task);
        return "redirect:/tasks";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        Task task = taskDAO.show(id);
        model.addAttribute("manager", taskDAO.getManagerById(task.getManager_id()));
        model.addAttribute("managers", managerDAO.index());
        model.addAttribute("task", task);
        return "tasks/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("task") @Valid Task task, BindingResult bindingResult, @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) {
            return "tasks/edit";
        }

        taskDAO.update(id, task);
        return "redirect:/tasks";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        taskDAO.delete(id);
        return "redirect:/tasks";
    }

    @GetMapping("/search")
    public String searchForm(Model model) {
        model.addAttribute("managers", managerDAO.index());
        model.addAttribute("specialists", specialistDAO.index());
        return "tasks/search";
    }

    @GetMapping("/results")
    public String searchResults(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "specialistId", required = false) Integer specialistId,
            @RequestParam(value = "managerId", required = false) Integer managerId,
            Model model) {

        List<Task> tasks = taskDAO.searchTasks(name, specialistId, managerId);
        model.addAttribute("tasks", tasks);
        return "tasks/results";
    }


}
