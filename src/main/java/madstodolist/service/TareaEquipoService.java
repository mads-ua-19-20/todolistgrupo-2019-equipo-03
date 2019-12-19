package madstodolist.service;

import madstodolist.controller.exception.EquipoNotFoundException;
import madstodolist.controller.exception.UsuarioNotFoundException;
import madstodolist.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class TareaEquipoService {
    private UsuarioRepository usuarioRepository;
    private EquipoRepository equipoRepository;
    private TareaEquipoRepository tareaEquipoRepository;

    @Autowired
    public TareaEquipoService(EquipoRepository equipoRepository, UsuarioRepository usuarioRepository, TareaEquipoRepository tareaEquipoRepository) {
        this.equipoRepository = equipoRepository;
        this.usuarioRepository = usuarioRepository;
        this.tareaEquipoRepository = tareaEquipoRepository;
    }

    @Transactional(readOnly = true)
    public TareaEquipo findById(Long tareaEquipoId) {
        return tareaEquipoRepository.findById(tareaEquipoId).orElse(null);
    }

    @Transactional
    public TareaEquipo nuevaTareaEquipo(Long idEquipo, String tituloTarea, Usuario usuario, Date fechalimite) {
        Equipo equipo = equipoRepository.findById(idEquipo).orElse(null);
        if (equipo == null) {
            throw new TareaServiceException("Usuario " + idEquipo + " no existe al crear tarea " + tituloTarea);
        }

        if(usuario != null) {
            if (!equipo.getUsuarios().contains(usuario)) {
                throw new TareaServiceException("El usuario no está en el equipo");
            }
        }
        TareaEquipo tareaEquipo = new TareaEquipo(equipo, tituloTarea, usuario, fechalimite);
        tareaEquipoRepository.save(tareaEquipo);
        return tareaEquipo;
    }

    @Transactional
    public void borraTareaEquipo(Long idTareaEquipo) {
        TareaEquipo tareaEquipo = tareaEquipoRepository.findById(idTareaEquipo).orElse(null);
        if (tareaEquipo == null) {
            throw new TareaServiceException("No existe tarea de equipo con id " + idTareaEquipo);
        }
        Usuario usuario = tareaEquipo.getUsuario();
        if(usuario != null) {
            usuario.getTareasEquipoAsignadas().remove(tareaEquipo);
            usuarioRepository.save(usuario);
        }
        tareaEquipoRepository.delete(tareaEquipo);
    }

    @Transactional
    public TareaEquipo modificaTareaEquipo(Long idTareaEquipo, String nuevoTitulo, int nuevoEstado, Usuario usuario, Date fechalimite) {
        TareaEquipo tareaEquipo = tareaEquipoRepository.findById(idTareaEquipo).orElse(null);
        if (tareaEquipo == null) {
            throw new TareaServiceException("No existe tarea de equipo con id " + idTareaEquipo);
        }
        tareaEquipo.setTitulo(nuevoTitulo);
        if(nuevoEstado == 1 || nuevoEstado == 2 || nuevoEstado == 3){
            tareaEquipo.setEstado(nuevoEstado);
        }

        if(usuario == null)
        {
            tareaEquipo.setUsuario(usuario);
        }
        else if(tareaEquipo.getEquipo().getUsuarios().contains(usuario)) {
            tareaEquipo.setUsuario(usuario);
        }
        else
        {
            throw new TareaServiceException("El usuario no está en el equipo, idUsuario: " + usuario.getId());
        }
        tareaEquipo.setFechalimite(fechalimite);
        tareaEquipoRepository.save(tareaEquipo);
        return tareaEquipo;
    }

    @Transactional
    public void usuarioPerteneceEquipo(Long idUsuario, Long idEquipo){
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
        if(usuario == null){
            throw new UsuarioNotFoundException();
        }
        Equipo equipo = equipoRepository.findById(idEquipo).orElse(null);
        if(equipo == null){
            throw  new EquipoNotFoundException();
        }

        if(!usuario.getEquipos().contains(equipo)){
            throw new EquipoServiceException("No se puede realizar la acción. No perteneces a este equipo");
        }
    }

    @Transactional
    public void archivaTarea(Long idTarea, boolean archivar){
        TareaEquipo tareaEquipo = tareaEquipoRepository.findById(idTarea).orElse(null);
        if (tareaEquipo == null) {
            throw new TareaServiceException("No existe tarea con id " + idTarea);
        }

        tareaEquipo.setArchivada(archivar);
        tareaEquipoRepository.save(tareaEquipo);
    }
}
