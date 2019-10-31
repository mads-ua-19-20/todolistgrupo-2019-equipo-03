package madstodolist;

import madstodolist.controller.EquipoController;
import madstodolist.model.Equipo;
import madstodolist.model.Usuario;
import madstodolist.service.EquipoService;
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

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(EquipoController.class)
public class EquipoWebTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private EquipoService equipoService;

    @Test
    public void verListadoEquipos() throws Exception {
        Usuario usuario = new Usuario("domingo@ua.es");
        usuario.setId(1L);

        Equipo equipo1 = new Equipo("Proyecto Cobalto");
        equipo1.setId(1L);
        Equipo equipo2 = new Equipo("Proyecto Adamantium");
        equipo2.setId(2L);
        List<Equipo> equipos = new ArrayList<>();
        equipos.add(equipo1);
        equipos.add(equipo2);

        when(usuarioService.findById(null)).thenReturn(usuario);
        when(equipoService.findAllOrderedByName()).thenReturn(equipos);

        this.mockMvc.perform(get("/equipos"))
                .andDo(print())
                .andExpect(content().string(containsString("Listado de equipos")))
                .andExpect(content().string(containsString("Proyecto Cobalto")))
                .andExpect(content().string(containsString("Proyecto Adamantium")));
    }

    @Test
    public void verUsuariosEquipo() throws Exception {
        Usuario usuario = new Usuario("domingo@ua.es");
        usuario.setId(1L);

        Equipo equipo = new Equipo("Proyecto Cobalto");
        equipo.setId(1L);

        List<Usuario> usuariosEquipo = new ArrayList<>();
        usuariosEquipo.add(usuario);

        when(usuarioService.findById(null)).thenReturn(usuario);
        when(equipoService.findById(1L)).thenReturn(equipo);
        when(equipoService.usuariosEquipo(1L)).thenReturn(usuariosEquipo);

        this.mockMvc.perform(get("/equipos/1/usuarios"))
                .andDo(print())
                .andExpect(content().string(containsString("Listado de usuarios del equipo Proyecto Cobalto")))
                .andExpect(content().string(containsString("domingo@ua.es")));

    }

    @Test
    public void verEquiposNoAutorizado() throws Exception {
        this.mockMvc.perform(get("/equipos"))
                .andDo(print())
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void verUsuariosEquipoNoAutorizado() throws Exception {
        this.mockMvc.perform(get("/equipos/1/usuarios"))
                .andDo(print())
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void verUsuariosEquipoNotFound() throws Exception {
        Usuario usuario = new Usuario("domingo@ua.es");
        usuario.setId(1L);

        when(usuarioService.findById(null)).thenReturn(usuario);
        when(equipoService.findById(1L)).thenReturn(null);

        this.mockMvc.perform(get("/equipos/1/usuarios"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
