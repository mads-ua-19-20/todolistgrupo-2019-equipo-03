package madstodolist;

import madstodolist.authentication.ManagerUserSesion;
import madstodolist.controller.EquipoController;
import madstodolist.model.Equipo;
import madstodolist.model.TareaEquipo;
import madstodolist.model.Usuario;
import madstodolist.service.EquipoService;
import madstodolist.service.TareaEquipoService;
import madstodolist.service.UsuarioService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(EquipoController.class)
public class TareaEquipoWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private EquipoService equipoService;

    @MockBean
    private TareaEquipoService tareaEquipoService;

    @MockBean
    private ManagerUserSesion managerUserSesion;

    @Test
    public void nuevaTareaEquipoDevuelveForm() throws Exception {
        Equipo equipo = new Equipo("Proyecto Cobalto");
        equipo.setId(1L);


        Usuario usuario = new Usuario("domingo@ua.es");
        usuario.setId(1L);

        when(usuarioService.findById(null)).thenReturn(usuario);
        when(equipoService.findById(1L)).thenReturn(equipo);

        this.mockMvc.perform(get("/equipos/1/usuarios/tareanueva"))
                .andDo(print())
                .andExpect(content().string(containsString("action=\"/equipos/1/usuarios/tareanueva\"")))
                .andExpect(content().string(containsString("Asignaci")));
    }


    @Test
    public void nuevaTareaEquipoDevuelveNotFound() throws Exception {

        when(equipoService.findById(1L)).thenReturn(null);

        this.mockMvc.perform(get("/equipos/1/usuarios/tareanueva"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void editarTareaEquipoDevuelveForm() throws Exception {
        TareaEquipo tareaEquipo = new TareaEquipo(new Equipo("Proyecto Cobalto"), "Limpieza almacén", null, null);
        tareaEquipo.setId(1L);
        tareaEquipo.getEquipo().setId(1L);

        Usuario usuario = new Usuario("domingo@ua.es");
        usuario.setId(1L);

        when(usuarioService.findById(null)).thenReturn(usuario);
        when(tareaEquipoService.findById(1L)).thenReturn(tareaEquipo);

        this.mockMvc.perform(get("/equipos/1/usuarios/tareaEquipo/1/editar"))
                .andDo(print())
                .andExpect(content().string(allOf(
                        // Contiene la acción para enviar el post a la URL correcta
                        containsString("action=\"/equipos/1/usuarios/tareaEquipo/1/editar\""),
                        // Contiene el texto de la tarea a editar
                        containsString("Limpieza"),
                        // Contiene enlace a listar tareas del usuario si se cancela la edición
                        containsString("href=\"/equipos/1/usuarios\""),
                        containsString("Estado"),
                        containsString("Fecha límite"),
                        containsString("Asignaci"))));
    }

    @Test
    public void verUsuariosEquipo() throws Exception {
        Usuario usuario = new Usuario("domingo@ua.es");
        usuario.setId(1L);

        Equipo equipo = new Equipo("Proyecto Cobalto");
        equipo.setId(1L);

        TareaEquipo tareaEquipo = new TareaEquipo(equipo, "Limpieza almacén", usuario, null);
        tareaEquipo.setId(1L);

        List<TareaEquipo> tareasEquipo = new ArrayList<>();
        tareasEquipo.add(tareaEquipo);

        when(usuarioService.findById(null)).thenReturn(usuario);
        when(equipoService.findById(1L)).thenReturn(equipo);
        when(equipoService.tareasEquipo(1L)).thenReturn(tareasEquipo);

        this.mockMvc.perform(get("/equipos/1/usuarios"))
                .andDo(print())
                .andExpect(content().string(containsString("Equipo Proyecto Cobalto")))
                .andExpect(content().string(containsString("Limpieza")));

    }
}
