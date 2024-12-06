package org.example.ProjectManagerInterface.util;

import org.example.ProjectManagerInterface.dao.SpecialistDAO;
import org.example.ProjectManagerInterface.models.Specialist;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class SpecialistValidator implements Validator {

    private final SpecialistDAO specialistDAO;

    public SpecialistValidator(SpecialistDAO specialistDAO) {
        this.specialistDAO = specialistDAO;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Specialist.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Specialist specialist = (Specialist) target;

        // Ищем специалиста с такой же почтой
        Specialist existingSpecialist = specialistDAO.index().stream()
                .filter(s -> s.getEmail().equals(specialist.getEmail()))
                .findFirst()
                .orElse(null);

        // Проверяем, не совпадает ли ID с редактируемым
        if (existingSpecialist != null && existingSpecialist.getId() != specialist.getId()) {
            errors.rejectValue("email", "EmailNotUnique", "This email is already in use");
        }
    }
}