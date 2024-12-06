package org.example.ProjectManagerInterface.controllers;

import jakarta.validation.Valid;
import org.example.ProjectManagerInterface.dao.SpecialistDAO;
import org.example.ProjectManagerInterface.dao.TaskDAO;
import org.example.ProjectManagerInterface.models.Specialist;
import org.example.ProjectManagerInterface.models.Task;
import org.example.ProjectManagerInterface.util.SpecialistValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/specialists")
public class SpecialistController {

    private final SpecialistDAO specialistDAO;
    private final TaskDAO taskDAO;
    private final SpecialistValidator specialistValidator;

    @Autowired
    public SpecialistController(SpecialistDAO specialistDAO, TaskDAO taskDAO, SpecialistValidator specialistValidator) {
        this.specialistDAO = specialistDAO;
        this.taskDAO = taskDAO;
        this.specialistValidator = specialistValidator;
    }


    @GetMapping()
    public String index(Model model) {
        model.addAttribute("specialists", specialistDAO.index());
        return "specialists/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {

        model.addAttribute("tasks", specialistDAO.getTasksBySpecialistId(id));
        Specialist specialist = specialistDAO.show(id);
        model.addAttribute("specialist", specialist);

        return "specialists/show";
    }

    @GetMapping("/new")
    public String newAppointment(Model model, @ModelAttribute("specialist") Specialist specialist) {

        return "specialists/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("specialist") @Valid Specialist specialist, BindingResult bindingResult) {

        specialistValidator.validate(specialist, bindingResult);

        if (bindingResult.hasErrors()) {
            return "specialists/new";
        }

        specialistDAO.save(specialist);
        return "redirect:/specialists";
    }


    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("specialist", specialistDAO.show(id));

        return "specialists/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("specialist") @Valid Specialist specialist, BindingResult bindingResult, @PathVariable("id") int id) {

        specialistValidator.validate(specialist, bindingResult);

        if (bindingResult.hasErrors()) {
            return "specialists/edit";
        }
        specialistDAO.update(id, specialist);
        return "redirect:/specialists";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        specialistDAO.delete(id);
        return "redirect:/specialists";
    }

    @GetMapping("/{id}/add-distribution")
    public String addDistributionPage(@PathVariable("id") int id, Model model) {
        // Получаем специалиста по id
        Specialist specialist = specialistDAO.show(id);

        // Получаем все задачи
        List<Task> tasks = taskDAO.index();

        // Получаем задачи, которые уже связаны с данным специалистом
        List<Task> assignedTasks = taskDAO.getTasksBySpecialistId(id);
        // Извлекаем ID задач, назначенных специалисту
        List<Integer> assignedTaskIds = assignedTasks.stream()
                .map(Task::getId)
                .collect(Collectors.toList());



        // Добавляем в модель
        model.addAttribute("specialist", specialist);
        model.addAttribute("tasks", tasks);
        model.addAttribute("assignedTaskIds", assignedTaskIds);  // Передаем уже связанные задачи

        return "specialists/add_distribution";
    }

    @PostMapping("/{id}/add-distribution")
    public String addDistribution(@PathVariable("id") int id, @RequestParam("taskIds") List<Integer> selectedTaskIds) {
        // Получаем уже связанные задачи для этого специалиста
        List<Task> assignedTasks = taskDAO.getTasksBySpecialistId(id);

        // Удаляем старые записи из таблицы Work_distribution
        taskDAO.removeAllTaskAssignments(id);

        // Добавляем новые записи
        for (Integer taskId : selectedTaskIds) {
            taskDAO.addDistribution(id, taskId);
        }

        return "redirect:/specialists/" + id;
    }

}
