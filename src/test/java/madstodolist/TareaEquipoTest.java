package madstodolist;

import madstodolist.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TareaEquipoTest {

    @Autowired
    EquipoRepository equipoRepository;

    @Autowired
    TareaEquipoRepository tareaEquipoRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Test
    public void crearTareaEquipo() throws Exception {
        // GIVEN
        Equipo equipo = new Equipo("Proyecto Zinc");
        Usuario usuario = new Usuario("alex@ua.es");

        // WHEN
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        TareaEquipo tareaEquipo = new TareaEquipo(equipo, "Cambiar ruedas bicicleta", usuario, sdf.parse("2019-09-10"));

        // THEN

        assertThat(tareaEquipo.getTitulo()).isEqualTo("Cambiar ruedas bicicleta");
        assertThat(tareaEquipo.getEquipo()).isEqualTo(equipo);
        assertThat(tareaEquipo.getEstado()).isEqualTo(1);
        assertThat(tareaEquipo.isArchivada()).isEqualTo(false);
        assertThat(tareaEquipo.getUsuario()).isEqualTo(usuario);
        assertThat(tareaEquipo.getFechalimite()).isEqualTo(sdf.parse("2019-09-10"));
    }

    @Test
    public void comprobarIgualdadSinId() throws Exception {
        // GIVEN
        Equipo equipo = new Equipo("Proyecto Zinc");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        TareaEquipo tareaEquipo1 = new TareaEquipo(equipo, "Cambiar ruedas bicicleta", null, sdf.parse("2019-09-10"));
        TareaEquipo tareaEquipo2 = new TareaEquipo(equipo, "Cambiar ruedas bicicleta", null, sdf.parse("2019-09-10"));
        TareaEquipo tareaEquipo3 = new TareaEquipo(equipo, "Engrasar cadena bicicleta", null, sdf.parse("2019-09-10"));

        // THEN

        assertThat(tareaEquipo1).isEqualTo(tareaEquipo2);
        assertThat(tareaEquipo2).isNotEqualTo(tareaEquipo3);
    }

    @Test
    @Transactional
    public void crearTareaEquipoEnBaseDatos() throws Exception {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        Equipo equipo = equipoRepository.findById(1L).orElse(null);
        Usuario usuario = usuarioRepository.findById(1L).orElse(null);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        TareaEquipo tareaEquipo = new TareaEquipo(equipo, "Cambiar ruedas bicicleta", usuario, sdf.parse("2019-09-10"));

        // WHEN

        tareaEquipoRepository.save(tareaEquipo);

        // THEN

        assertThat(tareaEquipo.getId()).isNotNull();
        assertThat(tareaEquipo.getEquipo()).isEqualTo(equipo);
        assertThat(tareaEquipo.getTitulo()).isEqualTo("Cambiar ruedas bicicleta");
        assertThat(tareaEquipo.getUsuario()).isEqualTo(usuario);
        assertThat(tareaEquipo.getFechalimite()).isEqualTo(sdf.parse("2019-09-10"));
    }

    @Test(expected = Exception.class)
    @Transactional
    public void salvarTareaEquipoEnBaseDatosConUsuarioNoBDLanzaExcepcion() {
        // GIVEN
        // Creamos un usuario sin ID y, por tanto, sin estar en gestionado
        // por JPA
        Equipo equipo = new Equipo("Proyecto Zinc");
        TareaEquipo tareaEquipo = new TareaEquipo(equipo, "Cambiar ruedas bicicleta", null, null);

        // WHEN

        tareaEquipoRepository.save(tareaEquipo);

        // THEN
        // Se lanza una excepción (capturada en el test)
    }

    @Test
    @Transactional(readOnly = true)
    public void unEquipoTieneUnaListaDeTareasEquipo() {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        Equipo equipo = equipoRepository.findById(1L).orElse(null);

        // WHEN
        Set<TareaEquipo> tareasEquipo = equipo.getTareasEquipo();

        // THEN

        assertThat(tareasEquipo).isNotEmpty();
    }

    @Test
    @Transactional
    public void unaTareaEquipoNuevaSeAñadeALaListaDeTareasEquipo() {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        Equipo equipo = equipoRepository.findById(1L).orElse(null);

        // WHEN

        Set<TareaEquipo> tareasEquipo = equipo.getTareasEquipo();
        TareaEquipo tareaEquipo = new TareaEquipo(equipo, "Cambiar ruedas bicicleta", null, null);
        tareaEquipoRepository.save(tareaEquipo);

        // THEN

        assertThat(equipo.getTareasEquipo()).contains(tareaEquipo);
        assertThat(tareasEquipo).isEqualTo(equipo.getTareasEquipo());
        assertThat(equipo.getTareasEquipo()).contains(tareaEquipo);
    }

    @Test
    @Transactional
    public void unaTareaEquipoNuevaSeAñadeUnUsuarioNull() {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        Equipo equipo = equipoRepository.findById(1L).orElse(null);
        Usuario usuario = null;

        // WHEN

        Set<TareaEquipo> tareasEquipo = equipo.getTareasEquipo();
        TareaEquipo tareaEquipo = new TareaEquipo(equipo, "Cambiar ruedas bicicleta", usuario, null);
        tareaEquipoRepository.save(tareaEquipo);

        // THEN

        assertThat(equipo.getTareasEquipo()).contains(tareaEquipo);
        assertThat(tareasEquipo).isEqualTo(equipo.getTareasEquipo());
        assertThat(equipo.getTareasEquipo()).contains(tareaEquipo);
        assertThat(tareaEquipo.getUsuario()).isNull();
    }

    @Test
    @Transactional
    public void unaTareaEquipoNuevaSeAñadeUnUsuario() {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        Equipo equipo = equipoRepository.findById(1L).orElse(null);
        Usuario usuario = usuarioRepository.findById(1L).orElse(null);

        // WHEN

        Set<TareaEquipo> tareasEquipo = equipo.getTareasEquipo();
        TareaEquipo tareaEquipo = new TareaEquipo(equipo, "Cambiar ruedas bicicleta", usuario, null);
        tareaEquipoRepository.save(tareaEquipo);

        // THEN

        assertThat(equipo.getTareasEquipo()).contains(tareaEquipo);
        assertThat(tareasEquipo).isEqualTo(equipo.getTareasEquipo());
        assertThat(equipo.getTareasEquipo()).contains(tareaEquipo);
        assertThat(tareaEquipo.getUsuario()).isEqualTo(usuario);
    }
}
