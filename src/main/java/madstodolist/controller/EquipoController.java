package madstodolist.controller;

import madstodolist.authentication.ManagerUserSesion;
import madstodolist.authentication.UsuarioNoLogeadoException;
import madstodolist.controller.exception.EquipoNotFoundException;
import madstodolist.controller.exception.UsuarioNotFoundException;
import madstodolist.model.Equipo;
import madstodolist.model.Usuario;
import madstodolist.service.EquipoService;
import madstodolist.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class EquipoController {
    @Autowired
    UsuarioService usuarioService;

    @Autowired
    EquipoService equipoService;

    @Autowired
    ManagerUserSesion managerUserSesion;

    @GetMapping("/equipos")
    public String listadoEquipos(Model model, HttpSession session) {
        Long id = (Long) session.getAttribute("idUsuarioLogeado");
        Usuario usuario = usuarioService.findById(id);

        if(usuario !=  null){
            List<Equipo> equipos = equipoService.findAllOrderedByName();

            model.addAttribute("nombreUsuario", usuario.getNombre());
            model.addAttribute("idUsuario", usuario.getId());
            model.addAttribute("equipos", equipos);
            model.addAttribute("admin", usuario.getAdminCheck());
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

    @GetMapping("equipos/{id}/{accion}/usuario/{idUsuario}")
    public String pertenenciaEquipo(@PathVariable(value="id") Long idEquipo, @PathVariable(value="idUsuario") Long idUsuario,
                                    @PathVariable(value="accion") String accion, Model model, HttpSession session){
        Equipo equipo = equipoService.findById(idEquipo);

        if(equipo == null){
            throw new EquipoNotFoundException();
        }

        managerUserSesion.comprobarUsuarioLogeado(session, idUsuario);

        if(accion.equals("agregar")){
            equipoService.agregarUsuarioEquipo(idEquipo, idUsuario);
        }
        else if(accion.equals("eliminar")){
            equipoService.eliminarUsuarioEquipo(idEquipo, idUsuario);
        }

        return "redirect:/equipos/" + idEquipo + "/usuarios";
    }

    @GetMapping("/equipos/{id}/editar")
    public String formEditaEquipo(@PathVariable(value="id") Long idEquipo, @ModelAttribute EquipoData equipoData,
                                 Model model, HttpSession session) {

        Equipo equipo = equipoService.findById(idEquipo);
        if (equipo == null) {
            throw new EquipoNotFoundException();
        }

        Long idLog = (Long) session.getAttribute("idUsuarioLogeado");

        if(idLog == null){
            throw  new UsuarioNoLogeadoException();
        }

        Usuario usuarioLog = usuarioService.findById(idLog);

        if(usuarioLog !=  null) {
            managerUserSesion.comprobarUsuarioAdmin(usuarioLog);
            model.addAttribute("nombreUsuario", usuarioLog.getNombre());
            model.addAttribute("idUsuario", usuarioLog.getId());
            model.addAttribute("equipo", equipo);
            equipo.setNombre(equipo.getNombre());
        }
        else{
            throw new UsuarioNotFoundException();
        }

        return "formEditarEquipo";
    }

    @PostMapping("/equipos/{id}/editar")
    public String grabaEquipoModificado(@PathVariable(value="id") Long idEquipo, @ModelAttribute EquipoData equipoData,
                                       Model model, RedirectAttributes flash, HttpSession session) {
        Equipo equipo = equipoService.findById(idEquipo);
        if (equipo == null) {
            throw new EquipoNotFoundException();
        }

        Long idLog = (Long) session.getAttribute("idUsuarioLogeado");

        if(idLog == null){
            throw  new UsuarioNoLogeadoException();
        }

        Usuario usuarioLog = usuarioService.findById(idLog);
        if(usuarioLog !=  null) {
            managerUserSesion.comprobarUsuarioAdmin(usuarioLog);

            equipoService.modificaEquipo(idEquipo, equipoData.getNombre());
            flash.addFlashAttribute("mensaje", "Equipo modificado correctamente");
        }
        else{
            throw new UsuarioNotFoundException();
        }

        return "redirect:/equipos";
    }

    @DeleteMapping("/equipos/{id}")
    @ResponseBody
    public String borrarEquipo(@PathVariable(value="id") Long idEquipo, RedirectAttributes flash, HttpSession session) {
        Equipo equipo = equipoService.findById(idEquipo);
        if (equipo == null) {
            throw new EquipoNotFoundException();
        }

        Long idLog = (Long) session.getAttribute("idUsuarioLogeado");

        if(idLog == null){
            throw  new UsuarioNoLogeadoException();
        }

        Usuario usuarioLog = usuarioService.findById(idLog);
        if(usuarioLog !=  null) {
            managerUserSesion.comprobarUsuarioAdmin(usuarioLog);

            equipoService.borraEquipo(idEquipo);
        }
        else{
            throw new UsuarioNotFoundException();
        }

        return "";
    }
}
