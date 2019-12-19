package madstodolist.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "equipos")
public class Equipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String nombre;

    @ManyToMany()
    @JoinTable(name = "equipo_usuario",
        joinColumns = { @JoinColumn(name = "fk_equipo") },
        inverseJoinColumns = {@JoinColumn(name = "fk_usuario")})
    Set<Usuario> usuarios = new HashSet<>();

    @OneToMany(mappedBy = "equipo", fetch = FetchType.EAGER)
    Set<TareaEquipo> tareasEquipo = new HashSet<>();

    @ManyToMany()
    @JoinTable(name = "equipo_usuariobloq",
        joinColumns =  {@JoinColumn(name = "fk_equipo")},
        inverseJoinColumns = {@JoinColumn(name = "fk_usuariobloq")})
    Set<Usuario> usuariosbloq = new HashSet<>();

    private Equipo(){};

    public Equipo(String nombre){
        this.nombre = nombre;
    }

    public String getNombre(){
        return nombre;
    }

    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    public Long getId(){
        return  id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public Set<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Set<Usuario> usuarios){
        this.usuarios = usuarios;
    }

    public Set<TareaEquipo> getTareasEquipo() {
        return tareasEquipo;
    }

    public void setTareasEquipo(Set<TareaEquipo> tareasEquipo) {
        this.tareasEquipo = tareasEquipo;
    }

    public Set<Usuario> getUsuariosbloq() {
        return usuariosbloq;
    }

    public void setUsuariosbloq(Set<Usuario> usuariosbloq){
        this.usuariosbloq = usuariosbloq;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Equipo equipo = (Equipo) o;
        if (id != null && equipo.id != null)
            // Si tenemos los ID, comparamos por ID
            return Objects.equals(id, equipo.id);
        // sino comparamos por campos obligatorios
        return nombre.equals(equipo.nombre);
    }

    @Override
    public int hashCode() {
        // Generamos un hash basado en los campos obligatorios
        return Objects.hash(nombre);
    }
}
