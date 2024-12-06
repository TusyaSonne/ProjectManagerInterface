package org.example.ProjectManagerInterface.controllers;

import jakarta.validation.Valid;
import org.example.ProjectManagerInterface.dao.ManagerDAO;
import org.example.ProjectManagerInterface.models.Manager;
import org.example.ProjectManagerInterface.util.ManagerValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/managers")
public class ManagerController {

    private final ManagerDAO managerDAO;
    private final ManagerValidator managerValidator;

    @Autowired
    public ManagerController(ManagerDAO managerDAO, ManagerValidator managerValidator) {
        this.managerDAO = managerDAO;
        this.managerValidator = managerValidator;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("managers", managerDAO.index());
        return "managers/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        Manager manager = managerDAO.show(id);
        model.addAttribute("manager", manager);
        model.addAttribute("tasksCount", managerDAO.getTaskCountByManagerId(id));
        model.addAttribute("tasks", managerDAO.getTasksByManagerId(id));

        return "managers/show";
    }

    @GetMapping("/new")
    public String newManager(Model model, @ModelAttribute("manager") Manager manager) {
        return "managers/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("manager") @Valid Manager manager, BindingResult bindingResult) {

        managerValidator.validate(manager, bindingResult);

        if (bindingResult.hasErrors()) {
            return "managers/new";
        }

        managerDAO.save(manager);
        return "redirect:/managers";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("manager", managerDAO.show(id));
        return "managers/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("manager") @Valid Manager manager, BindingResult bindingResult, @PathVariable("id") int id) {

        managerValidator.validate(manager, bindingResult);

        if (bindingResult.hasErrors()) {
            return "managers/edit";
        }

        managerDAO.update(id, manager);
        return "redirect:/managers";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        managerDAO.delete(id);
        return "redirect:/managers";
    }
}
