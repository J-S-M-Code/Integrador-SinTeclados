package domain.model;

import infrastructure.exception.BusinessRuleViolationsException;

import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class Proyect {
    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private ProyectStatus status;
    private Optional<String> description;

    private Proyect(Long id,
                     String name,
                     LocalDate startDate,
                     LocalDate endDate,
                     ProyectStatus status,
                     Optional<String> description) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.description = description;

    }

    /**
     *  Utiliza Factory Method
     *  El id no los da la db
     * @param id
     * @param name
     * @param startDate
     * @param endDate
     * @param status
     * @param description
     * @return nuevo proyecto
     */
    public static Proyect create(Long id,
                                 String name,
                                 LocalDate startDate,
                                 LocalDate endDate,
                                 ProyectStatus status,
                                 Optional<String> description){
        if (id == null){
            throw new BusinessRuleViolationsException("El id nulo");
        }
        if (name == null || name.isBlank()) {
            throw new BusinessRuleViolationsException("El nombre no puede ser nulo");
        }
        if (startDate == null || endDate == null) {
            throw new BusinessRuleViolationsException("La fecha de inicio o final final no pueden ser nulas");
        }
        if (startDate.isAfter(endDate) || startDate.isAfter(LocalDate.now())){
            throw new BusinessRuleViolationsException("La fecha de inicio es invalida");
        }
        if (endDate.isAfter(LocalDate.now())){
            throw new BusinessRuleViolationsException("La fecha de fin es invalida");
        }
        if (status == null) {
            throw new BusinessRuleViolationsException("El estatus del proyecto no puede ser nulo");
        }
        return new Proyect(id, name, startDate, endDate, status, description);
    }
}
