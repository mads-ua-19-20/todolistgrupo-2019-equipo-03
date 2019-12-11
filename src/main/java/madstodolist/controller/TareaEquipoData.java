package madstodolist.controller;

import madstodolist.model.Usuario;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class TareaEquipoData {
    private String titulo;
    private int estado;
    @DateTimeFormat(pattern="dd-MM-yyyy")
    private Date fechalimite;

    private Usuario asignacion;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public Usuario getAsignacion() {
        return asignacion;
    }

    public void setAsignacion(Usuario asignacion) {
        this.asignacion = asignacion;
    }

    public Date getFechalimite() {
        return fechalimite;
    }

    public void setFechalimite(Date fechalimite) {
        this.fechalimite = fechalimite;
    }
}
