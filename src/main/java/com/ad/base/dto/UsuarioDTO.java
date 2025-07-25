package com.ad.base.dto;

import java.io.Serializable;

public class    UsuarioDTO implements Serializable {

    private String username;
    private String nombre;
    private String apellido;

    public UsuarioDTO(String username, String nombre, String apellido) {
        this.username = username;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public String getUsername() {
        return username;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
}
