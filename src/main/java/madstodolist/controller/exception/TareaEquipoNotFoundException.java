package madstodolist.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason="Tarea del equipo no encontrada")
public class TareaEquipoNotFoundException extends RuntimeException {
}