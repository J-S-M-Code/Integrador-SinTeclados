package domain.model;

import infrastructure.exception.BusinessRuleViolationsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ProyectTest {
    @Test
    @DisplayName("Debe crear un Proyect exitosamente cuando los datos son válidos")
    void testCreateProyect_ShouldSucceed_WhenDataIsValid() {
        Long id = 123456789L;
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(10);
        String name = "Proyecto Test";

        // 1. Act (Actuar)
        Proyect proyect = Proyect.create(id,
                name,
                startDate,
                endDate,
                ProyectStatus.PLANNED,
                Optional.of("Descripción de prueba")
        );

        // 2. Assert (Verificar)
        assertNotNull(proyect);
        assertEquals(name, proyect.getName());
        assertEquals(startDate, proyect.getStartDate());
        assertEquals(endDate, proyect.getEndDate());
        assertEquals(ProyectStatus.PLANNED, proyect.getStatus());
        assertEquals(id, proyect.getId());
    }

    @Test
    @DisplayName("Debe lanzar excepción si endDate es anterior a startDate")
    void testCreateProyect_ShouldThrowException_WhenEndDateIsBeforeStartDate() {
        Long id = 123456789L;
        String name = "Proyecto Test";
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.minusDays(1); // Fecha inválida

        // Act y Assert
        // Verificamos que se lanza la excepción correcta
        Exception exception = assertThrows(BusinessRuleViolationsException.class, () -> {
            Proyect.create(id,
                    name,
                    startDate,
                    endDate,
                    ProyectStatus.PLANNED,
                    Optional.of("Descripción de prueba")
            );
        });

        // Verificamos el mensaje (basado en la regla del README)
        assertEquals("La fecha de fin es invalida", exception.getMessage());
    }

    @Test
    @DisplayName("Debe lanzar excepción si endDate es anterior a hoy")
    void testCreateProyect_ShouldThrowException_WhenEndDateIsInThePast() {
        Long id = 123456789L;
        String name = "Proyecto Test";
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(-10);//Fecha invalida

        // Act y Assert
        Exception exception = assertThrows(BusinessRuleViolationsException.class, () -> {
            // Usamos 'startDate' Y endDate para que pase la primera validación
            // pero falle la segunda (endDate >= startDate)
            Proyect.create(id,
                    name,
                    startDate,
                    endDate,
                    ProyectStatus.PLANNED,
                    Optional.of("Descripción de prueba")
            );
        });

        assertEquals("La fecha de fin es invalida", exception.getMessage());
    }

    @Test
    @DisplayName("Debe lanzar excepción si falta un campo requerido (ej: name es null)")
    void testCreateProyect_ShouldThrowException_WhenNameIsNull() {
        Long id = 123456789L;
        String name = null;
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(10);
        // Act y Assert
        Exception exception = assertThrows(BusinessRuleViolationsException.class, () -> {
            Proyect.create(id,
                    name,
                    startDate,
                    endDate,
                    ProyectStatus.PLANNED,
                    Optional.of("Descripción de prueba")
            );
        });

        assertEquals("El nombre no puede ser nulo", exception.getMessage());
    }

  //  @Test
  //  @DisplayName("canAddTask debe devolver true si el estado NO es CLOSED")
  //  void testCanAddTask_ShouldReturnTrue_WhenStatusIsActive() {
  //      // Creamos un proyecto válido con estado ACTIVE
  //      Proyect activeProyect = Proyect.create(
  //              "Proyecto Activo",
  //              LocalDate.now(),
  //              LocalDate.now().plusDays(1),
  //              "Desc",
  //              ProyectStatus.ACTIVE // Estado NO cerrado
  //      );
  //
  //      // Assert
  //      assertTrue(activeProyect.canAddTask());
  //  }
  //
  //  @Test
  //  @DisplayName("canAddTask debe devolver false si el estado ES CLOSED")
  //  void testCanAddTask_ShouldReturnFalse_WhenStatusIsClosed() {
  //      // Creamos un proyecto válido con estado CLOSED
  //      Proyect closedProyect = Proyect.create(
  //              "Proyecto Cerrado",
  //              LocalDate.now(),
  //              LocalDate.now().plusDays(1),
  //              "Desc",
  //              ProyectStatus.CLOSED // Estado CERRADO
  //      );
  //
  //      // Assert
  //      assertFalse(closedProyect.canAddTask());
  //  }
}
