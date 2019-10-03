package madstodolist.authentication;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason="Acceso sólo permitido a administradores")
public class NoAdminAccessException extends RuntimeException {
}
