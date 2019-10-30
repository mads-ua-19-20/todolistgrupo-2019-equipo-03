package madstodolist.controller;

import madstodolist.controller.exception.UsuarioNotFoundException;
import madstodolist.model.Usuario;
import madstodolist.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class EquipoController {
    @Autowired
    UsuarioService usuarioService;

    @GetMapping("/equipos")
    public String listadoEquipos(Model model, HttpSession session) {
        Long id = (Long) session.getAttribute("idUsuarioLogeado");

        if(id !=  null){
            Usuario usuario = usuarioService.findById(id);

            model.addAttribute("nombreUsuario", usuario.getNombre());
            model.addAttribute("idUsuario", usuario.getId());
        }
        else{
            model.addAttribute("nombreUsuario", "null");
            model.addAttribute("idUsuario", "null");
        }

        return "equipos";
    }
}
