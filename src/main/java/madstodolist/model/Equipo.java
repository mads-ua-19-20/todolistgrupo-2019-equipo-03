package madstodolist.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "equipos")
public class Equipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String nombre;

    public Equipo(String nombre){
        this.nombre = nombre;
    }

    public String getNombre(){
        return nombre;
    }

    public Long getId(){
        return  id;
    }
}
