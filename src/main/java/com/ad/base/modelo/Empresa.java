package com.ad.base.modelo;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "empresa")
public class Empresa implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empresa")
    private int id;
    @Column(name = "razon_social", length = 250)
    private String razonSocial;
    @Column(name = "ruc")
    private String ruc;
    @Column(name = "direccion", length = 150)
    private String direccion;
    @Column(name = "telefono",length = 15)
    private String telefono;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_persona_representante", referencedColumnName = "id_persona")
    private Persona representanteLegal;
    @Column(name = "activa", nullable = false)
    private Boolean activo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Persona getRepresentanteLegal() {
        return representanteLegal;
    }

    public void setRepresentanteLegal(Persona persona) {
        this.representanteLegal = persona;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
