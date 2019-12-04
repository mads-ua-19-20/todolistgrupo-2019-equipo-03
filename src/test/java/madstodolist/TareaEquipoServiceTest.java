package madstodolist;

import madstodolist.model.Equipo;
import madstodolist.model.TareaEquipo;
import madstodolist.service.EquipoService;
import madstodolist.service.TareaEquipoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TareaEquipoServiceTest {

    @Autowired
    EquipoService equipoService;

    @Autowired
    TareaEquipoService tareaEquipoService;


    @Test
    public void testNuevaTareaEquipo() {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        // WHEN
        TareaEquipo tareaEquipo = tareaEquipoService.nuevaTareaEquipo(1L, "Cambiar ruedas bicicleta");

        // THEN

        Equipo equipo = equipoService.findById(1L);
        assertThat(equipo.getTareasEquipo()).contains(tareaEquipo);
    }

    @Test
    public void testListadoTareasEquipo() {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        Equipo equipo = new Equipo("Proyecto Zinc");
        equipo.setId(1L);

        TareaEquipo tareaEquipo = new TareaEquipo(equipo, "Limpieza almacén");
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
    public void testModificarTareaEquipo() {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        TareaEquipo tareaEquipo = tareaEquipoService.nuevaTareaEquipo(1L, "Cambiar ruedas bicicleta");
        Long idNuevaTareaEquipo = tareaEquipo.getId();

        // WHEN

        TareaEquipo tareaEquipoModificada = tareaEquipoService.modificaTareaEquipo(idNuevaTareaEquipo, "Engrasar cadena", 1);
        TareaEquipo tareaEquipoBD = tareaEquipoService.findById(idNuevaTareaEquipo);

        // THEN

        assertThat(tareaEquipoModificada.getTitulo()).isEqualTo("Engrasar cadena");
        assertThat(tareaEquipoBD.getTitulo()).isEqualTo("Engrasar cadena");
    }

    @Test
    public void testBorrarTareaEquipo() {
        // GIVEN

        TareaEquipo tareaEquipo = tareaEquipoService.nuevaTareaEquipo(1L, "Cambiar rueda bicicleta");

        // WHEN

        tareaEquipoService.borraTareaEquipo(tareaEquipo.getId());

        // THEN

        assertThat(tareaEquipoService.findById(tareaEquipo.getId())).isNull();

    }
}
