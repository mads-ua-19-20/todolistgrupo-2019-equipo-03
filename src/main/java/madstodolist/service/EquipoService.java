package madstodolist.service;

import madstodolist.model.Equipo;
import madstodolist.model.EquipoRepository;
import madstodolist.model.Usuario;
import madstodolist.model.UsuarioRepository;
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

    @Autowired
    public EquipoService(EquipoRepository equipoRepository, UsuarioRepository usuarioRepository){
        this.equipoRepository = equipoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Equipo nuevoEquipo(String nombre){
        Equipo equipo = new Equipo(nombre);
        equipoRepository.save(equipo);

        return equipo;
    }

    @Transactional
    public void agregarUsuarioEquipo(Long idEquipo, Long idUsuario){
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
    }

    @Transactional
    public void eliminarUsuarioEquipo(Long idEquipo, Long idUsuario){
        Equipo equipo = equipoRepository.findById(idEquipo).orElse(null);
        if (equipo == null) {
            throw new EquipoServiceException("Equipo " + idEquipo +
                    " no existe al intentar eliminarle un usuario");
        }
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
        List<Usuario> listaUsuariosEquipo = usuariosEquipo(idEquipo);
        List<Equipo> listaEquiposUsuario = new ArrayList<>(usuario.getEquipos());
        boolean done = false;

        for(int i = 0; i<listaUsuariosEquipo.size() && !done; i++){
            if(listaUsuariosEquipo.get(i).getId() == idUsuario){
                listaUsuariosEquipo.remove(i);
                done = true;
            }
        }
        if(done){
            Set<Usuario> listaActualizada = new HashSet<>();
            listaActualizada.addAll(listaUsuariosEquipo);
            equipo.setUsuarios(listaActualizada);
            done = false;
        }

        for(int i = 0; i<listaEquiposUsuario.size() && !done; i++){
            if(listaEquiposUsuario.get(i).getId() == idEquipo){
                listaEquiposUsuario.remove(i);
                done = true;
            }
        }
        if(done){
            Set<Equipo> listaActualizada = new HashSet<>();
            listaActualizada.addAll(listaEquiposUsuario);
            usuario.setEquipos(listaActualizada);
        }
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
    public List<Equipo> findAll() { return equipoRepository.findAll(); }
}
