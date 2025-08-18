package com.ad.base.cdi;

import com.ad.Generico.AbstractControllerGenerico;
import com.ad.Generico.EnumAccionABM;
import com.ad.Generico.GenericService;
import com.ad.base.ejb.EmpresaService;
import com.ad.base.modelo.Empresa;
import com.ad.base.modelo.Persona;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Named
@ViewScoped
public class EmpresaController extends AbstractControllerGenerico<Empresa> implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean filtroActivo;
    private boolean modoEdicion;
    private boolean filtroActivoSet = false; // Nuevo flag

    @Inject
    private EmpresaService empresaService;

    @Inject
    private PersonaController personaController;

    private Empresa empresaSeleccionadaParaEliminar;


    public boolean isFiltroActivoSet() {
        return filtroActivoSet;
    }


    @Override
    public GenericService<Empresa> getService() {
        return empresaService;
    }


    @Override
    public void reiniciarVista() {
        setEntidad(new Empresa());
        List<Empresa> lista = empresaService.listarEmpresasConRepresentante();
        if (lista == null) {
            System.out.println("listaEmpresasConRepresentante devolvió null, inicializando como vacía");
            lista = new ArrayList<>();
        }
        setLista(lista);
        setEnEdicion(false);
        setModoEdicion(false);
        empresaSeleccionadaParaEliminar = null;
        this.filtroActivoSet = false; // Reinicia el flag del filtro

        if (personaController != null) {
            personaController.reiniciarVista();
        }
    }

    @Override
    public void seleccionar() {
        setEnEdicion(true);
    }

    @Override
    public void antesABM(EnumAccionABM accion) {
        // Validaciones previas (en caso de necesitarlas)
    }

    @Override
    public List<Empresa> getLista() {
        List<Empresa> todas = super.getLista();
        if (todas == null) {
            System.out.println("Lista de empresas es null, inicializando como vacía");
            todas = new ArrayList<>();
            setLista(todas); // Actualiza la propiedad heredada
        }
        if (!isFiltroActivoSet()) {
            System.out.println("filtroActivo no establecido, devolviendo todas las empresas");
            return todas;
        }
        boolean filtro = filtroActivo;
        return todas.stream()
                .filter(emp -> filtro == emp.getActivo())
                .collect(Collectors.toList());
    }

    public void cancelarEdicion() {
        reiniciarVista();
    }

    public void prepararEdicion(Empresa empresa) {
        setEntidad(empresa);
        setEnEdicion(true);
        setModoEdicion(true);
    }

    @Override
    public void eliminar() {
        super.eliminar();
        empresaSeleccionadaParaEliminar = null;
    }

    // Lógica para cargar persona desde el diálogo
    public void seleccionarPersona(Persona persona) {
        this.entidad.setRepresentanteLegal(persona);
    }


    public void nuevo() {
        this.entidad = new Empresa();
        this.modoEdicion = true;
        this.setEnEdicion(true);
    }

    public String getNombreRepresentante() {
        Persona representante = this.entidad != null ? this.entidad.getRepresentanteLegal() : null;
        if (representante != null) {
            String nombres = representante.getNombres() != null ? representante.getNombres() : "";
            String apellidos = representante.getApellidos() != null ? representante.getApellidos() : "";
            return nombres + " " + apellidos;
        }
        return "";
    }

    /* ======== Getters y Setters que siguen siendo necesarios ======== */

    public Empresa getEmpresaSeleccionadaParaEliminar() {
        return empresaSeleccionadaParaEliminar;
    }

    public void setEmpresaSeleccionadaParaEliminar(Empresa empresaSeleccionadaParaEliminar) {
        this.empresaSeleccionadaParaEliminar = empresaSeleccionadaParaEliminar;
    }

    public Boolean getFiltroActivo() {
        return filtroActivo;
    }

    public void setFiltroActivo(boolean filtroActivo) {
        this.filtroActivo = filtroActivo;
        this.filtroActivoSet = true; // Asegura que el flag se active al establecer el filtro
    }

    public boolean isModoEdicion() {
        return modoEdicion;
    }

    public void setModoEdicion(boolean modoEdicion) {
        this.modoEdicion = modoEdicion;
    }

    public PersonaController getPersonaController() {
        return personaController;
    }

    public void setPersonaController(PersonaController personaController) {
        this.personaController = personaController;
    }
}
