package madstodolist.controller;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class TareaData {
    private String titulo;
    private int estado;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date fechalimite;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getEstado() { return estado; }

    public void setEstado(int estado) { this.estado = estado; }

    public Date getFechalimite() { return fechalimite; }

    public void setFechalimite(Date fechalimite) { this.fechalimite = fechalimite; }
}
