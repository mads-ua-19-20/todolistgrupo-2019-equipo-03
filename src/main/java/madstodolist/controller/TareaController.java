package madstodolist.controller;

import madstodolist.authentication.ManagerUserSesion;
import madstodolist.controller.exception.TareaNotFoundException;
import madstodolist.controller.exception.UsuarioNotFoundException;
import madstodolist.model.Tarea;
import madstodolist.model.Usuario;
import madstodolist.service.TareaService;
import madstodolist.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Controller
public class TareaController {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    TareaService tareaService;

    @Autowired
    ManagerUserSesion managerUserSesion;


    @GetMapping("/usuarios/{id}/tareas/nueva")
    public String formNuevaTarea(@PathVariable(value="id") Long idUsuario,
                                 @ModelAttribute TareaData tareaData, Model model,
                                 HttpSession session) {

        managerUserSesion.comprobarUsuarioLogeado(session, idUsuario);

        Usuario usuario = usuarioService.findById(idUsuario);
        if (usuario == null) {
            throw new UsuarioNotFoundException();
        }
        model.addAttribute("usuario", usuario);
        return "formNuevaTarea";
    }

    @PostMapping("/usuarios/{id}/tareas/nueva")
    public String nuevaTarea(@PathVariable(value="id") Long idUsuario, @Valid TareaData tareaData,
                             BindingResult result, Model model, RedirectAttributes flash,
                             HttpSession session) {

        managerUserSesion.comprobarUsuarioLogeado(session, idUsuario);

        Usuario usuario = usuarioService.findById(idUsuario);
        if (usuario == null) {
            throw new UsuarioNotFoundException();
        }

        if (result.hasErrors()) {
            return "redirect:/usuarios/" + idUsuario + "/tareas/nueva";
        }

        tareaService.nuevaTareaUsuario(idUsuario, tareaData.getTitulo(), tareaData.getFechalimite());
        flash.addFlashAttribute("mensaje", "Tarea creada correctamente");
        return "redirect:/usuarios/" + idUsuario + "/tareas";
    }

    @GetMapping("/usuarios/{id}/tareas")
    public String listadoTareas(@PathVariable(value="id") Long idUsuario, Model model, HttpSession session) {

        managerUserSesion.comprobarUsuarioLogeado(session, idUsuario);

        Usuario usuario = usuarioService.findById(idUsuario);
        if (usuario == null) {
            throw new UsuarioNotFoundException();
        }
        List<Tarea> tareas = tareaService.allTareasUsuario(idUsuario);

        LocalDate date = LocalDate.now();
        Date fecha = Date.valueOf(date);
        model.addAttribute("fecha", fecha);
        model.addAttribute("usuario", usuario);
        model.addAttribute("tareas", tareas);
        model.addAttribute("tareasequipo", usuario.getTareasEquipoAsignadas());
        return "listaTareas";
    }

    @GetMapping("/tareas/{id}/editar")
    public String formEditaTarea(@PathVariable(value="id") Long idTarea, @ModelAttribute TareaData tareaData,
                                 Model model, HttpSession session) {

        Tarea tarea = tareaService.findById(idTarea);
        if (tarea == null) {
            throw new TareaNotFoundException();
        }

        managerUserSesion.comprobarUsuarioLogeado(session, tarea.getUsuario().getId());

        model.addAttribute("tarea", tarea);
        tareaData.setTitulo(tarea.getTitulo());
        tareaData.setFechalimite(tarea.getFechaLimite());
        return "formEditarTarea";
    }

    @PostMapping("/tareas/{id}/editar")
    public String grabaTareaModificada(@PathVariable(value="id") Long idTarea, @Valid TareaData tareaData,
                                       BindingResult result, Model model, RedirectAttributes flash, HttpSession session) {
        Tarea tarea = tareaService.findById(idTarea);
        if (tarea == null) {
            throw new TareaNotFoundException();
        }

        managerUserSesion.comprobarUsuarioLogeado(session, tarea.getUsuario().getId());

        if (result.hasErrors()) {
            return "redirect:/tareas/" + idTarea + "/editar";
        }

        tareaService.modificaTarea(idTarea, tareaData.getTitulo(), tareaData.getEstado(), tareaData.getFechalimite());
        flash.addFlashAttribute("mensaje", "Tarea modificada correctamente");
        return "redirect:/usuarios/" + tarea.getUsuario().getId() + "/tareas";
    }

    @PostMapping("tareas/archivar/{id}")
    @ResponseBody
    public String archivarTarea(@PathVariable(value="id") Long idTarea, RedirectAttributes flash, HttpSession session){
        Tarea tarea = tareaService.findById(idTarea);
        if (tarea == null) {
            throw new TareaNotFoundException();
        }

        managerUserSesion.comprobarUsuarioLogeado(session, tarea.getUsuario().getId());

        tareaService.archivaTarea(idTarea, true);
        flash.addFlashAttribute("mensaje", "Tarea archivada correctamente");

        return "";
    }

    @DeleteMapping("/tareas/{id}")
    @ResponseBody
    public String borrarTarea(@PathVariable(value="id") Long idTarea, RedirectAttributes flash, HttpSession session) {
        Tarea tarea = tareaService.findById(idTarea);
        if (tarea == null) {
            throw new TareaNotFoundException();
        }

        managerUserSesion.comprobarUsuarioLogeado(session, tarea.getUsuario().getId());

        tareaService.borraTarea(idTarea);

        return "";
    }
}

