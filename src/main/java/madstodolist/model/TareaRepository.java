package madstodolist.model;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TareaRepository extends CrudRepository<Tarea, Long> {
    Optional<Tarea> findByTitulo(String s);
}
