package madstodolist.service;

import madstodolist.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class EquipoService {

    private EquipoRepository equipoRepository;

    private UsuarioRepository usuarioRepository;

    private TareaEquipoRepository tareaEquipoRepository;

    @Autowired
    public EquipoService(EquipoRepository equipoRepository, UsuarioRepository usuarioRepository, TareaEquipoRepository tareaEquipoRepository){
        this.equipoRepository = equipoRepository;
        this.usuarioRepository = usuarioRepository;
        this.tareaEquipoRepository = tareaEquipoRepository;
    }

    @Transactional
    public Equipo nuevoEquipo(String nombre){
        Equipo equipo = new Equipo(nombre);
        equipoRepository.save(equipo);

        return equipo;
    }

    @Transactional
    public void agregarUsuarioEquipo(Long idEquipo, Long idUsuario){
        if(!usuarioBloqueado(idEquipo, idUsuario)) {
            Equipo equipo = equipoRepository.findById(idEquipo).orElse(null);
            if (equipo == null) {
                throw new EquipoServiceException("Equipo " + idEquipo +
                        " no existe al intentar añadirle un usuario");
            }

            Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
            if (usuario == null) {
                throw new EquipoServiceException("Usuario " + idUsuario +
                        " no existe al intentar añadirlo al equipo elegido");
            }

            equipo.getUsuarios().add(usuario);
            usuario.getEquipos().add(equipo);
        } else{
            throw new EquipoServiceException("No se puede realizar la acción. Estás bloqueado en este equipo");
        }
    }

    @Transactional
    public void eliminarUsuarioEquipo(Long idEquipo, Long idUsuario){
        if(!usuarioBloqueado(idEquipo, idUsuario)) {
            Equipo equipo = equipoRepository.findById(idEquipo).orElse(null);
            if (equipo == null) {
                throw new EquipoServiceException("Equipo " + idEquipo +
                        " no existe al intentar eliminarle un usuario");
            }
            Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
            if (usuario == null) {
                throw new EquipoServiceException("Usuario " + idUsuario +
                        " no existe al intentar eliminarlo de la lista del equipo elegido");
            }

            equipo.getUsuarios().remove(usuario);
            usuario.getEquipos().remove(equipo);
        } else{
            throw new EquipoServiceException("No se puede realizar la acción. Estás bloqueado en este equipo");
        }
    }

    @Transactional
    public Equipo modificaEquipo(Long idEquipo, String nuevoNombre){
        Equipo equipo = equipoRepository.findById(idEquipo).orElse(null);
        if(equipo == null){
            throw new EquipoServiceException("No existe un equipo con id " + idEquipo);
        }

        equipo.setNombre(nuevoNombre);
        equipoRepository.save(equipo);

        return equipo;
    }

    @Transactional
    public void borraEquipo(Long idEquipo){
        Equipo equipo = equipoRepository.findById(idEquipo).orElse(null);

        if(equipo == null){
            throw new EquipoServiceException("No existe un equipo con id " + idEquipo);
        }

        equipoRepository.delete(equipo);
    }

    @Transactional
    public void bloquearUsuario(Long idEquipo, Long idUsuario, Long idLog){
        if(idUsuario == idLog){
            throw  new EquipoServiceException("No puedes bloquearte a ti mismo");
        }
        Equipo equipo = equipoRepository.findById(idEquipo).orElse(null);
        if (equipo == null) {
            throw new EquipoServiceException("Equipo " + idEquipo +
                    " no existe al intentar eliminarle un usuario");
        }
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
        if (usuario == null) {
            throw new EquipoServiceException("Usuario " + idUsuario +
                    " no existe al intentar eliminarlo de la lista del equipo elegido");
        }

        equipo.getUsuarios().remove(usuario);
        usuario.getEquipos().remove(equipo);

        equipo.getUsuariosbloq().add(usuario);
        usuario.getEquiposbloq().add(equipo);
    }

    @Transactional
    public boolean usuarioBloqueado(Long idEquipo, Long idUsuario){
        Equipo equipo = equipoRepository.findById(idEquipo).orElse(null);
        if (equipo == null) {
            throw new EquipoServiceException("Equipo " + idEquipo +
                    " no existe al intentar eliminarle un usuario");
        }
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
        if (usuario == null) {
            throw new EquipoServiceException("Usuario " + idUsuario +
                    " no existe al intentar eliminarlo de la lista del equipo elegido");
        }

        return equipo.getUsuariosbloq().contains(usuario);
    }

    @Transactional(readOnly = true)
    public List<Equipo> findAllOrderedByName(){
        return equipoRepository.findByOrderByNombreAsc();
    }

    @Transactional(readOnly = true)
    public Equipo findById(Long equipoId) {
        return equipoRepository.findById(equipoId).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Usuario> usuariosEquipo(Long idEquipo){
        Equipo equipo = findById(idEquipo);
        return new ArrayList<>(equipo.getUsuarios());
    }

    @Transactional(readOnly = true)
    public List<TareaEquipo> tareasEquipo(Long idEquipo){
        Equipo equipo = findById(idEquipo);
        return new ArrayList<>(equipo.getTareasEquipo());
    }

    @Transactional(readOnly = true)
    public List<Equipo> findAll() { return equipoRepository.findAll(); }

}
