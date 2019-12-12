package madstodolist.controller;

import madstodolist.authentication.ManagerUserSesion;
import madstodolist.authentication.UsuarioNoLogeadoException;
import madstodolist.controller.exception.UsuarioNotFoundException;
import madstodolist.model.Usuario;
import madstodolist.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class UsuariosController {

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

        managerUserSesion.comprobarIdLogNotNull(id);

        Usuario usuario = usuarioService.findById(id);

        if(usuario !=  null){
            managerUserSesion.comprobarUsuarioAdmin(usuario);

            model.addAttribute("nombreUsuario", usuario.getNombre());
            model.addAttribute("idUsuario", usuario.getId());
            model.addAttribute("usuarios", usuarioService.findAll());
        }
        else{
            throw new UsuarioNoLogeadoException();
        }

        return "usuarios";
    }

    @GetMapping("/usuarios/{id}")
    public String descripcion(@PathVariable(value="id") Long idDescrip, Model model, HttpSession session){

        Long idLog = (Long) session.getAttribute("idUsuarioLogeado");

        managerUserSesion.comprobarIdLogNotNull(idLog);

        Usuario usuarioLog = usuarioService.findById(idLog);

        if(usuarioLog !=  null){
            managerUserSesion.comprobarUsuarioAdminOLogeado(usuarioLog, idDescrip);

            Usuario usuarioDescrip = usuarioService.findById(idDescrip);

            model.addAttribute("nombreUsuario", usuarioLog.getNombre());
            model.addAttribute("idUsuario", usuarioLog.getId());
            model.addAttribute("usuarioLog",usuarioLog);
            model.addAttribute("usuario", usuarioDescrip);
        }
        else if(idLog == null){
            throw new UsuarioNoLogeadoException();
        }

        return "descripcionUsuario";
    }

    @GetMapping("/usuarios/{id}/editar")
    public String formEditaUsuario(@PathVariable(value="id") Long idUsuario, @ModelAttribute UsuarioData usuarioData,
                                   Model model, HttpSession session) {
        Long idLog = (Long) session.getAttribute("idUsuarioLogeado");

        managerUserSesion.comprobarIdLogNotNull(idLog);

        Usuario usuarioLog = usuarioService.findById(idLog);

        if(usuarioLog !=  null){
            managerUserSesion.comprobarUsuarioAdminOLogeado(usuarioLog, idUsuario);

            Usuario usuario = usuarioService.findById(idUsuario);

            model.addAttribute("nombreUsuario", usuarioLog.getNombre());
            model.addAttribute("idUsuario", usuarioLog.getId());
            model.addAttribute("usuarioLog",usuarioLog);
            model.addAttribute("usuario", usuario);
            usuarioData.setNombre(usuario.getNombre());
            usuarioData.seteMail(usuario.getEmail());
            usuarioData.setFechaNacimiento(usuario.getFechaNacimiento());
        }
        else if(idLog == null){
            throw new UsuarioNoLogeadoException();
        }

        return "formEditarUsuario";
    }

    @PostMapping("/usuarios/{id}/editar")
    public String grabarUsuarioModificado(@PathVariable(value="id") Long idUsuario, @Valid UsuarioData usuarioData,
                                          BindingResult result, Model model, RedirectAttributes flash, HttpSession session) {
        Long idLog = (Long) session.getAttribute("idUsuarioLogeado");

        managerUserSesion.comprobarIdLogNotNull(idLog);

        Usuario usuarioLog = usuarioService.findById(idLog);

        if(usuarioLog !=  null){
            managerUserSesion.comprobarUsuarioAdminOLogeado(usuarioLog, idUsuario);

            Usuario usuario = usuarioService.findById(idUsuario);

            if (result.hasErrors()) {
                return "redirect:/usuarios/" + idUsuario + "/editar";
            }

            usuarioService.modificaPerfilUsuario(idUsuario, usuarioData.getNombre(), usuarioData.geteMail(), usuarioData.getFechaNacimiento());
            flash.addFlashAttribute("mensaje", "Usuario modificado correctamente");
        }
        else if(idLog == null){
            throw new UsuarioNoLogeadoException();
        }

        return "redirect:/usuarios/" + idUsuario;
    }

    @GetMapping("/usuarios/{id}/gestion/{accion}")
    public String gestionarUsuario(@PathVariable(value="id") Long id, @PathVariable(value="accion") String accion, Model model, HttpSession session){

        Long idLog = (Long) session.getAttribute("idUsuarioLogeado");
        Usuario usuarioAdmin = usuarioService.findById(idLog);

        if(id !=  null){
            managerUserSesion.comprobarUsuarioAdmin(usuarioAdmin);

            Usuario usuario = usuarioService.findById(id);
            if (usuario == null) {
                throw new UsuarioNotFoundException();
            }

            usuarioService.modificaUsuario(id, accion);
        }
        else{
            throw new UsuarioNoLogeadoException();
        }

        return "redirect:/usuarios";
    }
}
