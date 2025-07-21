package com.ad.base.modelo;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "rol_usuario")
public class RolUsuario implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Long idRol;
    @Column(name = "nombre_rol")
    private String nombre_rol;



    public String getNombre_rol() {
        return nombre_rol;
    }

    public void setNombre_rol(String nombre_rol) {
        this.nombre_rol = nombre_rol;
    }

    public Long getIdRol() {
        return idRol;
    }

    public void setIdRol(Long idRol) {
        this.idRol = idRol;
    }

    public Long getId() {
        return idRol;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        RolUsuario that = (RolUsuario) obj;
        return idRol != null && idRol.equals(that.idRol);
    }

    @Override
    public int hashCode() {
        return 31;
    }

}
