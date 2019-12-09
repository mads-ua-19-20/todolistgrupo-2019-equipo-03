package madstodolist.controller;

import madstodolist.authentication.ManagerUserSesion;
import madstodolist.authentication.UsuarioNoLogeadoException;
import madstodolist.controller.exception.EquipoNotFoundException;
import madstodolist.controller.exception.TareaEquipoNotFoundException;
import madstodolist.controller.exception.UsuarioNotFoundException;
import madstodolist.model.Equipo;
import madstodolist.model.TareaEquipo;
import madstodolist.model.Usuario;
import madstodolist.service.EquipoService;
import madstodolist.service.TareaEquipoService;
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
    TareaEquipoService tareaEquipoService;

    @Autowired
    ManagerUserSesion managerUserSesion;

    @GetMapping("/equipos")
    public String listadoEquipos(Model model, HttpSession session) {
        Long id = (Long) session.getAttribute("idUsuarioLogeado");

        managerUserSesion.comprobarIdLogNotNull(id);

        Usuario usuario = usuarioService.findById(id);

        if(usuario !=  null){
            List<Equipo> equipos = equipoService.findAllOrderedByName();

            model.addAttribute("nombreUsuario", usuario.getNombre());
            model.addAttribute("idUsuario", usuario.getId());
            model.addAttribute("equipos", equipos);
            model.addAttribute("admin", usuario.getAdminCheck());
        }
        else{
            throw new UsuarioNotFoundException();
        }

        return "equipos";
    }

    @GetMapping("equipos/{id}/usuarios")
    public String getUsuariosEquipo(@PathVariable(value="id") Long idEquipo, Model model, HttpSession session){
        Long id = (Long) session.getAttribute("idUsuarioLogeado");

        managerUserSesion.comprobarIdLogNotNull(id);

        Usuario usuario = usuarioService.findById(id);

        if(usuario !=  null){
            Equipo equipo = equipoService.findById(idEquipo);
            if(equipo == null){
                throw new EquipoNotFoundException();
            }

            boolean bloqueado = equipoService.usuarioBloqueado(idEquipo, id);
            List<Usuario> usuariosEquipo = equipoService.usuariosEquipo(idEquipo);
            boolean apuntado = usuariosEquipo.contains(usuario);

            List<TareaEquipo> tareasEquipo = equipoService.tareasEquipo(idEquipo);
            model.addAttribute("nombreUsuario", usuario.getNombre());
            model.addAttribute("idUsuario", usuario.getId());
            model.addAttribute("usuariosEquipo", usuariosEquipo);
            model.addAttribute("nombreEquipo", equipo.getNombre());
            model.addAttribute("idEquipo", equipo.getId());
            model.addAttribute("apuntado", apuntado);
            model.addAttribute("tareasEquipo", tareasEquipo);
            model.addAttribute("usuBloqueado", bloqueado);
        }
        else{
            throw new UsuarioNotFoundException();
        }

        return "usuariosEquipo";
    }

    @GetMapping("/equipos/nuevo")
    public String formNuevoEquipo(@ModelAttribute EquipoData equipoData, Model model,
                                  HttpSession session) {
        Long id = (Long) session.getAttribute("idUsuarioLogeado");

        managerUserSesion.comprobarIdLogNotNull(id);

        Usuario usuario = usuarioService.findById(id);

        if(usuario != null){
            model.addAttribute("nombreUsuario", usuario.getNombre());
            model.addAttribute("idUsuario", usuario.getId());
        }
        else{
            throw new UsuarioNotFoundException();
        }
        return "formNuevoEquipo";
    }

    @PostMapping("/equipos/nuevo")
    public String nuevoEquipo(@ModelAttribute EquipoData equipoData,
                             Model model, RedirectAttributes flash,
                             HttpSession session) {
        Long id = (Long) session.getAttribute("idUsuarioLogeado");

        managerUserSesion.comprobarIdLogNotNull(id);

        Usuario usuario = usuarioService.findById(id);

        if(usuario != null){
            equipoService.nuevoEquipo(equipoData.getNombre());
            flash.addFlashAttribute("mensaje", "Equipo creado correctamente");
        }
        else{
            throw new UsuarioNotFoundException();
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

        managerUserSesion.comprobarIdLogNotNull(idLog);

        Usuario usuarioLog = usuarioService.findById(idLog);

        if(usuarioLog !=  null) {
            managerUserSesion.comprobarUsuarioAdmin(usuarioLog);
            model.addAttribute("nombreUsuario", usuarioLog.getNombre());
            model.addAttribute("idUsuario", usuarioLog.getId());
            model.addAttribute("equipo", equipo);
            equipoData.setNombre(equipo.getNombre());
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

        managerUserSesion.comprobarIdLogNotNull(idLog);

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

        managerUserSesion.comprobarIdLogNotNull(idLog);

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

    @GetMapping("/equipos/{id}/usuarios/tareanueva")
    public String formNuevaTarea(@PathVariable(value="id") Long idEquipo,
                                 @ModelAttribute TareaEquipoData tareaEquipoData, Model model,
                                 HttpSession session) {

        Long idLog = (Long) session.getAttribute("idUsuarioLogeado");
        managerUserSesion.comprobarUsuarioLogeado(session, idLog);
        tareaEquipoService.usuarioPerteneceEquipo(idLog, idEquipo);

        Usuario usuario = usuarioService.findById(idLog);
        if (usuario == null) {
            throw new UsuarioNotFoundException();
        }

        Equipo equipo = equipoService.findById(idEquipo);

        model.addAttribute("usuario", usuario);
        model.addAttribute("equipo", equipo);
        return "formNuevaTareaEquipo";
    }


    @PostMapping("/equipos/{id}/usuarios/tareanueva")
    public String nuevaTarea(@PathVariable(value="id") Long idEquipo, @ModelAttribute TareaEquipoData tareaEquipoData,
                             Model model, RedirectAttributes flash,
                             HttpSession session) {

        Long idLog = (Long) session.getAttribute("idUsuarioLogeado");
        managerUserSesion.comprobarUsuarioLogeado(session, idLog);
        tareaEquipoService.usuarioPerteneceEquipo(idLog, idEquipo);

        Usuario usuario = usuarioService.findById(idLog);
        if (usuario == null) {
            throw new UsuarioNotFoundException();
        }

        tareaEquipoService.nuevaTareaEquipo(idEquipo, tareaEquipoData.getTitulo(), tareaEquipoData.getAsignacion());
        flash.addFlashAttribute("mensaje", "Tarea creada correctamente");
        return "redirect:/equipos/" + idEquipo + "/usuarios";
    }

    @DeleteMapping("/tareasEquipo/{id}")
    @ResponseBody
    public String borrarTareaEquipo(@PathVariable(value="id") Long idTareaEquipo, RedirectAttributes flash, HttpSession session) {
        TareaEquipo tareaEquipo = tareaEquipoService.findById(idTareaEquipo);
        if (tareaEquipo == null) {
            throw new TareaEquipoNotFoundException();
        }

        Long idLog = (Long) session.getAttribute("idUsuarioLogeado");
        managerUserSesion.comprobarUsuarioLogeado(session, idLog);

        //tareaEquipoService.usuarioPerteneceEquipo(idLog, idEquipo);

        tareaEquipoService.borraTareaEquipo(idTareaEquipo);
        flash.addFlashAttribute("mensaje", "Tarea borrada correctamente");
        return "";
    }

    @GetMapping("/equipos/{idEquipo}/usuarios/tareaEquipo/{idTareaEquipo}/editar")
    public String formEditaTareaEquipo(@PathVariable(value="idEquipo") Long idEquipo, @PathVariable(value="idTareaEquipo") Long idTareaEquipo, @ModelAttribute TareaEquipoData tareaEquipoData,
                                 Model model, HttpSession session) {

        TareaEquipo tareaEquipo = tareaEquipoService.findById(idTareaEquipo);
        if (tareaEquipo == null) {
            throw new TareaEquipoNotFoundException();
        }

        Long idLog = (Long) session.getAttribute("idUsuarioLogeado");
        managerUserSesion.comprobarUsuarioLogeado(session,idLog);
        tareaEquipoService.usuarioPerteneceEquipo(idLog, idEquipo);

        Usuario usuario = usuarioService.findById(idLog);
        if (usuario == null) {
            throw new UsuarioNotFoundException();
        }

        model.addAttribute("tareaEquipo", tareaEquipo);
        model.addAttribute("usuario", usuario);
        model.addAttribute("idEquipo", idEquipo);
        tareaEquipoData.setTitulo(tareaEquipo.getTitulo());
        return "formEditarTareaEquipo";
    }

    @PostMapping("/equipos/{idEquipo}/usuarios/tareaEquipo/{idTareaEquipo}/editar")
    public String grabaTareaEquipoModificada(@PathVariable(value="idEquipo") Long idEquipo, @PathVariable(value="idTareaEquipo") Long idTareaEquipo, @ModelAttribute TareaEquipoData tareaEquipoData,
                                       Model model, RedirectAttributes flash, HttpSession session) {
        TareaEquipo tareaEquipo = tareaEquipoService.findById(idTareaEquipo);
        if (tareaEquipo == null) {
            throw new TareaEquipoNotFoundException();
        }

        Long idLog = (Long) session.getAttribute("idUsuarioLogeado");
        managerUserSesion.comprobarUsuarioLogeado(session, idLog);
        tareaEquipoService.usuarioPerteneceEquipo(idLog, idEquipo);

        tareaEquipoService.modificaTareaEquipo(idTareaEquipo, tareaEquipoData.getTitulo(), tareaEquipoData.getEstado());
        flash.addFlashAttribute("mensaje", "Tarea de equipo modificada correctamente");
        return "redirect:/equipos/" + idEquipo + "/usuarios";
    }

    @PostMapping("/equipos/{idEquipo}/tareas/archivar/{idTarea}")
    @ResponseBody
    public String archivarTarea(@PathVariable(value="idEquipo") Long idEquipo, @PathVariable(value="idTarea") Long idTarea, RedirectAttributes flash, HttpSession session){
        TareaEquipo tareaEquipo = tareaEquipoService.findById(idTarea);
        if (tareaEquipo == null) {
            throw new TareaEquipoNotFoundException();
        }
        Long idLog = (Long) session.getAttribute("idUsuarioLogeado");
        if(idLog == null){
            throw new UsuarioNoLogeadoException();
        }

        tareaEquipoService.usuarioPerteneceEquipo(idLog, idEquipo);

        tareaEquipoService.archivaTarea(idTarea, true);

        flash.addFlashAttribute("mensaje", "Tarea archivada correctamente");

        return "";
    }

    @GetMapping("equipos/{idEquipo}/usuarios/{idUsuario}/{accion}")
    public String bloquearUsuarioEquipo(@PathVariable(value="accion") String accion, @PathVariable(value="idEquipo") Long idEquipo, @PathVariable(value="idUsuario") Long idUsuario, HttpSession session, Model model){
        Long idLog = (Long) session.getAttribute("idUsuarioLogeado");

        if(idLog !=  null){
            tareaEquipoService.usuarioPerteneceEquipo(idLog ,idEquipo);

            Usuario usuario = usuarioService.findById(idLog);
            if (usuario == null) {
                throw new UsuarioNotFoundException();
            }
            if(accion.equals("bloquear")){
                equipoService.bloquearUsuario(idEquipo, idUsuario, idLog, true);
            } else if(accion.equals("desbloquear")){
                equipoService.bloquearUsuario(idEquipo, idUsuario, idLog, false);
            }
        }
        else{
            throw new UsuarioNoLogeadoException();
        }

        return "redirect:/equipos/" + idEquipo + "/usuarios";
    }

    @GetMapping("equipos/{id}/usuarios/bloqueados")
    public String getUsuariosBloqueadosEquipo(@PathVariable(value="id") Long idEquipo, Model model, HttpSession session){
        Long id = (Long) session.getAttribute("idUsuarioLogeado");

        managerUserSesion.comprobarIdLogNotNull(id);

        Usuario usuario = usuarioService.findById(id);

        if(usuario !=  null){
            Equipo equipo = equipoService.findById(idEquipo);
            if(equipo == null){
                throw new EquipoNotFoundException();
            }

            tareaEquipoService.usuarioPerteneceEquipo(id, idEquipo);
            List<Usuario> usuariosBloqueados = equipoService.usuariosBloqueadosEquipo(idEquipo);

            model.addAttribute("nombreUsuario", usuario.getNombre());
            model.addAttribute("idUsuario", usuario.getId());
            model.addAttribute("usuariosBloqueados", usuariosBloqueados);
            model.addAttribute("nombreEquipo", equipo.getNombre());
            model.addAttribute("idEquipo", equipo.getId());
        }
        else{
            throw new UsuarioNotFoundException();
        }

        return "usuariosBloqueadosEquipo";
    }
}
