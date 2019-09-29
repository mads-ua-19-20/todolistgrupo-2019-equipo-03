package madstodolist.controller;

import madstodolist.authentication.ManagerUserSesion;
import madstodolist.authentication.UsuarioNoLogeadoException;
import madstodolist.controller.exception.UsuarioNotFoundException;
import madstodolist.model.Usuario;
import madstodolist.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

@Controller
public class HomeController {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    ManagerUserSesion managerUserSesion;

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/about")
    public String loginForm(Model model, HttpSession session) {

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

        return "about";
    }

    @GetMapping("/usuarios")
    public String usuarios(Model model, HttpSession session){

        Long id = (Long) session.getAttribute("idUsuarioLogeado");

        if(id !=  null){
            Usuario usuario = usuarioService.findById(id);

            managerUserSesion.comprobarUsuarioAdmin(usuario);

            model.addAttribute("nombreUsuario", usuario.getNombre());
            model.addAttribute("idUsuario", usuario.getId());
        }
        else{
            throw new UsuarioNoLogeadoException();
        }

        model.addAttribute("usuarios", usuarioService.findAll());

        return "usuarios";
    }

    @GetMapping("/usuarios/{id}")
    public String descripcion(@PathVariable(value="id") Long idDescrip, Model model, HttpSession session){

        Long idLog = (Long) session.getAttribute("idUsuarioLogeado");

        if(idLog !=  null){
            Usuario usuarioLog = usuarioService.findById(idLog);

            managerUserSesion.comprobarUsuarioAdmin(usuarioLog);

            Usuario usuarioDescrip = usuarioService.findById(idDescrip);

            model.addAttribute("nombreUsuario", usuarioLog.getNombre());
            model.addAttribute("idUsuario", usuarioLog.getId());
            model.addAttribute("usuario", usuarioDescrip);
        }
        else if(idLog == null){
            throw new UsuarioNoLogeadoException();
        }

        return "descripcionUsuario";
    }
}
