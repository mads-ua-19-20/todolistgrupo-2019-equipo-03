package madstodolist;

import madstodolist.model.Equipo;
import madstodolist.model.EquipoRepository;
import madstodolist.model.Usuario;
import madstodolist.model.UsuarioRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EquipoTest {
    @Autowired
    private EquipoRepository equipoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    public void crearEquipo() {
        Equipo equipo = new Equipo("Proyecto Cobalto");
        assertThat(equipo.getNombre()).isEqualTo("Proyecto Cobalto");
    }

    @Test
    @Transactional
    public void grabarEquipo() {
        // GIVEN
        Equipo equipo = new Equipo("Proyecto Cobalto");

        // WHEN
        equipoRepository.save(equipo);

        // THEN
        assertThat(equipo.getId()).isNotNull();
    }

    @Test
    public void comprobarIgualdadEquipos() {
        // GIVEN
        // Creamos tres equipos sin id, sólo con el nombre
        Equipo equipo1 = new Equipo("Proyecto Cobalto");
        Equipo equipo2 = new Equipo("Proyecto Níquel");
        Equipo equipo3 = new Equipo("Proyecto Níquel");

        // THEN
        // Comprobamos igualdad basada en el atributo nombre
        assertThat(equipo1).isNotEqualTo(equipo2);
        assertThat(equipo2).isEqualTo(equipo3);

        // WHEN
        // Añadimos identificadores y comprobamos igualdad por identificadores
        equipo1.setId(1L);
        equipo2.setId(1L);
        equipo3.setId(2L);

        // THEN
        // Comprobamos igualdad basada en el atributo nombre
        assertThat(equipo1).isEqualTo(equipo2);
        assertThat(equipo2).isNotEqualTo(equipo3);
    }

    @Test
    public void comprobarRecuperarEquipo() {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        // WHEN

        Equipo equipo = equipoRepository.findById(1L).orElse(null);

        // THEN
        assertThat(equipo).isNotNull();
        assertThat(equipo.getId()).isEqualTo(1L);
        assertThat(equipo.getNombre()).isEqualTo("Proyecto Cobalto");
    }

    @Test
    public void relaciónMuchosAMuchosVacia() {
        // GIVEN

        Equipo equipo = new Equipo("Proyecto Cobalto");
        Usuario usuario = new Usuario("prueba@gmail.com");

        // WHEN
        // THEN

        assertThat(equipo.getUsuarios()).isEmpty();
        assertThat(usuario.getEquipos()).isEmpty();
    }

    @Test
    public void relaciónMuchosAMuchosVaciaUsuariosBlod() {
        // GIVEN

        Equipo equipo = new Equipo("Proyecto Cobalto");
        Usuario usuario = new Usuario("prueba@gmail.com");

        // WHEN
        // THEN

        assertThat(equipo.getUsuariosbloq()).isEmpty();
        assertThat(usuario.getEquiposbloq()).isEmpty();
    }

    @Test
    @Transactional
    public void comprobarRelacionBaseDatos() {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        // WHEN
        Equipo equipo = equipoRepository.findById(1L).orElse(null);
        Usuario usuario = usuarioRepository.findById(1L).orElse(null);

        // THEN

        assertThat(equipo.getUsuarios()).hasSize(1);
        assertThat(equipo.getUsuarios()).contains(usuario);
        assertThat(usuario.getEquipos()).hasSize(1);
        assertThat(usuario.getEquipos()).contains(equipo);
    }

    @Test
    @Transactional
    public void comprobarRelacionBaseDatosUsuarioBloq() {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        // WHEN
        Equipo equipo = equipoRepository.findById(1L).orElse(null);
        Usuario usuario = usuarioRepository.findById(2L).orElse(null);

        // THEN

        assertThat(equipo.getUsuariosbloq()).hasSize(1);
        assertThat(equipo.getUsuariosbloq()).contains(usuario);
        assertThat(usuario.getEquiposbloq()).hasSize(1);
        assertThat(usuario.getEquiposbloq()).contains(equipo);
    }

    @Test
    @Transactional
    public void comprobarFindAll() {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        // WHEN
        List<Equipo> equipos = equipoRepository.findAll();

        // THEN
        assertThat(equipos).hasSize(2);
    }
}