package madstodolist.authentication;

import madstodolist.model.Usuario;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

@Component
public class ManagerUserSesion {

    // Añadimos el id de usuario en la sesión HTTP para hacer
    // una autorización sencilla. En los métodos de controllers
    // comprobamos si el id del usuario logeado coincide con el obtenido
    // desde la URL
    public void logearUsuario(HttpSession session, Long idUsuario) {
        session.setAttribute("idUsuarioLogeado", idUsuario);
    }

    public void comprobarUsuarioLogeado(HttpSession session, Long idUsuario) {
        Long idUsuarioLogeado = (Long) session.getAttribute("idUsuarioLogeado");
        if (!idUsuario.equals(idUsuarioLogeado))
            throw new UsuarioNoLogeadoException();
    }

    public void comprobarUsuarioAdmin(Usuario usuario){
        if(!usuario.getAdminCheck()){
            throw  new NoAdminAccessException();
        }
    }

    public void comprobarIdLogNotNull(Long id){
        if(id == null){
            throw new UsuarioNoLogeadoException();
        }
    }

    public void comprobarUsuarioAdminOLogeado(Usuario usuario, Long idUsuario) {
        if(!usuario.getAdminCheck() && !idUsuario.equals(usuario.getId())){
            throw  new NoAdminAccessException();
        }
    }
}
