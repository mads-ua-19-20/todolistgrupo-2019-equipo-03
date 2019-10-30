package madstodolist.controller;

import madstodolist.authentication.UsuarioNoLogeadoException;
import madstodolist.controller.exception.UsuarioNotFoundException;
import madstodolist.model.Equipo;
import madstodolist.model.Usuario;
import madstodolist.service.EquipoService;
import madstodolist.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class EquipoController {
    @Autowired
    UsuarioService usuarioService;

    @Autowired
    EquipoService equipoService;

    @GetMapping("/equipos")
    public String listadoEquipos(Model model, HttpSession session) {
        Long id = (Long) session.getAttribute("idUsuarioLogeado");

        if(id !=  null){
            Usuario usuario = usuarioService.findById(id);
            List<Equipo> equipos = equipoService.findAllOrderedByName();

            model.addAttribute("nombreUsuario", usuario.getNombre());
            model.addAttribute("idUsuario", usuario.getId());
            model.addAttribute("equipos", equipos);
        }
        else{
            throw new UsuarioNoLogeadoException();
        }

        return "equipos";
    }
}
