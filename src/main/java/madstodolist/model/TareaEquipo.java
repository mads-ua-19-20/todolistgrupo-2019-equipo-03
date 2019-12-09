package madstodolist.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "tareasequipo")
public class TareaEquipo {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String titulo;

    private int estado;

    private boolean archivada;

    @NotNull
    // Relación muchos-a-uno entre tareas y equipo
    @ManyToOne
    // Nombre de la columna en la BD que guarda físicamente
    // el ID del equipo con el que está asociado una tarea
    @JoinColumn(name = "equipo_id")
    private Equipo equipo;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    private TareaEquipo() {}

    // Al crear una tarea la asociamos automáticamente a un
    // equipo. Actualizamos por tanto la lista de tareas del
    // equipo.
    public TareaEquipo(Equipo equipo, String titulo, Usuario usuario) {
        this.equipo = equipo;
        this.titulo = titulo;
        this.estado = 1;
        this.archivada = false;
        equipo.getTareasEquipo().add(this);
        this.usuario = usuario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public boolean isArchivada() {
        return archivada;
    }

    public void setArchivada(boolean archivada) {
        this.archivada = archivada;
    }

    public Usuario getUsuario() { return usuario; }

    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TareaEquipo tarea = (TareaEquipo) o;
        return titulo.equals(tarea.titulo) &&
                equipo.equals(tarea.equipo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(titulo, equipo);
    }
}
