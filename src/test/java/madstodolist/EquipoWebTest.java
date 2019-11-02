package madstodolist;

import madstodolist.authentication.ManagerUserSesion;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(EquipoController.class)
public class EquipoWebTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private EquipoService equipoService;

    @MockBean
    private ManagerUserSesion managerUserSesion;

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
                .andExpect(status().isNotFound());

    }

    @Test
    public void verUsuariosEquipoNoAutorizado() throws Exception {
        this.mockMvc.perform(get("/equipos/1/usuarios"))
                .andDo(print())
                .andExpect(status().isNotFound());

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

    @Test
    public void botonNuevoEquipo() throws  Exception {
        Usuario usuario = new Usuario("domingo@ua.es");
        usuario.setId(1L);

        when(usuarioService.findById(null)).thenReturn(usuario);

        this.mockMvc.perform(get("/equipos"))
                .andDo(print())
                .andExpect(content().string(containsString("href=\"/equipos/nuevo\"")));
    }

    @Test
    public void formCrearEquipo() throws Exception {
        Usuario usuario = new Usuario("domingo@ua.es");
        usuario.setId(1L);

        when(usuarioService.findById(null)).thenReturn(usuario);

        this.mockMvc.perform(get("/equipos/nuevo"))
                .andDo(print())
                .andExpect(content().string(containsString("Nombre del equipo:")))
                .andExpect(content().string(containsString("Crear equipo")));
    }

    @Test
    public void crearEquipoRedirect() throws  Exception {
        Usuario usuario = new Usuario("domingo@ua.es");
        usuario.setId(1L);

        when(usuarioService.findById(null)).thenReturn(usuario);

        this.mockMvc.perform(post("/equipos/nuevo")
                .param("nombre", "Equipo prueba"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/equipos"));
    }

    @Test
    public void eliminarseUsuarioEquipo() throws Exception {
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
                .andExpect(content().string(containsString("href=\"/equipos/1/eliminar/usuario/1")));

    }

    @Test
    public void añadirseUsuarioEquipo() throws Exception {
        Usuario usuario = new Usuario("domingo@ua.es");
        usuario.setId(1L);

        Equipo equipo = new Equipo("Proyecto Cobalto");
        equipo.setId(1L);

        List<Usuario> usuariosEquipo = new ArrayList<>();

        when(usuarioService.findById(null)).thenReturn(usuario);
        when(equipoService.findById(1L)).thenReturn(equipo);
        when(equipoService.usuariosEquipo(1L)).thenReturn(usuariosEquipo);

        this.mockMvc.perform(get("/equipos/1/usuarios"))
                .andDo(print())
                .andExpect(content().string(containsString("href=\"/equipos/1/agregar/usuario/1")));
    }

    @Test
    public void botonesEditarEliminarEquipo() throws Exception {
        Usuario usuario = new Usuario("antonio@gmail.com");
        usuario.setAdminCheck(true);
        Equipo equipo = new Equipo("Equipo prueba");
        List<Equipo> equipos = new ArrayList<>();
        equipos.add(equipo);

        when(usuarioService.findById(null)).thenReturn(usuario);
        when(equipoService.findAllOrderedByName()).thenReturn(equipos);

        this.mockMvc.perform(get("/equipos"))
                .andDo(print())
                .andExpect(content().string(containsString("Editar")))
                .andExpect(content().string(containsString("Borrar")));
    }

    @Test
    public void editarEquipo() throws Exception {
        Usuario usuario = new Usuario("antonio@gmail.com");
        usuario.setAdminCheck(true);
        Equipo equipo = new Equipo("Equipo prueba");
        equipo.setId(1L);

        when(usuarioService.findById(null)).thenReturn(usuario);
        when(equipoService.findById(1L)).thenReturn(equipo);

        this.mockMvc.perform((get("/equipos/1/editar")))
                .andDo(print())
                .andExpect(content().string(containsString("<label for=\"nombre\">Nombre del equipo:</label>")));
    }

    @Test
    public void editarEquipoNoLogeado() throws Exception {
        this.mockMvc.perform((get("/equipos/1/editar")))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void editarEquipoPOSTRedirect() throws Exception {
        Usuario usuario = new Usuario("antonio@gmail.com");
        Equipo equipo = new Equipo("Equipo sin modificar");

        when(usuarioService.findById(null)).thenReturn(usuario);
        when(equipoService.findById(1L)).thenReturn(equipo);

        this.mockMvc.perform(post("/equipos/1/editar")
                .param("nombre", "Equipo Modificado"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/equipos"));
    }

    @Test
    public void editarEquipoPOSTNoLogeado() throws Exception {
        this.mockMvc.perform((post("/equipos/1/editar")))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void borrarEquipoRedirect() throws  Exception {
        Usuario usuario = new Usuario("antonio@gmail.com");
        Equipo equipo = new Equipo("Equipo sin modificar");

        when(usuarioService.findById(null)).thenReturn(usuario);
        when(equipoService.findById(1L)).thenReturn(equipo);

        this.mockMvc.perform(delete("/equipos/1"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void eliminarEquipoNoLogeado() throws Exception {
        this.mockMvc.perform((delete("/equipos/1")))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
