package madstodolist.controller;

import madstodolist.model.Usuario;

public class TareaEquipoData {
    private String titulo;
    private int estado;

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
}
