package madstodolist;


import madstodolist.model.Tarea;
import madstodolist.model.Usuario;
import madstodolist.service.TareaService;
import madstodolist.service.UsuarioService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
public class TareaServiceTest {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    TareaService tareaService;


    @Test
    public void testNuevaTareaUsuario() throws Exception {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        // WHEN
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Tarea tarea = tareaService.nuevaTareaUsuario(1L, "Práctica 1 de MADS", sdf.parse("2019-09-10"));

        // THEN

        Usuario usuario = usuarioService.findByEmail("ana.garcia@gmail.com");
        assertThat(usuario.getTareas()).contains(tarea);
    }

    @Test
    public void testListadoTareas() {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        Usuario usuario = new Usuario("ana.garcia@gmail.com");
        usuario.setId(1L);

        Tarea lavarCoche = new Tarea(usuario, "Lavar coche", null);
        lavarCoche.setId(1L);

        // WHEN

        List<Tarea> tareas = tareaService.allTareasUsuario(1L);

        // THEN

        assertThat(tareas).contains(lavarCoche);
    }

    @Test
    public void testBuscarTarea() {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        // WHEN

        Tarea lavarCoche = tareaService.findById(1L);

        // THEN

        assertThat(lavarCoche).isNotNull();
        assertThat(lavarCoche.getTitulo()).isEqualTo("Lavar coche");
    }

    @Test
    @Transactional
    public void testModificarTarea() throws Exception {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Tarea tarea = tareaService.nuevaTareaUsuario(1L, "Pagar el recibo", sdf.parse("2019-09-10"));
        Long idNuevaTarea = tarea.getId();
        int estadoInicial = tarea.getEstado();

        // WHEN
        //El estado no se va a modificar debido a que 0 no es un estado permitido (sólo se permiten estados 1, 2, 3)
        Tarea tareaModificada = tareaService.modificaTarea(idNuevaTarea, "Pagar la matrícula", 0, sdf.parse("2019-09-25"));
        Tarea tareaBD = tareaService.findById(idNuevaTarea);

        // THEN

        assertThat(tareaModificada.getTitulo()).isEqualTo("Pagar la matrícula");
        assertThat(tareaBD.getTitulo()).isEqualTo("Pagar la matrícula");
        assertThat(tareaBD.getEstado()).isEqualTo(estadoInicial);
        assertThat(tareaBD.getFechaLimite()).isEqualTo(sdf.parse("2019-09-25"));
    }

    @Test
    @Transactional
    public void testModificarTareaEstado() {
        Tarea tarea = tareaService.nuevaTareaUsuario(1L, "Pagar el recibo", null);
        Long idNuevaTarea = tarea.getId();
        int estadoInicial = tarea.getEstado();

        // WHEN
        //El estado no se va a modificar debido a que 0 no es un estado permitido (sólo se permiten estados 1, 2, 3)
        Tarea tareaModificada = tareaService.modificaTarea(idNuevaTarea, "Pagar la matrícula", 2, null);
        Tarea tareaBD = tareaService.findById(idNuevaTarea);

        // THEN

        assertThat(tareaModificada.getTitulo()).isEqualTo("Pagar la matrícula");
        assertThat(tareaBD.getTitulo()).isEqualTo("Pagar la matrícula");
        assertThat(tareaBD.getEstado()).isNotEqualTo(estadoInicial);
        assertThat(tareaBD.getEstado()).isEqualTo(2);
    }

    @Test
    @Transactional
    public void testModificarTareaFechaLimite() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Tarea tarea = tareaService.nuevaTareaUsuario(1L, "Pagar el recibo", sdf.parse("2019-09-10"));
        Long idNuevaTarea = tarea.getId();
        Date fechaInicial = tarea.getFechaLimite();

        // WHEN
        Tarea tareaModificada = tareaService.modificaTarea(idNuevaTarea, "Pagar la matrícula", 2, sdf.parse("2019-09-25"));
        Tarea tareaBD = tareaService.findById(idNuevaTarea);
        Date fechaLimite2 = tarea.getFechaLimite();

        // THEN

        assertThat(tareaModificada.getTitulo()).isEqualTo("Pagar la matrícula");
        assertThat(tareaBD.getTitulo()).isEqualTo("Pagar la matrícula");
        assertThat(tareaBD.getFechaLimite()).isNotEqualTo(fechaInicial);
        assertThat(tareaBD.getFechaLimite()).isEqualTo(fechaLimite2);
    }

    @Test
    @Transactional
    public void testArchivarTarea(){
        //GIVEN
        Tarea tarea = tareaService.findById(1L);
        boolean archivadaInicial = tarea.isArchivada();
        //WHEN
        tareaService.archivaTarea(1L, true);

        //THEN

        assertThat(archivadaInicial).isEqualTo(false);
        assertThat(tarea.isArchivada()).isEqualTo(true);
    }

    @Test
    public void testBorrarTarea() {
        // GIVEN

        Tarea tarea = tareaService.nuevaTareaUsuario(1L, "Estudiar MADS", null);

        // WHEN

        tareaService.borraTarea(tarea.getId());

        // THEN

        assertThat(tareaService.findById(tarea.getId())).isNull();

    }
}
