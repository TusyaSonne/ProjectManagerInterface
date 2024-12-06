package org.example.ProjectManagerInterface.util;

import org.example.ProjectManagerInterface.dao.ManagerDAO;
import org.example.ProjectManagerInterface.models.Manager;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ManagerValidator implements Validator {

    private final ManagerDAO managerDAO;

    public ManagerValidator(ManagerDAO managerDAO) {
        this.managerDAO = managerDAO;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Manager.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Manager manager = (Manager) target;

        // Получаем менеджера с такой же почтой, если он существует
        Manager existingManager = managerDAO.index().stream()
                .filter(m -> m.getEmail().equals(manager.getEmail()))
                .findFirst()
                .orElse(null);

        // Если найденный менеджер существует и это не тот же самый менеджер, то почта не уникальна
        if (existingManager != null && existingManager.getId() != manager.getId()) {
            errors.rejectValue("email", "EmailNotUnique", "Эта почта уже занята");
        }
    }
}
