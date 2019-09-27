package madstodolist;

import madstodolist.controller.HomeController;
import madstodolist.model.Usuario;
import madstodolist.service.UsuarioService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringRunner.class)
@WebMvcTest(HomeController.class)
public class DescripcionWebTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Test
    public void getUsuariosBotonDescripcion() throws Exception {
        Usuario usuario = new Usuario("domingo@ua.es");
        usuario.setId(1L);

        List<Usuario> lista = new ArrayList();
        lista.add(usuario);

        when(usuarioService.findAll()).thenReturn(lista);

        this.mockMvc.perform(get("/usuarios/"))
                .andDo(print())
                .andExpect(content().string(containsString("Descripción")));
    }

    @Test
    public void getDescripcionDevuelveDatos() throws Exception {
        Usuario usuario = new Usuario("domingo@ua.es");
        usuario.setId(1L);
        usuario.setNombre("Domingo Gallardo");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        usuario.setFechaNacimiento(sdf.parse("2001-02-10"));

        when(usuarioService.findById(1L)).thenReturn(usuario);

        this.mockMvc.perform(get("/usuarios/1"))
                .andDo(print())
                .andExpect(content().string(containsString("Descripción de Domingo Gallardo")))
                .andExpect(content().string(containsString("ID: 1")))
                .andExpect(content().string(containsString("Nombre: Domingo Gallardo")))
                .andExpect(content().string(containsString("E-mail: domingo@ua")))
                .andExpect(content().string(containsString("Fecha nacimiento: " + sdf.parse("2001-02-10"))));
    }
}
