package madstodolist;

import madstodolist.model.Usuario;
import madstodolist.service.UsuarioService;
import madstodolist.service.UsuarioServiceException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UsuarioServiceTest {

    Logger logger = LoggerFactory.getLogger(UsuarioServiceTest.class);

    @Autowired
    private UsuarioService usuarioService;

    @Test
    public void servicioLoginUsuario() {
        // GIVEN
        // Datos cargados de datos-test.sql

        // WHEN

        UsuarioService.LoginStatus loginStatusOK = usuarioService.login("ana.garcia@gmail.com", "12345678");
        UsuarioService.LoginStatus loginStatusErrorPassword = usuarioService.login("ana.garcia@gmail.com", "000");
        UsuarioService.LoginStatus loginStatusNoUsuario = usuarioService.login("pepito.perez@gmail.com", "12345678");
        UsuarioService.LoginStatus loginStatusUserBlocked = usuarioService.login("pepe.garcia@gmail.com", "12345678");

        // THEN

        assertThat(loginStatusOK).isEqualTo(UsuarioService.LoginStatus.LOGIN_OK);
        assertThat(loginStatusErrorPassword).isEqualTo(UsuarioService.LoginStatus.ERROR_PASSWORD);
        assertThat(loginStatusNoUsuario).isEqualTo(UsuarioService.LoginStatus.USER_NOT_FOUND);
        assertThat(loginStatusUserBlocked).isEqualTo(UsuarioService.LoginStatus.USER_BLOCKED);
    }

    @Test
    public void servicioRegistroUsuario() {
        // GIVEN

        Usuario usuario = new Usuario("usuario.prueba2@gmail.com");
        usuario.setPassword("12345678");

        // WHEN

        usuarioService.registrar(usuario);

        // THEN

        Usuario usuarioBaseDatos = usuarioService.findByEmail("usuario.prueba2@gmail.com");
        assertThat(usuarioBaseDatos).isNotNull();
        assertThat(usuarioBaseDatos.getPassword()).isEqualTo(usuario.getPassword());
    }

    @Test(expected = UsuarioServiceException.class)
    public void servicioRegistroUsuarioExcepcionConNullPassword() {
        // Pasamos como argumento un usario sin contraseña
        Usuario usuario =  new Usuario("usuario.prueba@gmail.com");
        usuarioService.registrar(usuario);
    }


    @Test(expected = UsuarioServiceException.class)
    public void servicioRegistroUsuarioExcepcionConEmailRepetido() {
        // GIVEN
        // Datos cargados de datos-test.sql

        // WHEN
        // Pasamos como argumento un usario con emaii existente en datos-test.sql
        Usuario usuario =  new Usuario("ana.garcia@gmail.com");
        usuario.setPassword("12345678");
        usuarioService.registrar(usuario);

        // THEN
        // Se produce una excepción comprobada con el expected del test
    }

    @Test
    public void servicioRegistroUsuarioDevuelveUsuarioConId() {
        // GIVEN

        Usuario usuario = new Usuario("usuario.prueba@gmail.com");
        usuario.setPassword("12345678");

        // WHEN

        usuario = usuarioService.registrar(usuario);

        // THEN

        assertThat(usuario.getId()).isNotNull();
    }

    @Test
    public void servicioConsultaUsuarioDevuelveUsuario() {
        // GIVEN
        // Datos cargados de datos-test.sql

        // WHEN

        Usuario usuario = usuarioService.findByEmail("ana.garcia@gmail.com");

        // THEN

        assertThat(usuario.getId()).isEqualTo(1L);
    }

    @Test
    public void servicioConsultaUsuarioDevuelveListaUsuarios() throws ParseException {
        // GIVEN
        // Datos cargados de datos-test.sql

        // WHEN
        Usuario usuario = new Usuario("ana.garcia@gmail.com");
        usuario.setId(1L);
        usuario.setNombre("Ana García");
        usuario.setPassword("12345678");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        usuario.setFechaNacimiento(sdf.parse("2001-02-10"));
        List<Usuario> lista = new ArrayList();
        lista.add(usuario);

        List<Usuario> usuarios = usuarioService.findAll();

        // THEN

        assertThat(usuarios.equals(lista));
    }

    @Test
    public void servicioConsultaUsuarioDevuelveUsuarioAdmin() throws ParseException {
        // GIVEN
        // Datos cargados de datos-test.sql

        // WHUsuario usuario = new Usuario("ana.garcia@gmail.com");
        Usuario usuario = usuarioService.findByAdminCheck(true);

        // THEN

        assertThat(usuario.getId()).isEqualTo(1L);
    }

    @Test
    @Transactional
    public void servicioModificarBloqueoUsuario() {
        // GIVEN

        // WHEN
        usuarioService.modificaUsuario(1L, "bloquear");

        // THEN

        Usuario usuarioBaseDatos = usuarioService.findByEmail("ana.garcia@gmail.com");
        assertThat(usuarioBaseDatos).isNotNull();
        assertThat(usuarioBaseDatos.isBloqueado()).isEqualTo(true);
    }

    @Test
    @Transactional
    public void servicioModificarDesbloqueoUsuario() {
        // GIVEN

        // WHEN
        usuarioService.modificaUsuario(1L, "desbloquear");

        // THEN

        Usuario usuarioBaseDatos = usuarioService.findByEmail("ana.garcia@gmail.com");
        assertThat(usuarioBaseDatos).isNotNull();
        assertThat(usuarioBaseDatos.isBloqueado()).isEqualTo(false);
    }

    @Test
    @Transactional
    public void testModificarPerfilUsuario() throws Exception {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        // WHEN
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        usuarioService.modificaPerfilUsuario(1L, "Prueba", "prueba@ua", sdf.parse("1980-07-15"));
        //El estado no se va a modificar debido a que 0 no es un estado permitido (sólo se permiten estados 1, 2, 3)
        Usuario usuarioBD = usuarioService.findById(1L);

        // THEN

        assertThat(usuarioBD.getNombre()).isEqualTo("Prueba");
        assertThat(usuarioBD.getEmail()).isEqualTo("prueba@ua");
        assertThat(usuarioBD.getFechaNacimiento()).isEqualTo(sdf.parse("1980-07-15"));
    }
}