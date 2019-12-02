package madstodolist.service;

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
}
