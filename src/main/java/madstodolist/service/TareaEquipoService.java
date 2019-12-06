package madstodolist.service;

import madstodolist.controller.exception.EquipoNotFoundException;
import madstodolist.controller.exception.UsuarioNotFoundException;
import madstodolist.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public TareaEquipo nuevaTareaEquipo(Long idEquipo, String tituloTarea) {
        Equipo equipo = equipoRepository.findById(idEquipo).orElse(null);
        if (equipo == null) {
            throw new TareaServiceException("Usuario " + idEquipo + " no existe al crear tarea " + tituloTarea);
        }
        TareaEquipo tareaEquipo = new TareaEquipo(equipo, tituloTarea);
        tareaEquipoRepository.save(tareaEquipo);
        return tareaEquipo;
    }

    @Transactional
    public void borraTareaEquipo(Long idTareaEquipo) {
        TareaEquipo tareaEquipo = tareaEquipoRepository.findById(idTareaEquipo).orElse(null);
        if (tareaEquipo == null) {
            throw new TareaServiceException("No existe tarea de equipo con id " + idTareaEquipo);
        }
        tareaEquipoRepository.delete(tareaEquipo);
    }

    @Transactional
    public TareaEquipo modificaTareaEquipo(Long idTareaEquipo, String nuevoTitulo, int nuevoEstado) {
        TareaEquipo tareaEquipo = tareaEquipoRepository.findById(idTareaEquipo).orElse(null);
        if (tareaEquipo == null) {
            throw new TareaServiceException("No existe tarea de equipo con id " + idTareaEquipo);
        }
        tareaEquipo.setTitulo(nuevoTitulo);
        if(nuevoEstado == 1 || nuevoEstado == 2 || nuevoEstado == 3){
            tareaEquipo.setEstado(nuevoEstado);
        }
        tareaEquipoRepository.save(tareaEquipo);
        return tareaEquipo;
    }

    @Transactional
    public boolean usuarioPerteneceEquipo(Long idUsuario, Long idEquipo){
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
        if(usuario == null){
            throw new UsuarioNotFoundException();
        }
        Equipo equipo = equipoRepository.findById(idEquipo).orElse(null);
        if(equipo == null){
            throw  new EquipoNotFoundException();
        }

        return usuario.getEquipos().contains(equipo);
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
