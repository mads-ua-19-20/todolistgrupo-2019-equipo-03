package madstodolist.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface EquipoRepository extends CrudRepository<Equipo, Long>, JpaRepository<Equipo, Long> {
    List<Equipo> findAll();
    List<Equipo> findByOrderByNombreAsc();
}
