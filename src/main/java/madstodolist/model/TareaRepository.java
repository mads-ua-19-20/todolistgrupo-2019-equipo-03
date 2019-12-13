package madstodolist.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TareaRepository extends CrudRepository<Tarea, Long> {
    @Query("SELECT t FROM Tarea t WHERE t.usuario = :usu and t.titulo = :titulo")
    List<Tarea> findAllTareasUsuarioByTitulo(
            @Param("usu") Usuario usu,
            @Param("titulo") String titulo);
}
