package madstodolist;

import madstodolist.model.Equipo;
import madstodolist.model.Usuario;
import madstodolist.service.EquipoService;
import madstodolist.service.EquipoServiceException;
import madstodolist.service.UsuarioService;
import org.hibernate.LazyInitializationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EquipoServiceTest {

    @Autowired
    EquipoService equipoService;

    @Autowired
    UsuarioService usuarioService;

    @Test
    public void obtenerListadoEquipos() {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        // WHEN
        List<Equipo> equipos = equipoService.findAllOrderedByName();

        // THEN
        assertThat(equipos).hasSize(2);
        assertThat(equipos.get(0).getNombre()).isEqualTo("Proyecto Adamantium");
        assertThat(equipos.get(1).getNombre()).isEqualTo("Proyecto Cobalto");
    }

    @Test
    public void obtenerEquipo() {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        // WHEN
        Equipo equipo = equipoService.findById(1L);

        // THEN
        assertThat(equipo.getNombre()).isEqualTo("Proyecto Cobalto");
        // Comprobamos que la relación con Usuarios es lazy: al
        // intentar acceder a la colección de usuarios se debe lanzar una
        // excepción de tipo LazyInitializationException.
        assertThatThrownBy(() -> {
            equipo.getUsuarios().size();
        }).isInstanceOf(LazyInitializationException.class);
    }

    @Test
    public void comprobarRelacionUsuarioEquipos() {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        // WHEN
        Usuario usuario = usuarioService.findById(1L);

        // THEN

        assertThat(usuario.getEquipos()).hasSize(1);
    }

    @Test
    public void obtenerUsuariosEquipo() {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        // WHEN
        List<Usuario> usuarios = equipoService.usuariosEquipo(1L);

        // THEN
        assertThat(usuarios).hasSize(1);
        assertThat(usuarios.get(0).getEmail()).isEqualTo("ana.garcia@gmail.com");
        // Comprobamos que la relación entre usuarios y equipos es eager
        // Primero comprobamos que la colección de equipos tiene 1 elemento
        assertThat(usuarios.get(0).getEquipos()).hasSize(1);
        // Y después que el elemento es el equipo Proyecto Cobalto
        assertThat(usuarios.get(0).getEquipos().stream().findFirst().get().getNombre()).isEqualTo("Proyecto Cobalto");
    }

    @Test
    public void obtenerUsuariosBloqueadosEquipo() {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        // WHEN
        List<Usuario> usuariosBloq = equipoService.usuariosBloqueadosEquipo(1L);

        // THEN
        assertThat(usuariosBloq).hasSize(1);
        assertThat(usuariosBloq.get(0).getEmail()).isEqualTo("pepe.garcia@gmail.com");
        // Comprobamos que la relación entre usuarios y equipos es eager
        // Primero comprobamos que la colección de equipos tiene 1 elemento
        assertThat(usuariosBloq.get(0).getEquiposbloq()).hasSize(1);
        // Y después que el elemento es el equipo Proyecto Cobalto
        assertThat(usuariosBloq.get(0).getEquiposbloq().stream().findFirst().get().getNombre()).isEqualTo("Proyecto Cobalto");
    }

    @Test
    @Transactional
    public void testNuevoEquipo(){
        //WHEN
        Equipo equipo = equipoService.nuevoEquipo("Equipo de prueba");

        //THEN
        assertThat(equipoService.findAll()).contains(equipo);
    }

    @Test
    @Transactional
    public void testAgregarUsuarioEquipo(){
        //GIVEN
        Equipo equipo = equipoService.findById(2L);
        Usuario usuario = usuarioService.findById(2L);

        //WHEN
        equipoService.agregarUsuarioEquipo(2L, 2L);

        //THEN
        assertThat(equipo.getUsuarios()).contains(usuario);
    }

    @Test
    public void testAgregarUsuarioEquipoInexistente(){
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        //WHEN

        //THEN
        assertThatThrownBy(() -> {
            equipoService.agregarUsuarioEquipo(0L, 2L);
        }).isInstanceOf(EquipoServiceException.class);
    }

    @Test
    public void testAgregarUsuarioInexistenteEquipo(){
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        //WHEN

        //THEN
        assertThatThrownBy(() -> {
            equipoService.agregarUsuarioEquipo(1L, 0L);
        }).isInstanceOf(EquipoServiceException.class);
    }

    @Test
    @Transactional
    public void testAgregarUsuarioEquipo2(){
        //GIVEN
        Equipo equipo = equipoService.findById(2L);
        Usuario usuario = usuarioService.findById(2L);

        //WHEN
        equipoService.agregarUsuarioEquipo(2L, 2L);

        //THEN
        assertThat(usuario.getEquipos()).contains(equipo);
    }

    @Test
    @Transactional
    public void testEliminarUsuarioEquipo(){
        //GIVEN
        Equipo equipo = equipoService.findById(1L);
        Usuario usuario = usuarioService.findById(1L);

        //WHEN
        equipoService.eliminarUsuarioEquipo(1L, 1L);

        //THEN
        assertThat(equipo.getUsuarios()).doesNotContain(usuario);
    }

    @Test
    @Transactional
    public void testEliminarUsuarioEquipo2(){
        //GIVEN
        Equipo equipo = equipoService.findById(1L);
        Usuario usuario = usuarioService.findById(1L);

        //WHEN
        equipoService.eliminarUsuarioEquipo(1L, 1L);

        //THEN
        assertThat(usuario.getEquipos()).doesNotContain(equipo);
    }

    @Test
    public void testEliminaUsuarioEquipoInexistente(){
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        //WHEN

        //THEN
        assertThatThrownBy(() -> {
            equipoService.eliminarUsuarioEquipo(0L, 1L);
        }).isInstanceOf(EquipoServiceException.class);
    }

    @Test
    public void testEliminaUsuarioInexistenteEquipo(){
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        //WHEN

        //THEN
        assertThatThrownBy(() -> {
            equipoService.eliminarUsuarioEquipo(1L, 0L);
        }).isInstanceOf(EquipoServiceException.class);
    }

    @Test
    @Transactional
    public void cambiarNombreEquipo(){
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        Equipo equipo = equipoService.findById(1L);
        Long idEquipo = equipo.getId();

        // WHEN

        Equipo equipoModificado = equipoService.modificaEquipo(idEquipo, "Proyecto nombre cambiado");
        Equipo equipoBD = equipoService.findById(idEquipo);

        // THEN

        assertThat(equipoModificado.getNombre()).isEqualTo("Proyecto nombre cambiado");
        assertThat(equipoBD.getNombre()).isEqualTo("Proyecto nombre cambiado");

    }

    @Test
    public void cambiarNombreEquipoInexistente(){
        //GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        //WHEN

        //THEN
        assertThatThrownBy(() -> {
            equipoService.modificaEquipo(0L, "Equipo B");
        }).isInstanceOf(EquipoServiceException.class);
    }

    @Test
    @Transactional
    public void eliminarEquipo(){
        // GIVEN
        Equipo equipo = equipoService.findById(1L);

        // WHEN
        equipoService.borraEquipo(equipo.getId());
        // THEN
        assertThat(equipoService.findById(equipo.getId())).isNull();
    }

    @Test
    public void eliminarEquipoInexistente(){
        //GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        //WHEN

        //THEN
        assertThatThrownBy(() -> {
            equipoService.borraEquipo(0L);
        }).isInstanceOf(EquipoServiceException.class);
    }

    @Test
    @Transactional
    public void bloquearUsuario(){
        //GIVEN
        Usuario usuario = usuarioService.findById(1L);
        Equipo equipo = equipoService.findById(1L);

        //WHEN
        equipoService.bloquearUsuario(1L, 1L, 3L, true);

        //THEN
        assertThat(equipo.getUsuarios()).doesNotContain(usuario);
        assertThat(equipo.getUsuariosbloq()).contains(usuario);
    }

    @Test
    @Transactional
    public void desbloquearUsuario(){
        //GIVEN
        Usuario usuario = usuarioService.findById(2L);
        Equipo equipo = equipoService.findById(1L);
        boolean estabaBloqueado = equipo.getUsuariosbloq().contains(usuario);
        //WHEN
        equipoService.bloquearUsuario(1L, 2L, 3L, false);

        //THEN
        assertThat(equipo.getUsuariosbloq()).doesNotContain(usuario);
        assertThat(equipo.getUsuarios()).contains(usuario);
        assertThat(estabaBloqueado).isEqualTo(true);
    }

    @Test
    public void bloquearUsuarioLogeado(){
        //GIVEN


        //WHEN


        //THEN
        assertThatThrownBy(() -> {
            equipoService.bloquearUsuario(1L, 2L, 2L, true);
        }).isInstanceOf(EquipoServiceException.class);
    }

    @Test
    public void bloquearUsuarioInexistente(){
        //GIVEN


        //WHEN


        //THEN
        assertThatThrownBy(() -> {
            equipoService.bloquearUsuario(1L, 0L, 2L, true);
        }).isInstanceOf(EquipoServiceException.class);
    }

    @Test
    public void bloquearUsuarioEquipoInexistente(){
        //GIVEN


        //WHEN


        //THEN
        assertThatThrownBy(() -> {
            equipoService.bloquearUsuario(0L, 1L, 2L, true);
        }).isInstanceOf(EquipoServiceException.class);
    }

    @Test
    @Transactional
    public void comprobarUsuarioBloqueado(){
        //GIVEN

        //WHEN
        boolean resultado1 = equipoService.usuarioBloqueado(1L, 2L);
        boolean resultado2 = equipoService.usuarioBloqueado(1L, 1L);

        //THEN
        assertThat(resultado1).isEqualTo(true);
        assertThat(resultado2).isEqualTo(false);
    }
}