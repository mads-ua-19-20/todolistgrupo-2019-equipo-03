package madstodolist.service;

import madstodolist.model.Equipo;
import madstodolist.model.EquipoRepository;
import madstodolist.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class EquipoService {

    private EquipoRepository equipoRepository;

    @Autowired
    public EquipoService(EquipoRepository equipoRepository){
        this.equipoRepository = equipoRepository;
    }

    @Transactional
    public Equipo nuevoEquipo(String nombre){
        Equipo equipo = new Equipo(nombre);
        equipoRepository.save(equipo);

        return equipo;
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
