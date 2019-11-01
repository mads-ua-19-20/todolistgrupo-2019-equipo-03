package madstodolist.controller;

import madstodolist.authentication.UsuarioNoLogeadoException;
import madstodolist.controller.exception.EquipoNotFoundException;
import madstodolist.model.Equipo;
import madstodolist.model.Usuario;
import madstodolist.service.EquipoService;
import madstodolist.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        Usuario usuario = usuarioService.findById(id);

        if(usuario !=  null){
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

    @GetMapping("equipos/{id}/usuarios")
    public String getUsuariosEquipo(@PathVariable(value="id") Long idEquipo, Model model, HttpSession session){
        Long id = (Long) session.getAttribute("idUsuarioLogeado");
        Usuario usuario = usuarioService.findById(id);

        if(usuario !=  null){
            Equipo equipo = equipoService.findById(idEquipo);
            if(equipo == null){
                throw new EquipoNotFoundException();
            }
            List<Usuario> usuariosEquipo = equipoService.usuariosEquipo(idEquipo);
            boolean apuntado = usuariosEquipo.contains(usuario);
            model.addAttribute("nombreUsuario", usuario.getNombre());
            model.addAttribute("idUsuario", usuario.getId());
            model.addAttribute("usuariosEquipo", usuariosEquipo);
            model.addAttribute("nombreEquipo", equipo.getNombre());
            model.addAttribute("idEquipo", equipo.getId());
            model.addAttribute("apuntado", apuntado);
        }
        else{
            throw new UsuarioNoLogeadoException();
        }

        return "usuariosEquipo";
    }

    @GetMapping("/equipos/nuevo")
    public String formNuevoEquipo(@ModelAttribute EquipoData equipoData, Model model,
                                  HttpSession session) {
        Long id = (Long) session.getAttribute("idUsuarioLogeado");
        Usuario usuario = usuarioService.findById(id);

        if(usuario != null){
            model.addAttribute("nombreUsuario", usuario.getNombre());
            model.addAttribute("idUsuario", usuario.getId());
        }
        else{
            throw new UsuarioNoLogeadoException();
        }
        return "formNuevoEquipo";
    }

    @PostMapping("/equipos/nuevo")
    public String nuevoEquipo(@ModelAttribute EquipoData equipoData,
                             Model model, RedirectAttributes flash,
                             HttpSession session) {
        Long id = (Long) session.getAttribute("idUsuarioLogeado");
        Usuario usuario = usuarioService.findById(id);

        if(usuario != null){
            equipoService.nuevoEquipo(equipoData.getNombre());
            flash.addFlashAttribute("mensaje", "Equipo creado correctamente");
        }
        else{
            throw new UsuarioNoLogeadoException();
        }
        return "redirect:/equipos";
    }


}
