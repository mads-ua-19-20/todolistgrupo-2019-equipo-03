package madstodolist;

import madstodolist.model.Equipo;
import madstodolist.model.TareaEquipo;
import madstodolist.model.Usuario;
import madstodolist.service.EquipoService;
import madstodolist.service.EquipoServiceException;
import madstodolist.service.TareaEquipoService;
import madstodolist.service.UsuarioService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TareaEquipoServiceTest {

    @Autowired
    EquipoService equipoService;

    @Autowired
    TareaEquipoService tareaEquipoService;

    @Autowired
    UsuarioService usuarioService;

    @Test
    public void testNuevaTareaEquipo() {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        Usuario usuario = usuarioService.findById(1L);

        // WHEN
        TareaEquipo tareaEquipo = tareaEquipoService.nuevaTareaEquipo(1L, "Cambiar ruedas bicicleta", usuario, null);

        // THEN

        Equipo equipo = equipoService.findById(1L);
        assertThat(equipo.getTareasEquipo()).contains(tareaEquipo);
        assertThat(tareaEquipo.getUsuario()).isEqualTo(usuario);
    }

    @Test
    public void testListadoTareasEquipo() {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        Equipo equipo = new Equipo("Proyecto Zinc");
        equipo.setId(1L);

        TareaEquipo tareaEquipo = new TareaEquipo(equipo, "Limpieza almacén", null, null);
        tareaEquipo.setId(1L);

        // WHEN

        List<TareaEquipo> tareasEquipo = equipoService.tareasEquipo(1L);

        // THEN

        assertThat(tareasEquipo).contains(tareaEquipo);
    }

    @Test
    public void testBuscarTareaEquipo() {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        // WHEN

        //Tarea Limpieza almacén
        TareaEquipo tareaEquipo = tareaEquipoService.findById(1L);

        // THEN

        assertThat(tareaEquipo).isNotNull();
        assertThat(tareaEquipo.getTitulo()).isEqualTo("Limpieza almacén");
    }

    @Test
    @Transactional
    public void testModificarTareaEquipo() throws Exception {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        TareaEquipo tareaEquipo = tareaEquipoService.nuevaTareaEquipo(1L, "Cambiar ruedas bicicleta", null, sdf.parse("2019-09-10"));
        Usuario usuario = usuarioService.findById(1L);
        Long idNuevaTareaEquipo = tareaEquipo.getId();
        int estadoInicial = tareaEquipo.getEstado();

        // WHEN
        //El estado no se va a modificar debido a que 0 no es un estado permitido (sólo se permiten estados 1, 2, 3)
        TareaEquipo tareaEquipoModificada = tareaEquipoService.modificaTareaEquipo(idNuevaTareaEquipo, "Engrasar cadena", 1, usuario, sdf.parse("2019-09-25"));
        TareaEquipo tareaEquipoBD = tareaEquipoService.findById(idNuevaTareaEquipo);

        // THEN

        assertThat(tareaEquipoModificada.getTitulo()).isEqualTo("Engrasar cadena");
        assertThat(tareaEquipoBD.getTitulo()).isEqualTo("Engrasar cadena");
        assertThat(tareaEquipoBD.getEstado()).isEqualTo(estadoInicial);
        assertThat(tareaEquipoBD.getUsuario()).isEqualTo(usuario);
        assertThat(tareaEquipoBD.getFechalimite()).isEqualTo((sdf.parse("2019-09-25")));
    }

    @Test
    @Transactional
    public void testModificarTareaEquipoEstado() {
        TareaEquipo tareaEquipo = tareaEquipoService.nuevaTareaEquipo(1L, "Alquilar casa", null, null);
        Long idNuevaTarea = tareaEquipo.getId();
        int estadoInicial = tareaEquipo.getEstado();

        // WHEN
        //El estado no se va a modificar debido a que 0 no es un estado permitido (sólo se permiten estados 1, 2, 3)
        TareaEquipo tareaEquipoModificada = tareaEquipoService.modificaTareaEquipo(idNuevaTarea, "Pagar casa", 2, null, null);
        TareaEquipo tareaEquipoBD = tareaEquipoService.findById(idNuevaTarea);

        // THEN

        assertThat(tareaEquipoModificada.getTitulo()).isEqualTo("Pagar casa");
        assertThat(tareaEquipoBD.getTitulo()).isEqualTo("Pagar casa");
        assertThat(tareaEquipoBD.getEstado()).isNotEqualTo(estadoInicial);
        assertThat(tareaEquipoBD.getEstado()).isEqualTo(2);
    }

    @Test
    @Transactional
    public void testModificarTareaEquipoUsuario() {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        TareaEquipo tareaEquipo = tareaEquipoService.nuevaTareaEquipo(1L, "Cambiar ruedas bicicleta", null, null);
        Usuario usuario = usuarioService.findById(1L);
        Long idNuevaTareaEquipo = tareaEquipo.getId();

        // WHEN
        //El estado no se va a modificar debido a que 0 no es un estado permitido (sólo se permiten estados 1, 2, 3)
        TareaEquipo tareaEquipoModificada = tareaEquipoService.modificaTareaEquipo(idNuevaTareaEquipo, "Cambiar ruedas bicicleta", 1, usuario, null);
        TareaEquipo tareaEquipoBD = tareaEquipoService.findById(idNuevaTareaEquipo);

        // THEN
        assertThat(tareaEquipoModificada.getUsuario()).isEqualTo(usuario);
        assertThat(tareaEquipoBD.getUsuario()).isEqualTo(usuario);
    }

    @Test
    @Transactional
    public void testBorrarTareaEquipo() {
        // GIVEN

        TareaEquipo tareaEquipo = tareaEquipoService.nuevaTareaEquipo(1L, "Cambiar rueda bicicleta", null, null);

        // WHEN

        tareaEquipoService.borraTareaEquipo(tareaEquipo.getId());

        // THEN

        assertThat(tareaEquipoService.findById(tareaEquipo.getId())).isNull();

    }

    @Test
    @Transactional
    public void testBorrarTareaEquipoConUsuario() {
        // GIVEN
        Usuario usuario = usuarioService.findById(1L);

        TareaEquipo tareaEquipo = tareaEquipoService.nuevaTareaEquipo(1L, "Cambiar rueda bicicleta", usuario, null);

        // WHEN

        tareaEquipoService.borraTareaEquipo(tareaEquipo.getId());

        // THEN

        assertThat(tareaEquipoService.findById(tareaEquipo.getId())).isNull();
        assertThat(usuario.getTareasEquipoAsignadas()).doesNotContain(tareaEquipo);

    }

    @Test
    @Transactional
    public void testArchivarTareaEquipo(){
        //GIVEN
        TareaEquipo tareaEquipo = tareaEquipoService.findById(1L);
        boolean archivadaInicial = tareaEquipo.isArchivada();
        //WHEN
        tareaEquipoService.archivaTarea(1L, true);

        //THEN

        assertThat(archivadaInicial).isEqualTo(false);
        assertThat(tareaEquipo.isArchivada()).isEqualTo(true);
    }

    @Test
    @Transactional
    public void usuarioNoPerteneceEquipo(){
        //GIVEN


        //WHEN


        //THEN
        assertThatThrownBy(() -> {
            tareaEquipoService.usuarioPerteneceEquipo(2L, 1L);
        }).isInstanceOf(EquipoServiceException.class);

    }

    @Test
    @Transactional
    public void testModificarTareaFechaLimite() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        TareaEquipo tareaEquipo = tareaEquipoService.nuevaTareaEquipo(1L, "Pagar el recibo", null, sdf.parse("2019-09-10"));
        Long idNuevaTarea = tareaEquipo.getId();
        Date fechaInicial = tareaEquipo.getFechalimite();

        // WHEN
        TareaEquipo tareaEquipoModificada = tareaEquipoService.modificaTareaEquipo(idNuevaTarea, "Pagar la matrícula", 2, null, sdf.parse("2019-09-25"));
        TareaEquipo tareaEquipoBD = tareaEquipoService.findById(idNuevaTarea);
        Date fechaLimite2 = tareaEquipo.getFechalimite();

        // THEN

        assertThat(tareaEquipoModificada.getTitulo()).isEqualTo("Pagar la matrícula");
        assertThat(tareaEquipoBD.getTitulo()).isEqualTo("Pagar la matrícula");
        assertThat(tareaEquipoBD.getFechalimite()).isNotEqualTo(fechaInicial);
        assertThat(tareaEquipoBD.getFechalimite()).isEqualTo(fechaLimite2);
    }
}
