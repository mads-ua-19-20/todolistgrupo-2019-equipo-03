package madstodolist;


import madstodolist.model.Tarea;
import madstodolist.model.TareaRepository;
import madstodolist.model.Usuario;
import madstodolist.model.UsuarioRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
public class TareaTest {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    TareaRepository tareaRepository;

    //
    // Tests modelo Tarea
    //

    @Test
    public void crearTarea() throws Exception {
        // GIVEN
        Usuario usuario = new Usuario("juan.gutierrez@gmail.com");

        // WHEN
        Date fechaLimite = new Date(1575375150L);
        Tarea tarea = new Tarea(usuario, "Práctica 1 de MADS", fechaLimite);

        // THEN

        assertThat(tarea.getTitulo()).isEqualTo("Práctica 1 de MADS");
        assertThat(tarea.getUsuario()).isEqualTo(usuario);
        assertThat(tarea.getEstado()).isEqualTo(1);
        assertThat(tarea.getFechaLimite()).isEqualTo(fechaLimite);
        assertThat(tarea.isArchivada()).isEqualTo(false);
    }

    @Test
    public void comprobarIgualdadSinId() {
        // GIVEN

        Usuario usuario = new Usuario("juan.gutierrez@gmail.com");
        Tarea tarea1 = new Tarea(usuario, "Práctica 1 de MADS", null);
        Tarea tarea2 = new Tarea(usuario, "Práctica 1 de MADS", null);
        Tarea tarea3 = new Tarea(usuario, "Pagar el alquiler", null);

        // THEN

        assertThat(tarea1).isEqualTo(tarea2);
        assertThat(tarea1).isNotEqualTo(tarea3);
    }

    //
    // Tests TareaRepository
    //

    @Test
    @Transactional
    public void crearTareaEnBaseDatos() {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        Usuario usuario = usuarioRepository.findById(1L).orElse(null);
        Date fechaLimite = new Date(1575375150L);
        Tarea tarea = new Tarea(usuario, "Práctica 1 de MADS", fechaLimite);

        // WHEN

        tareaRepository.save(tarea);

        // THEN

        assertThat(tarea.getId()).isNotNull();
        assertThat(tarea.getUsuario()).isEqualTo(usuario);
        assertThat(tarea.getTitulo()).isEqualTo("Práctica 1 de MADS");
        assertThat(tarea.getFechaLimite()).isEqualTo(fechaLimite);
    }

    @Test(expected = Exception.class)
    @Transactional
    public void salvarTareaEnBaseDatosConUsuarioNoBDLanzaExcepcion() {
        // GIVEN
        // Creamos un usuario sin ID y, por tanto, sin estar en gestionado
        // por JPA
        Usuario usuario = new Usuario("juan.gutierrez@gmail.com");
        Tarea tarea = new Tarea(usuario, "Práctica 1 de MADS", null);

        // WHEN

        tareaRepository.save(tarea);

        // THEN
        // Se lanza una excepción (capturada en el test)
    }

    @Test
    @Transactional(readOnly = true)
    public void unUsuarioTieneUnaListaDeTareas() {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        Usuario usuario = usuarioRepository.findById(1L).orElse(null);

        // WHEN
        Set<Tarea> tareas = usuario.getTareas();

        // THEN

        assertThat(tareas).isNotEmpty();
    }

    @Test
    @Transactional
    public void unaTareaNuevaSeAñadeALaListaDeTareas() {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        Usuario usuario = usuarioRepository.findById(1L).orElse(null);

        // WHEN

        Set<Tarea> tareas = usuario.getTareas();
        Tarea tarea = new Tarea(usuario, "Práctica 1 de MADS", null);
        tareaRepository.save(tarea);

        // THEN

        assertThat(usuario.getTareas()).contains(tarea);
        assertThat(tareas).isEqualTo(usuario.getTareas());
        assertThat(usuario.getTareas()).contains(tarea);
    }
}
