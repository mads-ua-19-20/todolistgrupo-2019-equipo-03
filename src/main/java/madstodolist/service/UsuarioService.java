package madstodolist.service;

import madstodolist.model.Usuario;
import madstodolist.model.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    public enum LoginStatus {LOGIN_OK, USER_NOT_FOUND, ERROR_PASSWORD, USER_BLOCKED}

    private UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public LoginStatus login(String eMail, String password) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(eMail);
        if (!usuario.isPresent()) {
            return LoginStatus.USER_NOT_FOUND;
        } else if (!usuario.get().getPassword().equals(password)) {
            return LoginStatus.ERROR_PASSWORD;
        } else if(usuario.get().isBloqueado()){
            return LoginStatus.USER_BLOCKED;
        } else {
            return LoginStatus.LOGIN_OK;
        }
    }

    // Se añade un usuario en la aplicación.
    // El email y password del usuario deben ser distinto de null
    // El email no debe estar registrado en la base de datos
    @Transactional
    public Usuario registrar(Usuario usuario) {
        Optional<Usuario> usuarioBD = usuarioRepository.findByEmail(usuario.getEmail());
        if (usuarioBD.isPresent())
            throw new UsuarioServiceException("El usuario " + usuario.getEmail() + " ya está registrado");
        else if (usuario.getEmail() == null)
            throw new UsuarioServiceException("El usuario no tiene email");
        else if (usuario.getPassword() == null)
            throw new UsuarioServiceException("El usuario no tiene password");
        else return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario modificaUsuario(Long idUsuario, String accion) {
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
        if (usuario == null) {
            throw new UsuarioServiceException("No existe usuario con id " + idUsuario);
        }else {
            if(accion.equals("bloquear")){
                usuario.setBloqueado(true);
            }
            else if(accion.equals("desbloquear")){
                usuario.setBloqueado(false);
            }
            usuarioRepository.save(usuario);
        }

        return usuario;
    }

    @Transactional
    public Usuario modificaPerfilUsuario(Long idUsuario, String nombre, String email, Date fechaNacimiento) {
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
        if (usuario == null) {
            throw new UsuarioServiceException("No existe usuario con id " + idUsuario);
        }
        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuario.setFechaNacimiento(fechaNacimiento);
        usuarioRepository.save(usuario);
        return usuario;
    }

    @Transactional(readOnly = true)
    public Usuario findByEmail(String email) {
        return usuarioRepository.findByEmail(email).orElse(null);
    }

    @Transactional(readOnly = true)
    public Usuario findById(Long usuarioId) {
        return usuarioRepository.findById(usuarioId).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Usuario> findAll() { return usuarioRepository.findAll(); }

    @Transactional(readOnly = true)
    public Usuario findByAdminCheck(boolean admin) { return usuarioRepository.findByAdminCheck(admin).orElse(null); }
}
